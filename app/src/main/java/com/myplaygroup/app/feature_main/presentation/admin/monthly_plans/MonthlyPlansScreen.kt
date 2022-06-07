package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.plcoding.stockmarketapp.presentation.company_listings.components.MonthlyPlanItem

@Composable
fun MonthlyPlanScreen(
    adminViewModel: AdminViewModel,
    viewModel: MonthlyPlansViewModel = hiltViewModel()
) {
    val scaffoldState = collectEventFlow(viewModel = viewModel)

    adminViewModel.state = adminViewModel.state.copy(
        actionButtons = listOf()
    )
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
                        .padding(16.dp)
                )
                if(i < state.monthlyPlans.size){
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }
        }
    }
}