package com.example.todoapp.screens.adding

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Importance
import com.example.todoapp.screens.adding.component.TodoAppBar
import com.example.todoapp.screens.adding.component.TodoDataPicker
import com.example.todoapp.screens.adding.component.TodoDeleteButton
import com.example.todoapp.screens.adding.component.TodoDiviver
import com.example.todoapp.screens.adding.component.TodoEditText
import com.example.todoapp.screens.adding.component.TodoImportante
import com.example.todoapp.screens.ui.TodoAppCustomTheme
import com.example.todoapp.screens.ui.TodoAppTheme
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState

@Preview
@Composable
private fun AppThemeCompose(){
    TodoAppTheme {
        val editTextValue = remember { mutableStateOf("")}
        val importantValue = remember {mutableStateOf(Importance.BASIC)}
        val deathlineValue = remember {mutableStateOf(System.currentTimeMillis())}
        val deathlineString = remember { mutableStateOf("00:00")}
        val dataStateVisible = remember {mutableStateOf(false) }
        val enableClick = remember { mutableStateOf(false)}

        var listState = rememberLazyListState()
        val topBarElevation by animateDpAsState(
            if(listState.canScrollBackward) 8.dp else 0.dp,
            label = "top bar elevation"
        )
        val clockState = rememberUseCaseState()
        Scaffold(
            topBar = {
                TodoAppBar(true, topBarElevation,
                    { },
                    {  }
                )
            },
            containerColor = TodoAppCustomTheme.colors.backPrimary
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                state = listState
            ) {
                item {

                    TodoEditText(editTextValue)
                    TodoImportante(importantValue)
                    TodoDiviver(PaddingValues(horizontal = 16.dp))
                    TodoDataPicker(deathlineValue,deathlineString, dataStateVisible)
                    TodoDiviver(PaddingValues(horizontal = 16.dp))
                    TodoDeleteButton(enableClick.value) {  }
                }
                item {
                    Spacer(modifier = Modifier.height(96.dp))
                }
            }
        }
    }
}