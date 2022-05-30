package com.myplaygroup.app.feature_main.presentation.home.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.feature_main.domain.model.PlaygroupClass
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import io.ktor.util.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarScreen(classesFlow: MutableStateFlow<List<PlaygroupClass>>) {

    val classes by classesFlow.collectAsState()
    val calendarState = rememberSelectableCalendarState(
        initialSelectionMode = SelectionMode.Single,
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        SelectableCalendar(
            calendarState = calendarState,
            dayContent = { dayState ->
                CustomDay(
                    state = dayState,
                    playgroupClass = classes.firstOrNull { it.date == dayState.date },
                )
            },
            weekHeader = { WeekHeader(daysOfWeek = it) },
            monthHeader = { MonthHeader(monthState = it) },
        )
    }
}

@Composable
fun CustomDay(
    state: DayState<DynamicSelectionState>,
    playgroupClass: PlaygroupClass?,
    modifier: Modifier = Modifier,
) {
    val date = state.date
    val selectionState = state.selectionState

    val isSelected = selectionState.isDateSelected(date)

    val classColor = if(playgroupClass != null){
        if(state.date.dayOfYear < LocalDateTime.now().dayOfYear){
            Color.LightGray
        } else MaterialTheme.colors.primary
    }else Color.Unspecified

    val textColor =  if(playgroupClass != null){
        Color.White
    }
    else if(state.isCurrentDay){
        MaterialTheme.colors.primary
    }else if(state.date.dayOfYear < LocalDateTime.now().dayOfYear){
        Color.LightGray
    }
    else Color.Unspecified

    val textWeight = if(isSelected){
        FontWeight.Bold
    } else FontWeight.Normal

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .padding(7.dp)
            .background(classColor, CircleShape)
            .clip(CircleShape)
            .clickable {
                selectionState.onDateSelected(date)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                color = textColor,
                fontWeight = textWeight,
                text = date.dayOfMonth.toString()
            )
        }
    }
}

@Composable
private fun MonthHeader(monthState: MonthState) {
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

@Composable
private fun WeekHeader(daysOfWeek: List<DayOfWeek>) {
    Row {
        daysOfWeek.forEach { dayOfWeek ->
            Text(
                textAlign = TextAlign.Center,
                text = dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight()
            )
        }
    }
}