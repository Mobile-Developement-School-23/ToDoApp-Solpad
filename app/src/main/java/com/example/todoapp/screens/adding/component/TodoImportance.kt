package com.example.todoapp.screens.adding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.model.Importance
import com.example.todoapp.screens.ui.TodoAppCustomTheme
import com.example.todoapp.screens.ui.Red
import com.example.todoapp.util.convertImportanceToString

@Composable
fun TodoImportante(importance: MutableState<Importance>) {
    var menuExpanded = remember { mutableStateOf(false) }
    val isImportant = remember(importance) { importance.value == Importance.IMPORTANT}

    Column(modifier = Modifier
        .padding(top = 24.dp, bottom = 12.dp)
        .padding(horizontal = 12.dp)
        .clip(RoundedCornerShape(6.dp))
        .clickable { menuExpanded.value = true }
        .padding(4.dp)
    ) {
        Text(
            text = stringResource(id = R.string.important),
            color = TodoAppCustomTheme.colors.labelPrimary
        )
        Text(text = convertImportanceToString(importance.value),
            modifier = Modifier.padding(top = 4.dp),
            color = if(isImportant) Red else TodoAppCustomTheme.colors.labelTertiary
        )

        TodoImportanceDropdownMenu(
            menuExpanded.value,
            hideMenu = {menuExpanded.value = false},
            importance)
    }
}


@Composable
fun TodoImportanceDropdownMenu(
    menuExpanded: Boolean,
    hideMenu: () -> Unit,
    importance: MutableState<Importance>){
    DropdownMenu(
        expanded = menuExpanded,
        onDismissRequest = { hideMenu() },
        modifier = Modifier
            .background(TodoAppCustomTheme.colors.backElevated),
        offset = DpOffset(x = 52.dp, y = (-18).dp)
    ) {

        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.importance_basic)) },
            onClick = {
                importance.value = Importance.BASIC
                hideMenu()
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.importance_low)) },
            onClick = {
                importance.value = Importance.LOW
                hideMenu()
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(id = R.string.importance_important)) },
            onClick = {
                importance.value = Importance.IMPORTANT
                hideMenu()
            }
        )
    }

}

@Preview
@Composable
private fun TodoImportancePreview(){
    var importance = remember { mutableStateOf(Importance.IMPORTANT) }
    TodoImportante(importance)
}