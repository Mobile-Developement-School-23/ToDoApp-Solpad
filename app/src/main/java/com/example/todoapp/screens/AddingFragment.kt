package com.example.todoapp.screens

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todoapp.R
import com.example.todoapp.Utils
import com.example.todoapp.databinding.FragmentAddingBinding
import com.example.todoapp.databinding.FragmentMainBinding
import com.example.todoapp.model.TodoItem
import com.example.todoapp.model.TodoItemsRepository
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


class AddingFragment : Fragment() {

    private var _binding: FragmentAddingBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mViewModel: AddingFragmentViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddingBinding.inflate(layoutInflater,container,false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(this)[AddingFragmentViewModel::class.java]

        initialization()
    }

    var deathline:Long = 0L

    private fun createNewTodoItem() = TodoItem(
        id= "1",
        content = mBinding.edittextAdding.text.toString(),
        importance = Utils().convertIdToImportance(mBinding.spinnerAdding.selectedItemPosition),
        flag = false,
        dateCreated =System.currentTimeMillis(),
        dateChanged = System.currentTimeMillis(),
        deadline = deathline,
    )

    private fun initialization(){
        switchAndCalendarInit()
        closeSetListenerInit()
        saveSetListenerInit()
        deleteSetListenerInit()
    }

    private fun closeSetListenerInit(){
        mBinding.closeButton.setOnClickListener {
            findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
        }
    }

    private fun saveSetListenerInit(){
        mBinding.saveButton.setOnClickListener {
            var item = createNewTodoItem()
            mViewModel.addItemToList(createNewTodoItem())
            findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
        }
    }
    private fun deleteSetListenerInit(){

        mBinding.deleteButton.setOnClickListener {
            findNavController().navigate(R.id.action_addingFragment_to_mainFragment)
        }
    }
    private fun switchAndCalendarInit(){
        var datepicker = MaterialDatePicker.Builder.datePicker()
        var materialDatePicker = datepicker.build()
        mBinding.switchdeatline.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                materialDatePicker.show(parentFragmentManager,"tag")

            }
            else mBinding.textviewCalendardate.visibility = View.INVISIBLE
        }

        mBinding.textviewCalendardate.setOnClickListener {
                materialDatePicker.show(parentFragmentManager,"tag")
        }

        materialDatePicker.addOnPositiveButtonClickListener {
            mBinding.textviewCalendardate.visibility = View.VISIBLE
            mBinding.textviewCalendardate.text = materialDatePicker.headerText.toString()
            deathline = it
        }

    }
}