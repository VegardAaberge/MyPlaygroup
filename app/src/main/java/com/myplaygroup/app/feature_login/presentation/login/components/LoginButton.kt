package com.myplaygroup.app.feature_login.presentation.login.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R

@Composable
fun LoginButton(
    loginEvent: () -> Unit,
    enabled: Boolean,
    btnHeight: Dp = 50.dp,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = loginEvent,
        enabled = enabled,
        modifier = modifier
            .height(btnHeight)
    )
    {
        Text(text = stringResource(id = R.string.login_button))
    }
}