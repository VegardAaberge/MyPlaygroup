package com.myplaygroup.app.feature_main.presentation.admin.create_plans.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun UserCheckbox(
    user: String,
    isChecked: Boolean,
    userChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                focusManager.clearFocus()
                userChanged(it)
            },
            enabled = true,
            colors = CheckboxDefaults.colors()
        )
        Text(text = user)
    }
}