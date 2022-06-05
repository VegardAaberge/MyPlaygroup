package com.myplaygroup.app.feature_admin.presentation.classes.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_admin.domain.model.DailyClass
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.day.DayState
import io.github.boguszpawlowski.composecalendar.header.MonthState
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@Composable
fun CalendarClassesScreen(
    classes: List<DailyClass>,
    calendarState: CalendarState<DynamicSelectionState>,
    selectedDay: LocalDate?
) {
    Column(
        verticalArrangement = Arrangement.Top,
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
                    dailyClass = classes.firstOrNull { it.date == dayState.date },
                )
            },
            weekHeader = { WeekHeader(daysOfWeek = it) },
            monthHeader = { MonthHeader(monthState = it) },
        )

        Spacer(modifier = Modifier.height(16.dp))

        val selectedClasses = classes.filter { it.date == selectedDay }
        if (selectedClasses.any()) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn {
                    items(selectedClasses) { item ->
                        CalendarCard(dailyClass = item)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDay(
    state: DayState<DynamicSelectionState>,
    dailyClass: DailyClass?,
    modifier: Modifier = Modifier,
) {
    val date = state.date
    val selectionState = state.selectionState

    val isSelected = selectionState.isDateSelected(date)

    val classColor = if(dailyClass != null){
        if(state.date.dayOfYear < LocalDateTime.now().dayOfYear){
            Color.LightGray
        } else MaterialTheme.colors.primary
    }else Color.Unspecified

    val textColor =  if(dailyClass != null){
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

@Composable
fun CalendarCard(dailyClass: DailyClass) {
    Card(
        backgroundColor = colorResource(id = R.color.keynote_orange_3),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .height(90.dp)
            .padding(bottom = 12.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clickable {  }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        ) {

            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = dailyClass.classType,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.h5
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Green)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if(dailyClass.cancelled) "Cancelled" else "On going",
                        color = Color.White,
                        style = MaterialTheme.typography.body1
                    )
                }

                Text(
                    text = "${dailyClass.startTime} - ${dailyClass.endTime}",
                    color = Color.White,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarClassesScreenPreview() {
    MyPlaygroupTheme {
        val selectedDay = LocalDate.now().minusDays(1)

        CalendarClassesScreen(
            classes = listOf(
                DailyClass(
                    id = "2",
                    cancelled = false,
                    classType = "Morning Group",
                    date = selectedDay,
                    endTime = "11:30",
                    startTime = "9:30"
                )
            ),
            calendarState = rememberSelectableCalendarState(
                initialSelectionMode = SelectionMode.Single,
            ),
            selectedDay = selectedDay
        )
    }
}