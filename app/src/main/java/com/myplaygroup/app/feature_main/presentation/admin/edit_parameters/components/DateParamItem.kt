package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.myplaygroup.app.core.presentation.components.ReadonlyTextField
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DateParamItem(
    item: ParameterItem,
    timeChanged: (Any, String) -> Unit
) {
    val context = LocalContext.current
    val itemDate = item.value as LocalDate

    val time = itemDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            timeChanged(LocalDate.of(year, month, dayOfMonth), item.key)
        }, itemDate.year, itemDate.monthValue, itemDate.dayOfMonth
    )

    ReadonlyTextField(
        label = item.getTitle(),
        fieldValue = time,
        isError = item.error != null,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.Transparent,
            unfocusedIndicatorColor = Color.LightGray,
        ),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        datePickerDialog.show()
    }
}

@Preview
@Composable
fun DateParamItemPreview() {
    MyPlaygroupTheme {
        DateParamItem(
            item = ParameterItem(
                ParameterDisplayType.DATE,
                key = "Key",
                value = LocalDate.of(2021, 12, 11)
            ),
        ){ value, key ->

        }
    }
}