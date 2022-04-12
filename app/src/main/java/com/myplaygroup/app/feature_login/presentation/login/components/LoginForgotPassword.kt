package com.myplaygroup.app.feature_login.presentation.login.components

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R

@Composable
fun LoginForgotPassword(
    isBusy: Boolean,
    forgotPasswordEvent: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(50.dp)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = if (!isBusy) {
                    LocalIndication.current
                } else null,
                onClick = forgotPasswordEvent
            )
    ){
        Text(
            text = stringResource(id = R.string.forgot_passeord),
            color = Color.Blue,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}