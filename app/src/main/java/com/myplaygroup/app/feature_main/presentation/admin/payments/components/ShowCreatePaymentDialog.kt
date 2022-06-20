package com.myplaygroup.app.feature_main.presentation.admin.payments.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.core.presentation.components.DropdownOutlinedTextField
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.presentation.admin.create_plans.components.OutlinedDateField
import java.time.LocalDate

@Composable
fun ShowCreatePaymentDialog(
    usernameOptions: List<String>,
    usernameError: String?,
    dateError: String?,
    amountError: String?,
    createPayment: (String, LocalDate, Int) -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var username by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(LocalDate.now()) }
    var amount by remember { mutableStateOf(0) }

    Surface(
        modifier = Modifier
            .width(300.dp)
            .padding(5.dp)
            .clickable (
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                }
            ),
        shape = RoundedCornerShape(5.dp),
        color = Color.White,
    ) {
        val commonModifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)

        Column(
            modifier = Modifier.padding(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.padding(5.dp))

            Text(
                text = "Create Payment",
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.padding(10.dp))

            DropdownOutlinedTextField(
                label = "Username",
                items = usernameOptions,
                selected = username,
                errorMessage = usernameError,
                selectedChanged = {
                    username = it
                },
                modifier = commonModifier,
                errorModifier = commonModifier.height(25.dp)
            )

            OutlinedTextField(
                value = amount.toString(),
                onValueChange = {
                    try {
                        amount = it.toInt()
                    }catch (e: NumberFormatException){
                        // Not Int
                    }
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                },
                label = { Text(text = "Amount") },
                placeholder = { Text(text = "Amount") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = commonModifier,
                isError = amountError != null
            )

            Box(
                modifier = commonModifier.height(25.dp)
            ) {
                if(amountError != null){
                    Text(
                        text = amountError,
                        color = MaterialTheme.colors.error,
                    )
                }
            }

            OutlinedDateField(
                label = "Date",
                selected = date,
                errorMessage = dateError,
                timeChanged = {
                    date = it
                },
                modifier = commonModifier,
                errorModifier = commonModifier.height(25.dp)
            )

            Button(
                onClick = {
                    createPayment(username, date, amount)
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(60.dp)
                    .padding(10.dp),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colors.primary),
            ) {
                Text(
                    text = "Create",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun ShowCreatePaymentDialogPreview() {
    MyPlaygroupTheme {
        ShowCreatePaymentDialog(
            usernameError = "Username Error",
            dateError = "Date Error",
            amountError = "Amount Error",
            usernameOptions = listOf(
                "EVENING_2",
                "EVENING_3",
                "MORNING_2"
            ),
            createPayment = { _, _, _ ->

            }
        )
    }
}
