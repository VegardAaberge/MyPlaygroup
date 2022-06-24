package com.myplaygroup.app.feature_main.presentation.chat.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.presentation.chat.ChatState
import java.time.LocalDateTime

@Composable
fun MessageItem(
    isOwner: Boolean,
    message: Message,
    lastReadMessage: List<ChatState.LastRead>,
    resendMessage: () -> Unit = {},
    iconUri: Uri? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        MessageDateItem(
            created = message.created
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
        ) {
            if(!isOwner){
                RoundIcon(
                    iconUri = iconUri,
                    size = 40.dp,
                    modifier = Modifier.size(50.dp)
                )
            }

            MessageBox(
                isOwner = isOwner,
                message = message,
                resendMessage = resendMessage,
                modifier = Modifier.weight(1f)
            )

            if(isOwner){
                RoundIcon(
                    iconUri = iconUri,
                    size = 40.dp,
                    modifier = Modifier.size(50.dp)
                )
            }
        }

        val lastReads = lastReadMessage.filter { it.messageClientId == message.clientId }
        if(lastReads.isNotEmpty() && isOwner){
            Row(
                modifier = Modifier
                    .padding(horizontal = 65.dp)
                    .align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                lastReads.forEach { lastRead ->
                    Text(
                        text = lastRead.profileName,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }
        }else{
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyMessage() {
    MyPlaygroupTheme {
        MessageItem(
            isOwner = true,
            message = Message(
                clientId = "123",
                message = "This is my message",
                profileName = "Vegard Aaberge",
                created = LocalDateTime.now(),
                createdBy = "vegard",
                receivers = emptyList()
            ),
            lastReadMessage = listOf(
                ChatState.LastRead(
                    messageClientId = "123",
                    profileName = "Vegard"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LongMessage() {
    MyPlaygroupTheme {
        MessageItem(
            isOwner = true,
            message = Message(
                clientId = "123",
                message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                profileName = "Vegard Aaberge",
                created = LocalDateTime.now(),
                createdBy = "vegard",
                receivers = emptyList()
            ),
            lastReadMessage = listOf(
                ChatState.LastRead(
                    messageClientId = "123",
                    profileName = "Vegard"
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OtherMessage() {
    MyPlaygroupTheme {
        MessageItem(
            isOwner = false,
            message = Message(
                clientId = "123",
                message = "This is my message",
                profileName = "Vegard Aaberge",
                created = LocalDateTime.now(),
                createdBy = "vegard",
                receivers = emptyList()
            ),
            lastReadMessage = listOf(
                ChatState.LastRead(
                    messageClientId = "123",
                    profileName = "Vegard"
                )
            )
        )
    }
}