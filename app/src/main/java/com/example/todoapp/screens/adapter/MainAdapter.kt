package com.example.todoapp.screens.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.databinding.TodoItemBinding
import com.example.todoapp.model.Importance
import com.example.todoapp.model.TodoItem
import com.example.todoapp.util.ItemDiffCallback
import com.example.todoapp.util.Utils
import javax.inject.Inject

class MainAdapter (
    private val onClickListener: OnClickListener,
    private val onLongClickListener: OnLongClickListener,
    private val onCheckBoxClickListener: OnCheckBoxClickListener,
) :
    ListAdapter<TodoItem, MainAdapter.MainViewHolder>(ItemDiffCallback()) {



    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val binding = TodoItemBinding.bind(view)
        var infoItem = binding.infoItem
        var deathlineItem = binding.deathlineItem
        var checkBoxItem = binding.checkboxItem
        var importanceItem = binding.importanceItem
        var textItem = binding.textItem

        fun bind(item: TodoItem) = with(binding) {
            textItem.text = item.content
            if (item.deadline != 0L) deathlineItem.text =
                item.deadline?.let { Utils().convertLongDeathlineToString(it) }
            else deathlineItem.text = "Дедлайна нет, гуляем"
            item.flag?.let { checkBoxItem.isChecked = item.flag!! }

        }


    }
    class OnClickListener(val clickListener: (item: TodoItem) -> Unit) {
        fun onClick(item: TodoItem) = clickListener(item)
    }

    class OnLongClickListener(val longClickListener: (item: TodoItem, view:View) -> Unit) {
        fun onClick(item: TodoItem, itemView: View) = longClickListener(item,itemView)
    }
    class OnCheckBoxClickListener(val checkBoxClickListener: (item: TodoItem, flag: Boolean, itemView: View) -> Unit) {
        fun onClick(item: TodoItem, flag: Boolean, itemView: View) = checkBoxClickListener(item,flag,itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.todo_item, parent, false)
        return MainViewHolder(itemView)
    }


    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))

        onSetListenerButtonInfo(holder)


//       клик для редактирования
        holder.itemView.setOnClickListener {
            onClickListener.onClick(getItem(position))
        }

        // обработчик нажатия на чекбокс
        holder.checkBoxItem.setOnCheckedChangeListener{ buttonView, isChecked ->
            //onCheckBoxClickListener.onClick(getItem(position),isChecked,holder.itemView)
            Log.e("adapterListener","knok")
            if (getItem(position).flag == true) {
                holder.textItem.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else if (getItem(position).flag == false) {
                holder.textItem.paintFlags = Paint.ANTI_ALIAS_FLAG
            }
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
    private fun onSetListenerButtonInfo(holder: MainViewHolder) {
        holder.infoItem.setOnClickListener {
            if (holder.deathlineItem.visibility == View.GONE) holder.deathlineItem.visibility =
                View.VISIBLE
            else if (holder.deathlineItem.visibility == View.VISIBLE) holder.deathlineItem.visibility =
                View.GONE
        }
    }


}
