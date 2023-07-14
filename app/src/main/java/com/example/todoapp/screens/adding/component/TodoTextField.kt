package com.example.todoapp.screens.adding.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.screens.ui.TodoAppCustomTheme


@Composable
fun TodoEditText(valueState: MutableState<String>){
    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .padding(horizontal = 16.dp),
        value = valueState.value,
        onValueChange = {newText -> valueState.value = newText},
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = TodoAppCustomTheme.colors.labelPrimary
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        minLines = 3,
        cursorBrush = SolidColor(TodoAppCustomTheme.colors.backSecondary)
    ) { innerTextField ->

        Card(
            colors = CardDefaults.cardColors(
                containerColor = TodoAppCustomTheme.colors.backSecondary,
                contentColor = TodoAppCustomTheme.colors.labelTertiary
            )
        ) {

            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                if (valueState.value.isEmpty())
                    Text(
                        text = stringResource(id = R.string.hint_adding),
                        color = TodoAppCustomTheme.colors.labelPrimary
                    )
                innerTextField.invoke()
            }
        }
    }
}

@Preview
@Composable
fun textFieldPrewiew() {
    var rawValue = remember { mutableStateOf("") }

    TodoEditText(rawValue)
}