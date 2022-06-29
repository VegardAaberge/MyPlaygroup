package com.myplaygroup.app.feature_profile.presentation.edit_profile.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme

@Composable
fun ColumnScope.ProfileField(
    value: String,
    enabled: Boolean,
    placeholder: String,
    label: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    errorMessage: String? = null,
)
{
    OutlinedTextField(
        value = value,
        placeholder = { Text(text = placeholder) },
        label = { Text(text = label) },
        enabled = enabled,
        singleLine = true,
        onValueChange = onTextChange,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White
        ),
        visualTransformation = if(isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        modifier = modifier,
        isError = errorMessage != null
    )


    if(errorMessage != null){
        Text(
            text = errorMessage,
            color = MaterialTheme.colors.error,
            modifier = modifier.align(Alignment.End)
        )
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Preview
@Composable
fun EditProfileFieldPreview() {
    MyPlaygroupTheme {
        Column {
            ProfileField(
                value = "",
                enabled = true,
                placeholder = "Key",
                label = "Label",
                onTextChange = {

                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}