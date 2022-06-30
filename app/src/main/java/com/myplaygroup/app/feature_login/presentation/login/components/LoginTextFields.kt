package com.myplaygroup.app.feature_login.presentation.login.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R

@Composable
fun LoginTextFields(
    user: String,
    password: String,
    enabled: Boolean,
    onUserChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier,
)
{
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = user,
        placeholder = { Text(text = stringResource(id = R.string.login_user_placeholder)) },
        label = { Text(text = stringResource(id = R.string.login_user_label)) },
        singleLine = true,
        enabled = enabled,
        onValueChange = onUserChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier
    )
    Spacer(modifier = Modifier.height(10.dp))
    OutlinedTextField(
        value = password,
        placeholder = { Text(text = stringResource(id = R.string.login_create_profile_password_placeholder)) },
        label = { Text(text = stringResource(id = R.string.login_create_profile_password_label)) },
        enabled = enabled,
        singleLine = true,
        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        onValueChange = onPasswordChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        modifier = modifier,
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