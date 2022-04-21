package com.myplaygroup.app.feature_login.presentation.forgot_password.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.myplaygroup.app.R

@Composable
fun SendActionIcon(
    shouldCheckCode: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val sendEmailText = if(shouldCheckCode){
        stringResource(id = R.string.check_code)
    }else{
        stringResource(id = R.string.send_email)
    }

    val sendEmailIcon = if(shouldCheckCode)
        Icons.Default.ArrowForward else Icons.Default.Send

    val backgroundColor = if(enabled)
        MaterialTheme.colors.primary else Color.LightGray

    ExtendedFloatingActionButton(
        text = {
            Text(text = sendEmailText)
        },
        onClick = if(enabled) {
            onClick
        }else{
            {}
        },
        icon = {
            Icon(
                imageVector = sendEmailIcon,
                contentDescription = sendEmailText
            )
        },
        shape = CircleShape,
        contentColor = Color.White,
        backgroundColor = backgroundColor
    )
}