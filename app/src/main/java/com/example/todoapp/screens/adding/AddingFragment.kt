package com.example.todoapp.screens.adding

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Application
import com.example.todoapp.di.adding.AddingFragmentComponent
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.AlarmReceiver
import com.example.todoapp.screens.adding.component.TodoAppBar
import com.example.todoapp.screens.adding.component.TodoDataPicker
import com.example.todoapp.screens.adding.component.TodoDeleteButton
import com.example.todoapp.screens.adding.component.TodoEditText
import com.example.todoapp.screens.adding.component.TodoImportante
import com.example.todoapp.screens.adding.component.TodoDiviver
import com.example.todoapp.screens.ui.TodoAppCustomTheme
import com.example.todoapp.screens.ui.TodoAppTheme
import com.example.todoapp.util.convertImportanceToString
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import javax.inject.Inject

class AddingFragment : Fragment() {

    @Inject
    lateinit var addingViewModelFactory: AddingFragmentViewModelFactory
    private lateinit var mViewModel: AddingFragmentViewModel
    private lateinit var addingFragmentComponent: AddingFragmentComponent
    var todoItemBundle: TodoItem? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        addingFragmentComponent =
            (requireContext().applicationContext as Application).appComponent.addingFragmentComponent()
                .create()
        addingFragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val arguments = arguments
        if (arguments != null) todoItemBundle = arguments.get("item") as TodoItem

        Log.e("Bundle", todoItemBundle.toString())
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TodoAppTheme {
                    val editTextValue = remember {
                        if (todoItemBundle != null) mutableStateOf(todoItemBundle!!.content)
                        else mutableStateOf("")
                    }
                    val importantValue = remember {
                        if (todoItemBundle != null) mutableStateOf(todoItemBundle!!.importance)
                        else mutableStateOf(Importance.BASIC)
                    }
                    val deathlineValue = remember {
                        if (todoItemBundle != null) mutableStateOf(todoItemBundle!!.deadline)
                        else mutableStateOf(System.currentTimeMillis())
                    }
                    val deathlineString = remember {
                        if (todoItemBundle != null) mutableStateOf("00:00")
                        else mutableStateOf("00:00")
                    }
                    val dataStateVisible = remember {
                        if (todoItemBundle != null && todoItemBundle!!.deadline != 0L)
                            mutableStateOf(true)
                        else mutableStateOf(false)
                    }
                    val enableClick = remember {
                        if (todoItemBundle != null )
                            mutableStateOf(true)
                        else mutableStateOf(false)
                    }
                    var listState = rememberLazyListState()
                    val topBarElevation by animateDpAsState(
                        if(listState.canScrollBackward) 8.dp else 0.dp,
                        label = "top bar elevation"
                    )
                    val clockState = rememberUseCaseState()

                    enableClick.value = editTextValue.value.isNotEmpty()
                    Scaffold(
                        topBar = {
                            TodoAppBar(enableClick.value, topBarElevation,
                                {
                                    saveSetListenerInit(
                                        editTextValue.value,
                                        importantValue.value,
                                        deathlineValue.value
                                    )
                                },
                                { findNavController().popBackStack() }
                            )
                        },
                        containerColor = TodoAppCustomTheme.colors.backPrimary
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            state = listState
                        ) {
                            item {

                                TodoEditText(editTextValue)
                                TodoImportante(importantValue)
                                TodoDiviver(PaddingValues(horizontal = 16.dp))
                                TodoDataPicker(deathlineValue,deathlineString, dataStateVisible)
                                TodoDiviver(PaddingValues(horizontal = 16.dp))
                                TodoDeleteButton(enableClick.value) { deleteSetListenerInit() }
                            }
                            item {
                                Spacer(modifier = Modifier.height(96.dp))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel =
            ViewModelProvider(this, addingViewModelFactory)[AddingFragmentViewModel::class.java]
    }

    private fun saveSetListenerInit(contentValue: String, importance: Importance, deathline: Long) {
        if (contentValue != "") {
            if (todoItemBundle != null) {
                mViewModel.editItemToList(
                    editTodoItem(
                        todoItemBundle!!,
                        contentValue,
                        importance,
                        deathline
                    )
                )
                setAlarm(deathline, content = contentValue,importance = importance)
                findNavController().popBackStack()
            } else {
                setAlarm(deathline,content = contentValue,importance = importance)
                mViewModel.addItemToList(createNewTodoItem(contentValue, importance, deathline))
                findNavController().popBackStack()
            }
        } else Toast.makeText(context, "Заметка пустая!", Toast.LENGTH_SHORT).show()

    }

    private fun deleteSetListenerInit() {
        if (todoItemBundle != null) {
            mViewModel.deleteTodoItem(todoItemBundle!!, todoItemBundle!!.id)
            findNavController().popBackStack()
        } else findNavController().popBackStack()

    }

    private fun createNewTodoItem(
        contentValue: String,
        importance: Importance,
        deathline: Long
    ): TodoItem {
        return TodoItem(
            id = "1",
            content = contentValue,
            importance = importance,
            flag = false,
            dateCreated = System.currentTimeMillis(),
            dateChanged = System.currentTimeMillis(),
            deadline = deathline,
        )
    }

    private fun editTodoItem(
        todoItem: TodoItem,
        contentValue: String,
        importance: Importance,
        deathline: Long
    ): TodoItem {
        return todoItem.copy(
            content = contentValue,
            importance = importance,
            dateChanged = System.currentTimeMillis(),
            deadline = deathline
        )
    }

    private fun setAlarm(deathline: Long,
                         content: String,
                         importance: Importance,) {
        Log.e("alarm",deathline.toString())
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("content",content)
            putExtra("importance", convertImportanceToString(importance))
            Log.e("ALALA","$content,${convertImportanceToString(importance)}")
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            deathline,
            pendingIntent )

    }

}


