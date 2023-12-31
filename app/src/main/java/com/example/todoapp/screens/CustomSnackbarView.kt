package com.example.todoapp.screens


import android.content.Context
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.todoapp.R
import com.example.todoapp.util.convertDpToPixels
import com.example.todoapp.util.getColorFromAttr
import com.google.android.material.snackbar.ContentViewCallback

class CustomSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), ContentViewCallback {

    private val animatedProgressBar: ImageView

    init {
        View.inflate(context, R.layout.view_snackbar, this)
        clipToPadding = false
        this.animatedProgressBar = findViewById(R.id.animatedProgressBar)
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        val animatedNumbers = animatedProgressBar.drawable as AnimatedVectorDrawable
        val animatedProgressBar = animatedProgressBar.background as CustomProgressBar
        animatedNumbers.reset()
        animatedNumbers.start()
        animatedProgressBar.setColor(context.getColorFromAttr(com.google.android.material.R.attr.colorOnSecondary))
        animatedProgressBar.setSize(
            context.convertDpToPixels(60f),
            context.convertDpToPixels(10f)
        )
        animatedProgressBar.start()
    }

    override fun animateContentOut(delay: Int, duration: Int) {
        val animatedProgressBar = animatedProgressBar.background as CustomProgressBar
        animatedProgressBar.reset()
    }
}