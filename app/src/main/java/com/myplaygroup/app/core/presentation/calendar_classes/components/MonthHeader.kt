package com.myplaygroup.app.core.presentation.calendar_classes.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.sp
import io.github.boguszpawlowski.composecalendar.header.MonthState
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun MonthHeader(monthState: MonthState) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    )
    {
        IconButton(onClick = { monthState.currentMonth = monthState.currentMonth.minusMonths(1) }) {
            Image(
                imageVector = Icons.Default.KeyboardArrowLeft,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                contentDescription = "Next",
            )
        }
        val pattern = DateTimeFormatter.ofPattern("MMMM", Locale.getDefault())
        Text(
            text = monthState.currentMonth.format(pattern),
            fontSize = 26.sp
        )
        IconButton(onClick = { monthState.currentMonth = monthState.currentMonth.plusMonths(1) }) {
            Image(
                imageVector = Icons.Default.KeyboardArrowRight,
                colorFilter = ColorFilter.tint(MaterialTheme.colors.onSurface),
                contentDescription = "Next",
            )
        }
    }
}