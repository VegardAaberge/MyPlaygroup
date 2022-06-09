package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.presentation.admin.AdminScreenEvent
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.plcoding.stockmarketapp.presentation.company_listings.components.MonthlyPlanItem

@Composable
fun MonthlyPlanScreen(
    adminViewModel: AdminViewModel,
    viewModel: MonthlyPlansViewModel = hiltViewModel()
) {
    CreateToolbarActionItems(
        viewModel = viewModel,
        adminViewModel = adminViewModel
    )

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
                                    id = monthlyPlan.id
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

        if(state.showCreateMonthlyPlan){
            Dialog(
                onDismissRequest = {
                    viewModel.onEvent(MonthlyPlansScreenEvent.CreateMonthlyPlanDialog(false))
                },
                properties = DialogProperties()
            ) {
                Text(text = "Popup")
            }
        }
    }
}

@Composable
private fun CreateToolbarActionItems(
    viewModel: MonthlyPlansViewModel,
    adminViewModel: AdminViewModel
){
    adminViewModel.state = adminViewModel.state.copy(
        actionButtons = listOf(
            AdminState.ActionButton(
                icon = Icons.Default.Add,
                action = {
                    viewModel.onEvent(MonthlyPlansScreenEvent.CreateMonthlyPlanDialog(true))
                }
            )
        )
    )
}