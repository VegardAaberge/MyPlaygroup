package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.feature_main.domain.enums.DailyClassType

@Composable
fun LabeledRadioGroup(
    options: List<String>,
    modifier: Modifier = Modifier,
    classType: DailyClassType,
    classChanged: (DailyClassType) -> Unit
){
    Column(modifier = modifier) {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f, false)
                    .clickable {
                        classChanged(DailyClassType.valueOf(option))
                    },
            ) {
                RadioButton(selected = classType.name == option, onClick = null)
                Text(text = option)
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}