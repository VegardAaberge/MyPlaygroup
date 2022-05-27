package com.myplaygroup.app.feature_main.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.Message

@Composable
fun MessageBox(
    isOwner: Boolean,
    message: Message,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if(isOwner) {
            Arrangement.End
        } else Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if(isOwner){
            Spacer(modifier = Modifier.width(20.dp))
            MessageActivityIndicator(
                isSynced = message.isSynced(),
                modifier = Modifier.size(30.dp)
            )
        }

        Box(modifier = Modifier
            .weight(1f, false)
            .clip(RoundedCornerShape(10.dp))
            .background(
                color = if (isOwner) Color.Blue else Color.LightGray,
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp)
        ){
            Text(
                color = if(isOwner) Color.White else Color.Unspecified,
                text = message.message,
                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        if(!isOwner){
            MessageActivityIndicator(
                isSynced = message.isSynced(),
                modifier = Modifier.width(30.dp)
            )
            Spacer(modifier = Modifier.width(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MessageBoxPreview() {
    MyPlaygroupTheme {
        MessageBox(
            isOwner = true,
            message = Message(
                message = "This is my message",
                profileName = "Vegard Aaberge",
                created = 3424324,
                createdBy = "vegard"
            )
        )
    }
}