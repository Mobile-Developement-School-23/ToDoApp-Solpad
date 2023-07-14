package com.example.todoapp.screens.adding.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.todoapp.R
import com.example.todoapp.screens.ui.Blue
import com.example.todoapp.screens.ui.TodoAppCustomTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoAppBar(
    enabled: Boolean,
    elevation: Dp,
    onClick:   () -> Unit,
    onClickClose:   () -> Unit
) {
    TopAppBar(
        modifier = Modifier.shadow(elevation),
        navigationIcon = {
            IconButton(
                onClick = { onClickClose() }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                )
            }
        },
        title = { /* There is no title, but i can't remove it :) */ },
        actions = {
            val saveColor by animateColorAsState(
                targetValue = if (enabled) Blue else TodoAppCustomTheme.colors.labelDisable)

            TextButton(
                onClick = { onClick() },
                enabled = enabled,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = saveColor,
                    disabledContentColor = saveColor
                )
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.titleMedium,
                    color = TodoAppCustomTheme.colors.labelPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = TodoAppCustomTheme.colors.backPrimary,
            navigationIconContentColor = TodoAppCustomTheme.colors.labelPrimary
        )
    )
}