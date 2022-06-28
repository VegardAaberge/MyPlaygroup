package com.myplaygroup.app.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize

@Composable
fun ColumnScope.DropdownOutlinedTextField(
    label: String,
    items: List<String>,
    selected: String,
    errorMessage: String?,
    enabled: Boolean = true,
    isOutlined: Boolean = true,
    selectedChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorModifier: Modifier = Modifier
) {
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box(
        modifier = modifier
    ) {
        if(isOutlined){
            ReadonlyOutlinedTextField(
                label = label,
                fieldValue = selected,
                enabled = enabled,
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                },
                isError = errorMessage != null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    },
            ){
                expanded = !expanded
            }
        }else{
            ReadonlyTextField(
                label = label,
                fieldValue = selected,
                enabled = enabled,
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.LightGray,
                ),
                isError = errorMessage != null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    },
            ){
                expanded = !expanded
            }
        }


        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
                .align(Alignment.Center)
                .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            items.forEach { user ->
                DropdownMenuItem(onClick = {
                    selectedChanged(user)
                    expanded = false
                }) {
                    Text(text = user)
                }
            }
        }
    }

    Box(
        modifier = errorModifier.fillMaxWidth(),
    ) {
        if(errorMessage != null){
            Text(
                text = errorMessage,
                color = MaterialTheme.colors.error,
            )
        }
    }
}