package com.myplaygroup.app.feature_main.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.Message
import java.lang.Float.min
import java.time.LocalDateTime

@Composable
fun MessageBox(
    isOwner: Boolean,
    message: Message,
    resendMessage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val boxColor = if (isOwner) Color.Blue else Color.LightGray
    val textColor = if (isOwner) Color.White else Color.Unspecified

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
                isSending = message.isSending,
                resendMessage = resendMessage,
                modifier = Modifier.size(30.dp)
            )
        }

        Box(modifier = Modifier
            .weight(1f, false)
            .padding(horizontal = 6.dp)
            .background(
                color = boxColor,
                shape = RoundedCornerShape(10.dp)
            )
            .drawBehind {
                val startX = if (isOwner) size.width else 0f
                val starty = min(25.dp.toPx(), size.height/2)
                val triangleHeight = 4.dp.toPx()
                val triangleWidth = 5.dp.toPx() * (if (isOwner) 1 else -1)

                val trianglePath = Path().apply {
                    moveTo(startX, starty - triangleHeight)
                    lineTo(startX + triangleWidth, starty)
                    lineTo(startX, starty + triangleHeight)
                    close()
                }
                drawPath(
                    path = trianglePath,
                    color = boxColor
                )
            }
            .padding(10.dp)
        ){
            Text(
                color = textColor,
                text = message.message,
                overflow = TextOverflow.Visible,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        if(!isOwner){
            MessageActivityIndicator(
                isSynced = message.isSynced(),
                isSending = message.isSending,
                resendMessage = resendMessage,
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
                created = LocalDateTime.now(),
                createdBy = "vegard",
                receivers = emptyList()
            )
        )
    }
}