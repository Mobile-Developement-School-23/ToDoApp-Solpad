package com.example.todoapp.screens.adapter


import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.screens.CustomSnackbar
import com.example.todoapp.screens.CustomSnackbar.Companion.setAction
import com.example.todoapp.screens.main.MainFragmentViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import okhttp3.internal.notify


class MainItemTouchHelper @AssistedInject constructor(
    @Assisted("MainViewModel") private val mViewModel: MainFragmentViewModel,
    @Assisted("view") private val view: View,
    @Assisted("Adapter") private val adapter: MainAdapter,
    ):ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("MainViewModel") mViewModel: MainFragmentViewModel,
            @Assisted("view") view: View,
            @Assisted("Adapter") adapter: MainAdapter,
        ): MainItemTouchHelper
    }
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

                val viewForSnackbar = view.findViewById<View>(R.id.coordinator)
                CustomSnackbar.make(viewForSnackbar).apply {
                    animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE
                    duration = 4700
                    setAction {
                        mViewModel.addTodoItem(item)
                        adapter.notifyItemInserted(position)
                        adapter.notifyDataSetChanged()
                    }
                    show()
                }
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