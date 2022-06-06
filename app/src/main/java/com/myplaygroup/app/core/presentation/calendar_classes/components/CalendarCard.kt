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
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R
import com.myplaygroup.app.feature_main.domain.model.DailyClass

@Composable
fun CalendarCard(
    dailyClass: DailyClass,
    cardSelected: (DailyClass) -> Unit
) {
    Card(
        backgroundColor = colorResource(id = R.color.keynote_orange_3),
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