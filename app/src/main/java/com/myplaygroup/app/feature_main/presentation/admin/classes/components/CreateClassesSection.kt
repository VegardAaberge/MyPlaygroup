package com.myplaygroup.app.feature_main.presentation.admin.classes.components

import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme

@Composable
fun CreateClassesSection(
    generate: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .padding(end = 16.dp)
            .fillMaxWidth(),
    ) {
        LabeledRadioGroup(
            options = listOf("Morning", "Evening"),
            modifier = Modifier.padding(start = 14.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        LabeledClassTime(
            title = "Start Time",
            initalHour = 9,
            initialMinute = 30,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LabeledClassTime(
            title = "End Time",
            initalHour = 11,
            initialMinute = 30,
            modifier = Modifier.padding(start = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(start = 3.dp, end = 16.dp)
                .fillMaxWidth()
        ) {
            Column {
                LabelledCheckbox("Mon", true)
                LabelledCheckbox("Thu", true)
            }
            Column {
                LabelledCheckbox("Tue", true)
                LabelledCheckbox("Fri", true)
            }
            Column {
                LabelledCheckbox("Wed", true)
                LabelledCheckbox("Sat", false)
            }
        }

        Button(
            onClick = {
                generate()
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "Generate")
        }
    }
}

@Composable
fun LabelledCheckbox(
    title: String,
    defaultChecked: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        val isChecked = remember { mutableStateOf(defaultChecked) }

        Checkbox(
            checked = isChecked.value,
            onCheckedChange = { isChecked.value = it },
            enabled = true,
            colors = CheckboxDefaults.colors()
        )
        Text(text = title)
    }
}

@Composable
fun LabeledRadioGroup(
    options: List<String>,
    modifier: Modifier = Modifier
){
    var selected by remember { mutableStateOf(options.first()) }
    Row(modifier = modifier) {
        options.forEach { option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f, false)
                    .clickable {
                        selected = option
                    },
            ) {
                RadioButton(selected = selected == option, onClick = null)
                Text(text = option)
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

@Composable
fun LabeledClassTime(
    title: String,
    initalHour: Int,
    initialMinute: Int,
    modifier: Modifier = Modifier
){
    val context = LocalContext.current

    val time = remember { mutableStateOf("$initalHour:$initialMinute") }
    val timePickerDialog = TimePickerDialog(
        context,
        {_, hour : Int, minute: Int ->
            time.value = "$hour:$minute"
        }, initalHour, initialMinute, false
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .width(130.dp)
            .clickable {
                timePickerDialog.show()
            },
    ) {
        Text(text = "$title:")
        Text(text = time.value)
    }
}

@Preview(showBackground = true)
@Composable
fun CreateClassesSectionPreview() {
    MyPlaygroupTheme {
        CreateClassesSection {

        }
    }
}
