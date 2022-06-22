package com.myplaygroup.app.feature_main.presentation.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.core.util.TextUtils
import java.time.LocalDateTime

@Composable
fun MessageDateItem(
    created: LocalDateTime
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        val date by remember {
            mutableStateOf(
                TextUtils.displayMessageDate(created)
            )
        }

        Text(
            text = date,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}