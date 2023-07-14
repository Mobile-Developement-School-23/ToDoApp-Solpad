package com.example.todoapp.screens.adding.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.todoapp.R
import com.example.todoapp.screens.ui.Blue
import com.example.todoapp.screens.ui.BlueTranslucent
import com.example.todoapp.screens.ui.TodoAppCustomTheme
import com.example.todoapp.util.convertIntTimeToString
import com.example.todoapp.util.convertLongDeathlineToString
import com.example.todoapp.util.convertMinutesToLong
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDataPicker(
    date: MutableState<Long>,
    dateString: MutableState<String>,
    isDateVisible: MutableState<Boolean>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp)
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        var openData by remember { mutableStateOf(false) }
        var clockState: UseCaseState = rememberUseCaseState()
        DatePicker(
            date = date,
            enable = openData,
            closeFun = { openData = false },
        )

        ClockDialog(
            state = clockState,
            selection = ClockSelection.HoursMinutes { hour, min ->
                dateString.value = convertIntTimeToString(hour, min)
                date.value = convertMinutesToLong(date.value, hour, min)
            },
            config = ClockConfig(
                defaultTime = LocalTime.now(),
                is24HourFormat = true
            )
        )

        Column {
            Text(
                text = stringResource(id = R.string.deathline),
                modifier = Modifier.padding(start = 4.dp),
                color = TodoAppCustomTheme.colors.labelPrimary
            )

            AnimatedVisibility(visible = isDateVisible.value) {
                Column() {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { openData = true }
                            .padding(6.dp)
                    ) {
                        Text(text = convertLongDeathlineToString(date.value), color = Blue)
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { clockState.show() }
                            .padding(6.dp)
                    ) {
                        Text(text = dateString.value, color = Blue)
                    }
                }
            }
        }

        Switch(
            checked = isDateVisible.value,
            onCheckedChange = { isDateVisible.value = !isDateVisible.value },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Blue,
                checkedTrackColor = BlueTranslucent,
                uncheckedThumbColor = TodoAppCustomTheme.colors.backElevated,
                uncheckedTrackColor = TodoAppCustomTheme.colors.supportOverlay,
                uncheckedBorderColor = TodoAppCustomTheme.colors.supportOverlay,
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePicker(
    date: MutableState<Long>,
    enable: Boolean,
    closeFun: () -> Unit,
) {
    if (enable) {
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date.value)
        val confirmEnable by remember(datePickerState.selectedDateMillis) {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }

        DatePickerDialog(
            onDismissRequest = closeFun,
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            date.value = it
                        }
                        closeFun()
                    },
                    enabled = confirmEnable
                ) {
                    Text(stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton( onClick = closeFun ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    }
}

