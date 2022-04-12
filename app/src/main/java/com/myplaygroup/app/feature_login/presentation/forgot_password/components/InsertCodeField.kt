package com.myplaygroup.app.feature_login.presentation.forgot_password.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R

@Composable
fun InsertCodeField(
    code: String,
    countDown: Int,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {

        TextField(
            value = code,
            placeholder = { Text(text = stringResource(id = R.string.enter_code_placeholder)) },
            label = { Text(text = stringResource(id = R.string.enter_code_label)) },
            singleLine = true,
            enabled = countDown >= 0,
            onValueChange = onValueChange,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .weight(2f)
        )

        Box(
            Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if(countDown >= 0){
                Text(
                    text = countDown.toString(),
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }
}