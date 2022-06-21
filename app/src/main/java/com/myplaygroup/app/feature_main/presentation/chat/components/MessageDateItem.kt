package com.myplaygroup.app.feature_main.presentation.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun MessageDateItem(
    created: LocalDateTime
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        var date by remember {
            val pattern = if(created.plusDays(6) > LocalDateTime.now()){
                "EEE hh:mm a"
            } else "d/M/yy hh:mm a"

            val text = created.format(DateTimeFormatter.ofPattern(pattern)).toString()
            mutableStateOf(text)
        }

        Text(
            text = date,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}