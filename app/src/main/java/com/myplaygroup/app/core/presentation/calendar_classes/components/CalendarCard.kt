package com.myplaygroup.app.core.presentation.calendar_classes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.calendar_classes.CalendarClassesScreen
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.DailyClass
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import io.github.boguszpawlowski.composecalendar.selection.SelectionMode
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun CalendarCard(
    dailyClass: DailyClass,
    cardSelected: (DailyClass) -> Unit
) {
    val classEnded = dailyClass.date < LocalDate.now()
    val statusText = when {
        dailyClass.cancelled -> "Scheduled"
        classEnded -> "Completed"
        else -> "On going"
    }

    val statusColor = when {
        dailyClass.cancelled -> MaterialTheme.colors.error
        classEnded -> MaterialTheme.colors.secondaryVariant
        else -> Color.Green
    }

    val backgroundColor = when {
        dailyClass.cancelled || classEnded -> Color.LightGray
        else -> colorResource(id = R.color.keynote_orange_3)
    }


    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
            .height(90.dp)
            .padding(bottom = 12.dp)
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .clickable {
                cardSelected(dailyClass)
            }
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
                        .background(statusColor)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = statusText,
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
fun CalendarCardPreview() {
    MyPlaygroupTheme {
        val selectedDay = LocalDate.now().plusDays(1)

        CalendarCard(
            dailyClass = DailyClass(
                    classType = "Morning Group",
                    date = selectedDay,
                    endTime = LocalTime.of(11, 30),
                    startTime = LocalTime.of(9, 30),
                    cancelled = false,
                    dayOfWeek = DayOfWeek.MONDAY,
                )

        ){

        }
    }
}