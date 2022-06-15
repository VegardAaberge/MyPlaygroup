package com.myplaygroup.app.feature_main.presentation.admin.chat_groups.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.myplaygroup.app.feature_main.domain.model.ChatGroup
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ChatGroupItem(item: ChatGroup) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(50.dp)
    ) {
        Image(
            painter = rememberImagePainter(data = item.icon),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .aspectRatio(1f, matchHeightConstraintsFirst = true)
                .border(
                    1.dp,
                    Color.LightGray,
                    CircleShape
                )
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            FirstRow(
                name = item.username,
                updateTime = item.updateTime,
                modifier = Modifier.weight(1f)
            )

            SecondRow(
                lastMessage = item.lastMessage,
                notification = item.notification,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun FirstRow(
    name: String,
    updateTime: LocalDateTime?,
    modifier: Modifier = Modifier
){

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            updateTime?.let {
                Text(
                    text = it.format(DateTimeFormatter.ofPattern("h:ss")),
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun SecondRow(
    lastMessage: String?,
    notification: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = lastMessage ?: "(No message)",
            overflow = TextOverflow.Ellipsis,
            color = Color.Gray,
            softWrap = false,
            modifier = Modifier.weight(1f)
        )

        Box(
            modifier = Modifier.width(50.dp),
            contentAlignment = Alignment.Center
        ) {
            if(notification > 0){
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary, CircleShape)
                        .clip(CircleShape)
                        .height(20.dp)
                        .requiredWidthIn(min = 20.dp)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        color = Color.White,
                        text = notification.toString(),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(y = -1.dp)
                    )
                }
            }
        }
    }
}