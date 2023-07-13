package com.example.todoapp.screens.main

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.Application
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.di.main.MainFragmentComponent
import com.example.todoapp.model.Resource
import com.example.todoapp.model.TodoItem
import com.example.todoapp.network.model.SynchronizationWorkerFactory
import com.example.todoapp.screens.adapter.MainAdapter
import com.example.todoapp.screens.adapter.MainItemTouchHelper
import com.example.todoapp.screens.adding.AddingFragmentViewModel
import com.example.todoapp.screens.adding.AddingFragmentViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var adapterToDo: MainAdapter

    @Inject
    lateinit var mainViewModelFactory: MainFragmentViewModelFactory
    private lateinit var mViewModel: MainFragmentViewModel
    private lateinit var mainFragmentComponent: MainFragmentComponent

    @Inject
    lateinit var mainItemTouchHelperFactory: MainItemTouchHelper.Factory
    private var mainItemTouchHelperCallback: MainItemTouchHelper? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainFragmentComponent =
            (requireContext().applicationContext as Application).appComponent.mainFragmentComponent().create()
        mainFragmentComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel =
            ViewModelProvider(this, mainViewModelFactory)[MainFragmentViewModel::class.java]
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialization()
        mBinding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addingFragment)
        }
        mainItemTouchHelperCallback = mainItemTouchHelperFactory.create(mViewModel, view, adapterToDo)
        setItemTouchHelper()
        mViewModel.getListTodoItems()
        mViewModel.getTodoItemsLiveData().observe(viewLifecycleOwner) {
            adapterToDo.submitList(it)
            adapterToDo.notifyDataSetChanged()
        }
    }


    private fun initialization() {
        setRecyclerView()
        setResourseObserver()
        setSwipeRefresh()
        setSettingListener()
        //setInternetStatusUi()
    }

    private fun setRecyclerView() {
        mBinding?.apply {
            adapterToDo = MainAdapter(
                MainAdapter.OnClickListener { setOnClickListenerRV(it) },
                MainAdapter.OnLongClickListener { item, view ->
                    setOnLongClickListener(item, view)
                },
                MainAdapter.OnCheckBoxClickListener { item, flag, view ->
                    setOnCheckBoxListener(item, flag, view)
                })
            var linearLayoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
            }

            recyclerviewDo.layoutManager = linearLayoutManager
            recyclerviewDo.adapter = adapterToDo
        }
    }

    private fun setOnClickListenerRV(item: TodoItem) {
        val bundle = bundleOf("item" to item)
        findNavController().navigate(
            R.id.action_mainFragment_to_addingFragmentCompose,
            bundle
        )
    }
    private fun setOnCheckBoxListener(item: TodoItem, flag: Boolean, view: View) {
        Log.e("CheckBoxListener", item.toString())
        mViewModel.editTodoItem(editTodoItem(item, flag))
    }

    private fun setOnLongClickListener(item: TodoItem, view: View) {
        showPopUpMenu(item, view)
    }

    private fun editTodoItem(todoItem: TodoItem, flag: Boolean): TodoItem {
        return todoItem.copy(
            flag = flag
        )
    }

    private fun showPopUpMenu(item: TodoItem, view: View) {

        var popupMenu = context?.let { PopupMenu(it, view) }
        popupMenu?.inflate(R.menu.popup_menu);
        popupMenu?.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_edit -> {
                    val bundle = bundleOf("item" to item)
                    findNavController().navigate(
                        R.id.action_mainFragment_to_addingFragment,
                        bundle
                    )
                    true
                }

                R.id.menu_delete -> {
                    mViewModel.deleteTodoItem(item, item.id)
                    true
                }

                else -> false
            }
        }
        popupMenu?.show();
    }

    private fun setResourseObserver() {
        mViewModel.getResourseLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    Snackbar.make(
                        mBinding.recyclerviewDo,
                        it.message.toString(),
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Повторить", View.OnClickListener {
                        }).show()
                }

                is Resource.Success -> {
                }
            }
        }
    }

    private fun setItemTouchHelper() {

        mainItemTouchHelperCallback?.let {
            ItemTouchHelper(it).attachToRecyclerView(mBinding.recyclerviewDo)
        }


    }

    private fun setSwipeRefresh() {
        mBinding.swipeRefresh.setOnRefreshListener {
            mViewModel.getListTodoItems()
            if (!mViewModel.checkInternetConnection()) {
                Snackbar.make(
                    mBinding.recyclerviewDo,
                    "Отсутствует интернет соединение",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Обновить", View.OnClickListener {
                        mViewModel.getListTodoItems()
                    }).show()
            }
            mBinding.swipeRefresh.isRefreshing = false
        }
    }

    private fun setInternetStatusUi() {
        lifecycleScope.launch {
            mViewModel.internetConnection.collect {
                //if(it) mBinding.internetConnection.visibility = View.VISIBLE
                //else mBinding.internetConnection.visibility = View.GONE
            }
        }
    }

    private fun setSettingListener(){
        mBinding.settings.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
        }
    }
}