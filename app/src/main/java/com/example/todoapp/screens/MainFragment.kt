package com.example.todoapp.screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.adapter.MainAdapter
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository

class MainFragment : Fragment() {

    private lateinit var mViewModel:MainFragmentViewModel
    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var adapterToDo: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProvider(this)[MainFragmentViewModel::class.java]
        _binding = FragmentMainBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerView()
        setCompletedTask()
        mBinding.addButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addingFragment)
        }
    }

    private fun setCompletedTask(){
        mViewModel.completedTask?.observe(viewLifecycleOwner, Observer {
            mBinding.completeTitle.text = "Выполнено — " + it.toString()
        })
    }
    fun initRecyclerView(){
        _binding?.apply {
            adapterToDo = MainAdapter(TodoItemsRepository().getTodoItems(),MainAdapter.OnClickListener{
                setOnClickListenerRV(it)
            })
            recyclerviewDo.layoutManager = LinearLayoutManager(context)
            recyclerviewDo.adapter = adapterToDo
        }
    }
    private fun setOnClickListenerRV(item: TodoItem){
        Log.e("setOnClickListenerRV",item.toString())
        val bundle = bundleOf("item" to item)
        findNavController().navigate(
            R.id.action_mainFragment_to_addingFragment,
            bundle
        )
        }
    }