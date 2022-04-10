package com.myplaygroup.app.feature_login.presentation.forgot_password.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.myplaygroup.app.R
import com.myplaygroup.app.feature_login.presentation.forgot_password.ForgotPasswordEvent

@Composable
fun SendActionIcon(
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = stringResource(id = R.string.send_email))
        },
        onClick = onClick,
        icon = {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Add note"
            )
        },
        shape = CircleShape,
        contentColor = Color.White,
        backgroundColor = MaterialTheme.colors.primary
    )
}