package com.myplaygroup.app.feature_main.presentation.user.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun MessageDateItem(
    created: Long
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        var date by remember {
            val date = LocalDateTime.ofEpochSecond(created, 0, ZoneOffset.UTC)

            val pattern = if(date.plusDays(6) > LocalDateTime.now()){
                "EEE hh:mm a"
            } else "d/M/yy hh:mm a"

            val text = date.format(DateTimeFormatter.ofPattern(pattern)).toString()
            mutableStateOf(text)
        }

        Text(
            text = date,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}