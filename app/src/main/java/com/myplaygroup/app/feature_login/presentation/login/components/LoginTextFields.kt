package com.myplaygroup.app.feature_login.presentation.login.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
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
    enabled: Boolean,
    onUserChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onFocusChange: (FocusState) -> Unit,
    modifier: Modifier = Modifier,
)
{
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = user,
        placeholder = { Text(text = stringResource(id = R.string.user_placeholder)) },
        label = { Text(text = stringResource(id = R.string.user_label)) },
        singleLine = true,
        enabled = enabled,
        onValueChange = onUserChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier
            .onFocusChanged { onFocusChange(it) }
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = password,
        placeholder = { Text(text = stringResource(id = R.string.password_placeholder)) },
        label = { Text(text = stringResource(id = R.string.password_label)) },
        enabled = enabled,
        singleLine = true,
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = onPasswordChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier
            .onFocusChanged { onFocusChange(it) },
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