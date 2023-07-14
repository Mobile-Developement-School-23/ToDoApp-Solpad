package com.example.todoapp.screens.adding.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.todoapp.screens.ui.TodoAppCustomTheme

@Composable
fun TodoDiviver(
    padding: PaddingValues
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding),
        color = TodoAppCustomTheme.colors.supportSeparator
    )
}