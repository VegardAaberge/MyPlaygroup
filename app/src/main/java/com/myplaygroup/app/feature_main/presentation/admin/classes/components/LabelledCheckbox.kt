package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import java.time.DayOfWeek

@Composable
fun LabelledCheckbox(
    dayOfWeek: DayOfWeek,
    isChecked: Boolean,
    weekdayChanged: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                weekdayChanged(dayOfWeek)
            },
            enabled = true,
            colors = CheckboxDefaults.colors()
        )
        Text(text = dayOfWeek.name.take(3))
    }
}