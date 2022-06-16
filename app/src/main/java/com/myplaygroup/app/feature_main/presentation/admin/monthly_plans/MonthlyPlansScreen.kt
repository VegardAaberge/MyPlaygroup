package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.presentation.admin.AdminScreenEvent
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.plcoding.stockmarketapp.presentation.company_listings.components.MonthlyPlanItem

@Composable
fun MonthlyPlanScreen(
    adminViewModel: AdminViewModel,
    viewModel: MonthlyPlansViewModel
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
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(state.monthlyPlans.size){ i ->
                val monthlyPlan = state.monthlyPlans[i]
                MonthlyPlanItem(
                    monthlyPlan = monthlyPlan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            adminViewModel.onEvent(
                                AdminScreenEvent.NavigateToEditScreen(
                                    type = ParametersType.PLANS,
                                    clientId = monthlyPlan.clientId
                                )
                            )
                        }
                        .padding(16.dp)

                )
                if(i < state.monthlyPlans.size){
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }
        }

        if(viewModel.isBusy || adminViewModel.isBusy){
            CustomProgressIndicator()
        }
    }
}

@Composable
private fun CreateToolbarActionItems(
    viewModel: MonthlyPlansViewModel,
    adminViewModel: AdminViewModel
){
    val actionButtons = mutableListOf<AdminState.ActionButton>()
    if(viewModel.getUnsyncedMonthlyPlans().any()){
        actionButtons.add(
            AdminState.ActionButton(
                action = {
                    viewModel.onEvent(MonthlyPlansScreenEvent.UploadMonthlyPlans)
                },
                icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24)
            ),
        )
    }
    actionButtons.add(
        AdminState.ActionButton(
            action = {
                adminViewModel.onEvent(AdminScreenEvent.NavigateToCreateMonthlyPlan)
            },
            icon = Icons.Default.Add
        )
    )

    adminViewModel.state = adminViewModel.state.copy(
        actionButtons = actionButtons
    )
}

@Composable
fun CatchOnResume(
    viewModel: MonthlyPlansViewModel
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if(event == Lifecycle.Event.ON_RESUME){
                viewModel.onEvent(MonthlyPlansScreenEvent.RefreshData)
            }
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}