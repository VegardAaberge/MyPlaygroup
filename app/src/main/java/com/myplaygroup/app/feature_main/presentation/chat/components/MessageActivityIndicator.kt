package com.myplaygroup.app.feature_main.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.Message

@Composable
fun MessageActivityIndicator(
    isSynced: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if(!isSynced){
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
            modifier = Modifier.size(50.dp)
        )
    }
}