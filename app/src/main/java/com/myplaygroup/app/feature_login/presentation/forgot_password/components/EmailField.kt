package com.myplaygroup.app.feature_login.presentation.forgot_password.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R

@Composable
fun EmailField(
    textValue: String,
    isEnabled: Boolean,
    canInputCode: Boolean,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = textValue,
            placeholder = { Text(text = stringResource(id = R.string.forgot_password_placeholder)) },
            label = { Text(text = stringResource(id = R.string.forgot_password_label)) },
            singleLine = true,
            enabled = isEnabled,
            onValueChange = onTextChanged,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White
            ),
            modifier = modifier
        )
        
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(400.dp)
                .padding(horizontal = 30.dp)
        ) {

            TextField(
                value = textValue,
                placeholder = { Text(text = stringResource(id = R.string.enter_code_placeholder)) },
                label = { Text(text = stringResource(id = R.string.enter_code_label)) },
                singleLine = true,
                enabled = canInputCode,
                onValueChange = onTextChanged,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .weight(2f)
            )

            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = "120",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }
    }


}