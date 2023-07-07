package com.example.todoapp.screens.adding

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.Application
import com.example.todoapp.R
import com.example.todoapp.util.Utils
import com.example.todoapp.databinding.FragmentAddingBinding
import com.example.todoapp.di.adding.AddingFragmentComponent
import com.example.todoapp.model.TodoItem
import com.google.android.material.datepicker.MaterialDatePicker
import javax.inject.Inject

class AddingFragment : Fragment() {
    private var _binding: FragmentAddingBinding? = null
    private val mBinding get() = _binding!!

    @Inject
    lateinit var addingViewModelFactory: AddingFragmentViewModelFactory
    private lateinit var mViewModel: AddingFragmentViewModel
    private lateinit var addingFragmentComponent: AddingFragmentComponent

    var todoItemBundle: TodoItem? = null
    var deathline: Long = 0L
    override fun onAttach(context: Context) {
        super.onAttach(context)
        addingFragmentComponent =
            (requireContext().applicationContext as Application).appComponent.addingFragmentComponent()
                .create()
        addingFragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddingBinding.inflate(layoutInflater, container, false)

        val arguments = arguments
        if (arguments != null) todoItemBundle = arguments.get("item") as TodoItem
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel =
            ViewModelProvider(this, addingViewModelFactory)[AddingFragmentViewModel::class.java]
        initialization()
    }

    private fun createNewTodoItem(): TodoItem {
        return TodoItem(
            id = "1",
            content = mBinding.edittextAdding.text.toString(),
            importance = Utils().convertIdToImportance(mBinding.spinnerAdding.selectedItemPosition),
            flag = false,
            dateCreated = System.currentTimeMillis(),
            dateChanged = System.currentTimeMillis(),
            deadline = deathline,
        )
    }

    private fun editTodoItem(todoItem: TodoItem): TodoItem {
        return todoItem.copy(
            content = mBinding.edittextAdding.text.toString(),
            importance = Utils().convertIdToImportance(mBinding.spinnerAdding.selectedItemPosition),
            dateChanged = System.currentTimeMillis(),
            deadline = deathline
        )
    }

    private fun initialization() {
        todoItemBundle?.let { initializationItemInfo(it) }
        switchAndCalendarInit()
        closeSetListenerInit()
        saveSetListenerInit()
        deleteSetListenerInit()
    }

    private fun initializationItemInfo(todoItemInit: TodoItem) {
        mBinding.edittextAdding.append(todoItemInit.content)

        if (todoItemInit.deadline != 0L) {
            mBinding.switchdeatline.isChecked = true
            mBinding.textviewCalendardate.visibility = View.VISIBLE
            mBinding.textviewCalendardate.text = todoItemInit.deadline?.let {
                Utils().convertLongDeathlineToString(it)
            }
        }
        todoItemInit.importance.let { Utils().convertImportanceToId(it) }
            ?.let { mBinding.spinnerAdding.setSelection(it) }
    }

    private fun closeSetListenerInit() {
        mBinding.closeButton.setOnClickListener {
            findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
        }
    }

    private fun saveSetListenerInit() {
        mBinding.saveButton.setOnClickListener {
            if (mBinding.edittextAdding.text.toString() != "") {
                if (todoItemBundle != null) {
                    mViewModel.editItemToList(editTodoItem(todoItemBundle!!))
                    findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
                } else {
                    mViewModel.addItemToList(createNewTodoItem())
                    findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
                }
            } else Toast.makeText(context, "Заметка пустая!", Toast.LENGTH_SHORT)
        }
    }

    private fun deleteSetListenerInit() {
        mBinding.deleteButton.setOnClickListener {
            if (todoItemBundle != null) {
                mViewModel.deleteTodoItem(todoItemBundle!!, todoItemBundle!!.id)
                findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
            } else findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
        }
    }

    private fun switchAndCalendarInit() {
        var datepicker = MaterialDatePicker.Builder.datePicker()
        var materialDatePicker = datepicker.build()

        mBinding.switchdeatline.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) materialDatePicker.show(parentFragmentManager, "tag")
            else mBinding.textviewCalendardate.visibility = View.INVISIBLE
        }

        mBinding.textviewCalendardate.setOnClickListener {
            materialDatePicker.show(parentFragmentManager, "tag")
        }

        materialDatePicker.addOnPositiveButtonClickListener {
            mBinding.textviewCalendardate.visibility = View.VISIBLE
            mBinding.textviewCalendardate.text = materialDatePicker.headerText.toString()
            deathline = it
        }
    }
}