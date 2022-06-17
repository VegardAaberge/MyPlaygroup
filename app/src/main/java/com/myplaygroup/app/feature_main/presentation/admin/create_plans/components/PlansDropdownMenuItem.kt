package com.myplaygroup.app.feature_main.presentation.admin.create_plans.components

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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.myplaygroup.app.core.presentation.components.ReadonlyOutlinedTextField

@Composable
fun ColumnScope.PlansDropdownMenuItem(
    label: String,
    items: List<String>,
    selected: String,
    errorMessage: String?,
    selectedChanged: (String) -> Unit
) {
    var mTextFieldSize by remember { mutableStateOf(Size.Zero)}
    var expanded by remember { mutableStateOf(false) }
    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Box() {
        ReadonlyOutlinedTextField(
            label = label,
            fieldValue = selected,
            trailingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            },
            isError = errorMessage != null,
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
        ){
            expanded = !expanded
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            },
            modifier = Modifier
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

    if(errorMessage != null){
        Text(
            text = errorMessage,
            color = MaterialTheme.colors.error,
            modifier = Modifier.align(Alignment.Start)
        )
    }
}