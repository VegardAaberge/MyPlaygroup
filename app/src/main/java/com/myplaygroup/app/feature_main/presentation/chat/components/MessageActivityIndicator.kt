package com.myplaygroup.app.feature_main.presentation.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme

@Composable
fun MessageActivityIndicator(
    hasError: Boolean,
    isSynced: Boolean,
    resendMessage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clickable {
                if(hasError){
                    resendMessage()
                }
            },
    ) {
        if (hasError) {
            Icon(
                imageVector = Icons.Filled.Warning,
                contentDescription = null,
                tint = Color.Red
            )
        } else if (!isSynced) {
            CircularProgressIndicator(
                strokeWidth = 1.5.dp,
                modifier = Modifier
                    .size(15.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageActivityIndicatorPreview2() {
    MyPlaygroupTheme {
        MessageActivityIndicator(
            isSynced = false,
            hasError = true,
            modifier = Modifier.size(50.dp)
        )
    }
}