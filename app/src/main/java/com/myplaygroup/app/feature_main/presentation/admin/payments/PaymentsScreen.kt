package com.myplaygroup.app.feature_main.presentation.admin.payments

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.model.Payment
import com.myplaygroup.app.feature_main.presentation.admin.AdminScreenEvent
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.myplaygroup.app.feature_main.presentation.admin.payments.components.PaymentItem
import com.myplaygroup.app.feature_main.presentation.admin.payments.components.ShowCreatePaymentDialog
import java.time.LocalDate

@Composable
fun PaymentScreen(
    adminViewModel: AdminViewModel,
    viewModel: PaymentsViewModel
) {
    CreateToolbarActionItems(
        viewModel = viewModel,
        adminViewModel = adminViewModel
    )

    CatchOnResume(viewModel)

    val scaffoldState = collectEventFlow(viewModel = viewModel)
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        PaymentsLazyColumn(
            payments = state.payments,
            navigateToEditScreen = { clientId ->
                adminViewModel.onEvent(
                    AdminScreenEvent.NavigateToEditScreen(
                        type = ParametersType.PAYMENTS,
                        clientId = clientId
                    )
                )
            }
        )

        if(state.showCreatePayment){
            Dialog(
                onDismissRequest = {
                    viewModel.onEvent(PaymentsScreenEvent.CreatePaymentDialog(false))
                },
                properties = DialogProperties()
            ) {
                ShowCreatePaymentDialog(
                    usernameError = state.usernameError,
                    dateError = state.dateError,
                    amountError = state.amountError,
                    usernameOptions = state.users,
                    createPayment = { username, date, amount ->
                        viewModel.onEvent(PaymentsScreenEvent.CreatePayment(username, date, amount))
                    }
                )
            }
        }

        if(viewModel.isBusy || adminViewModel.isBusy){
            CustomProgressIndicator()
        }
    }
}

@Composable
fun PaymentsLazyColumn(
    payments: Map<String, List<Payment>>,
    navigateToEditScreen: (String) -> Unit
) {
    LazyColumn(
        reverseLayout = true,
        modifier = Modifier.fillMaxSize()
    ){
        payments.forEach { paymentsGroup ->
            val paymentsInMonth = paymentsGroup.value

            items(paymentsInMonth.size){ i ->
                val payment = paymentsInMonth[i]
                PaymentItem(
                    payment = payment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateToEditScreen(payment.clientId)
                        }
                        .padding(16.dp)

                )

                if(i < paymentsInMonth.size - 1){
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.primary)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = paymentsGroup.key,
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )

                    Text(
                        text = "¥" + paymentsGroup.value.sumOf { it.amount }.toString(),
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateToolbarActionItems(
    viewModel: PaymentsViewModel,
    adminViewModel: AdminViewModel
){
    val icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24)
    LaunchedEffect(key1 = viewModel.getUnsyncedPayments()){

        val actionButtons = mutableListOf<AdminState.ActionButton>()
        if(viewModel.getUnsyncedPayments().any()){
            actionButtons.add(
                AdminState.ActionButton(
                    action = {
                        viewModel.onEvent(PaymentsScreenEvent.UploadPayments)
                    },
                    icon = icon
                ),
            )
        }
        actionButtons.add(
            AdminState.ActionButton(
                action = {
                    viewModel.onEvent(PaymentsScreenEvent.CreatePaymentDialog(true))
                },
                icon = Icons.Default.Add
            )
        )

        adminViewModel.state = adminViewModel.state.copy(
            actionButtons = actionButtons
        )
    }
}

@Composable
fun CatchOnResume(
    viewModel: PaymentsViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME){
                viewModel.onEvent(PaymentsScreenEvent.RefreshData)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentsLazyColumnPreview() {
    MyPlaygroupTheme {
        PaymentsLazyColumn(
            payments = mapOf(
                "2022 August" to listOf(
                    Payment(
                        id = -1,
                        username = "meng",
                        date = LocalDate.of(2022, 9, 5),
                        cancelled = false,
                        amount = 790
                    ),
                    Payment(
                        id = 3,
                        username = "vegard",
                        date = LocalDate.of(2022, 9, 3),
                        cancelled = false,
                        amount = 590
                    ),
                ),
                "2022 July" to listOf(
                    Payment(
                        id = 2,
                        username = "meng",
                        date = LocalDate.of(2022, 8, 12),
                        cancelled = false,
                        amount = 590
                    ),
                    Payment(
                        id = 1,
                        username = "vegard",
                        date = LocalDate.of(2022, 9, 7),
                        cancelled = false,
                        amount = 790
                    ),
                ),
            )
        ) {}
    }
}