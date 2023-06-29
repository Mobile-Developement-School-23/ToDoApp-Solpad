package com.example.todoapp.screens.adapter

import MainViewHolder
import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import com.example.todoapp.R
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.util.ItemDiffCallback

class MainAdapter(private val onClickListener: OnClickListener, private val onLongClickListener: OnLongClickListener) :
    ListAdapter<TodoItem, MainViewHolder>(ItemDiffCallback()) {


    class OnClickListener(val clickListener: (item: TodoItem) -> Unit) {
        fun onClick(item: TodoItem) = clickListener(item)
    }

    class OnLongClickListener(val longClickListener: (item: TodoItem,view:View) -> Unit) {
        fun onClick(item: TodoItem, itemView: View) = longClickListener(item,itemView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return MainViewHolder(itemView)
    }


    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))

        onSetListenerButtonInfo(holder)
        Log.e("AdapterPosition",position.toString())
        onSetListenerCheckBox(holder, position)

//       клик для редактирования
        holder.itemView.setOnClickListener {
            onClickListener.onClick(getItem(position))
        }

        //лонгклик
        holder.itemView.setOnLongClickListener {
            onLongClickListener.onClick(getItem(position),holder.itemView)
            return@setOnLongClickListener true
        }

        // заполнение importance
        if (getItem(position).importance == Importance.LOW) {
            holder.importanceItem.visibility = View.VISIBLE
            holder.importanceItem.setImageResource(R.drawable.ic_arrow_to_down)

        }
        if (getItem(position).importance == Importance.BASIC) {
            holder.importanceItem.visibility = View.GONE
        }

        if (getItem(position).importance == Importance.IMPORTANT) {
            holder.importanceItem.visibility = View.VISIBLE
            holder.importanceItem.setImageResource(R.drawable.ic_priority)
        }


        //checkbox
        if (getItem(position).flag == true) {
            holder.checkBoxItem.isChecked = true
            holder.textItem.paintFlags = holder.textItem.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        }

        if (getItem(position).importance == Importance.IMPORTANT) {
            val tint = ContextCompat.getColorStateList(holder.itemView.context,R.drawable.checkbox_extra_tint)
            val drawable = getDrawable(holder.itemView.context,R.drawable.checkbox_extra_drawable)
            holder.checkBoxItem.buttonDrawable = drawable

            holder.checkBoxItem.buttonTintList = tint
        } else{
            val tint = ContextCompat.getColorStateList(holder.itemView.context,R.drawable.checkbox_normal_tint)
            holder.checkBoxItem.buttonTintList = tint
        }


    }
    private fun onSetListenerCheckBox(holder: MainViewHolder, position: Int) {
        holder.checkBoxItem.setOnCheckedChangeListener { buttonView, isChecked ->
            Log.e("AdapterBef",getItem(position).toString())
            getItem(position).flag = isChecked
            Log.e("AdapterAft",getItem(position).toString())
            if (getItem(position).flag == true) {
                holder.textItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else if (getItem(position).flag == false) {
                holder.textItem.paintFlags = Paint.ANTI_ALIAS_FLAG
            }
        }
    }
    private fun onSetListenerButtonInfo(holder: MainViewHolder) {
        holder.infoItem.setOnClickListener {
            if (holder.deathlineItem.visibility == View.GONE) holder.deathlineItem.visibility =
                View.VISIBLE
            else if (holder.deathlineItem.visibility == View.VISIBLE) holder.deathlineItem.visibility =
                View.GONE
        }
    }

}
