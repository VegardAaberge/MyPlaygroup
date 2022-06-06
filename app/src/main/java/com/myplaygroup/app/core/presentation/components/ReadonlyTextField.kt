package com.myplaygroup.app.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp

@Composable
fun ReadonlyTextField(
    label: String,
    fieldValue: String,
    modifier : Modifier = Modifier,
    action: () -> Unit,
) {
    Box() {
        TextField(
            value = fieldValue,
            onValueChange = {  },
            label = {
                Text(text = label)
            },
            modifier = modifier
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .alpha(0f)
                .clickable(onClick = {
                    action()
                }),
        )
    }
}