package com.example.todoapp.screens.adapter


import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.screens.main.MainFragmentViewModel
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import okhttp3.internal.notify


class MainItemTouchHelper(
    val adapter: MainAdapter,
    val mViewModel: MainFragmentViewModel,
    val view:View
    ):ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val item = adapter.currentList[position]
        when(direction){
            ItemTouchHelper.LEFT ->{
                mViewModel.deleteTodoItem(item,item.id)
                adapter.notifyDataSetChanged()
                var recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview_do)
                Snackbar.make(recyclerView,"Отменить удаление?",Snackbar.LENGTH_LONG)
                    .setAction("Да",View.OnClickListener {
                        mViewModel.addTodoItem(item)
                        adapter.notifyItemInserted(position)
                        adapter.notifyDataSetChanged()
                    }).show()
            }
            ItemTouchHelper.RIGHT ->{

            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if(dX<0) {
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addBackgroundColor(ContextCompat.getColor(view.context, R.color.red))
                .addActionIcon(com.example.todoapp.R.drawable.ic_delete)
                .create()
                .decorate()
        }else {
            RecyclerViewSwipeDecorator.Builder(
                c,
                recyclerView,
                viewHolder,
                dX,
                dY,
                actionState,
                isCurrentlyActive
            )
                .addBackgroundColor(ContextCompat.getColor(view.context, R.color.green))
                .addActionIcon(com.example.todoapp.R.drawable.ic_check)
                .create()
                .decorate()
        }
        super.onChildDraw( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive
        )
    }

}