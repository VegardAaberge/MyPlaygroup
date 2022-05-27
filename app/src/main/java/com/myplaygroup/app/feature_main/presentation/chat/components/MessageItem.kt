package com.myplaygroup.app.feature_main.presentation.chat.components

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.model.Message

@Composable
fun MessageItem(
    isOwner: Boolean,
    message: Message,
    iconUri: Uri? = null
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
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
    }
}

@Preview(showBackground = true)
@Composable
fun MyMessage() {
    MyPlaygroupTheme {
        MessageItem(
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

@Preview(showBackground = true)
@Composable
fun LongMessage() {
    MyPlaygroupTheme {
        MessageItem(
            isOwner = true,
            message = Message(
                message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                profileName = "Vegard Aaberge",
                created = 3424324,
                createdBy = "vegard"
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
                message = "This is my message",
                profileName = "Vegard Aaberge",
                created = 3424324,
                createdBy = "vegard"
            )
        )
    }
}