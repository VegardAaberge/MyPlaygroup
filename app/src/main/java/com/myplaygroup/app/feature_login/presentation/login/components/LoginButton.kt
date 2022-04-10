package com.myplaygroup.app.feature_login.presentation.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
    btnWidth: Dp = 400.dp,
    btnHorizontalPadding: Dp = 30.dp,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = loginEvent,
        enabled = enabled,
        modifier = modifier
            .width(btnWidth)
            .height(btnHeight)
            .padding(horizontal = btnHorizontalPadding)
    )
    {
        Text(text = stringResource(id = R.string.button_login))
    }
}