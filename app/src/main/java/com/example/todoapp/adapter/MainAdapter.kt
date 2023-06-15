package com.example.todoapp.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.Utils
import com.example.todoapp.databinding.TodoItemBinding
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem

class MainAdapter(val todoItemList:List<TodoItem>): RecyclerView.Adapter<MainAdapter.MainViewHolder>() {


    class MainViewHolder(view: View):RecyclerView.ViewHolder(view) {

        val binding = TodoItemBinding.bind(view)
        var infoItem = binding.infoItem
        var deathlineItem = binding.deathlineItem
        var checkBoxItem = binding.checkboxItem
        var importanceItem = binding.importanceItem
        var textItem = binding.textItem

        fun bind(item:TodoItem) = with(binding){
            textItem.text = item.content
            if(item.deadline != 0L)  deathlineItem.text = item.deadline?.let { Utils().convertLongDeathlineToString(it) }
            else deathlineItem.text = "Дедлайна нет, гуляем"
        }
     }

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
          val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item,parent,false)
         return MainViewHolder(itemView)
     }


     override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
         holder.bind(todoItemList[position])
         onSetListenerButtonInfo(holder)
         onSetListenerCheckBox(holder,position)

         holder.itemView.setOnClickListener {

         }


         if (todoItemList[position].importance == Importance.LOW){
             holder.importanceItem.visibility = View.VISIBLE
             holder.importanceItem.setImageResource(R.drawable.ic_arrow_to_down)

         }
         if (todoItemList[position].importance == Importance.URGENT){
             holder.importanceItem.visibility = View.VISIBLE
             holder.importanceItem.setImageResource(R.drawable.ic_priority)
         }
         if (todoItemList[position].importance == Importance.ORDINARY){
             holder.importanceItem.visibility = View.GONE
         }

         //checkbox
         if (todoItemList[position].flag == true) holder.checkBoxItem.isChecked = true

         if (todoItemList[position].flag == true){
             holder.textItem.paintFlags = holder.textItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

         }
         /*
         if(list[position].importance == Importance.URGENT) {
             holder.checkBoxItem.buttonTintList = ColorStateList(
                 arrayOf(intArrayOf(android.R.attr.state_pressed), intArrayOf()),
                 intArrayOf(
                    R.color.red, R.color.red))
         }

          */
     }

     override fun getItemCount(): Int {
        return todoItemList.size
     }

    private fun onSetListenerCheckBox(holder: MainViewHolder,position: Int){
        holder.checkBoxItem.setOnCheckedChangeListener { buttonView, isChecked ->
            todoItemList[position].flag = isChecked

            if (todoItemList[position].flag == true){
                holder.textItem.paintFlags =  Paint.STRIKE_THRU_TEXT_FLAG
            }
            else if (todoItemList[position].flag == false) {
                holder.textItem.paintFlags = Paint.ANTI_ALIAS_FLAG
            }
        }
    }
    private fun onSetListenerButtonInfo(holder: MainViewHolder){
         holder.infoItem.setOnClickListener {
             if(holder.deathlineItem.visibility == View.GONE) holder.deathlineItem.visibility = View.VISIBLE
             else if(holder.deathlineItem.visibility == View.VISIBLE) holder.deathlineItem.visibility = View.GONE
         }
     }

    private var onItemClickListener: ((TodoItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (TodoItem) -> Unit) {
        onItemClickListener = listener
    }

    private fun onSetListenerCheckBox(holder:MainViewHolder){

    }
 }
