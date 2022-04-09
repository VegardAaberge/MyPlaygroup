package com.myplaygroup.app.feature_login.presentation.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R
import com.myplaygroup.app.feature_login.presentation.login.LoginEvent

@Composable
fun LoginTextFields(
    user: String,
    password: String,
    onUserChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    textFieldWidth: Dp = 400.dp,
    textFieldHorizontalPadding: Dp = 30.dp,
    modifier: Modifier = Modifier,
)
{
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = user,
        placeholder = { Text(text = stringResource(id = R.string.user_placeholder)) },
        label = { Text(text = stringResource(id = R.string.user_label)) },
        singleLine = true,
        onValueChange = onUserChange,
        modifier = modifier
            .width(textFieldWidth)
            .padding(horizontal = textFieldHorizontalPadding)
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = password,
        placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
        label = { Text(text = stringResource(id = R.string.password_label)) },
        singleLine = true,
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = onPasswordChange,
        modifier = modifier
            .width(textFieldWidth)
            .padding(horizontal = textFieldHorizontalPadding),
        trailingIcon = {
            val image = if(passwordVisible)
                painterResource(id = R.drawable.ic_visibility)
            else
                painterResource(id = R.drawable.ic_visibility_off)

            val description = if (passwordVisible)
                "Hide password"
            else
                "Show password"

            IconButton(onClick = {passwordVisible = !passwordVisible}){
                Icon(painter = image, description)
            }
        }
    )
}