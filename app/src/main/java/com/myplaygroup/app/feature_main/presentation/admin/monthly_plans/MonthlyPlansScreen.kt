package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.CustomProgressIndicator
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.core.presentation.theme.MyPlaygroupTheme
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.presentation.admin.AdminScreenEvent
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.plcoding.stockmarketapp.presentation.company_listings.components.MonthlyPlanItem
import java.time.DayOfWeek
import java.time.LocalDate

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
        MonthlyPlanLazyColumn(
            monthlyPlans = state.monthlyPlans,
            navigateToEditScreen = { clientId ->
                adminViewModel.onEvent(
                    AdminScreenEvent.NavigateToEditScreen(
                        type = ParametersType.PLANS,
                        clientId = clientId
                    )
                )
            }
        )

        if(viewModel.isBusy || adminViewModel.isBusy){
            CustomProgressIndicator()
        }
    }
}

@Composable
fun MonthlyPlanLazyColumn(
    monthlyPlans: Map<String, List<MonthlyPlan>>,
    navigateToEditScreen: (String) -> Unit
) {
    LazyColumn(
        reverseLayout = true,
        modifier = Modifier.fillMaxSize()
    ){
        monthlyPlans.forEach { monthGroup ->
            val plansInMonth = monthGroup.value

            items(plansInMonth.size){ i ->
                val monthlyPlan = plansInMonth[i]
                MonthlyPlanItem(
                    monthlyPlan = monthlyPlan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navigateToEditScreen(monthlyPlan.clientId)
                        }
                        .padding(16.dp)

                )

                if(i < plansInMonth.size - 1){
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
                        text = monthGroup.key,
                        style = MaterialTheme.typography.h6,
                        color = Color.White
                    )

                    Text(
                        text = "¥" + monthGroup.value
                            .filter { !it.cancelled }
                            .sumOf { it.planPrice }.toString(),
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
    viewModel: MonthlyPlansViewModel,
    adminViewModel: AdminViewModel
){
    val icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24)
    LaunchedEffect(key1 = viewModel.getUnsyncedMonthlyPlans()){

        val actionButtons = mutableListOf<AdminState.ActionButton>()
        if(viewModel.getUnsyncedMonthlyPlans().any()){
            actionButtons.add(
                AdminState.ActionButton(
                    action = {
                        viewModel.onEvent(MonthlyPlansScreenEvent.UploadMonthlyPlans)
                    },
                    icon = icon
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

@Preview(showBackground = true)
@Composable
fun MonthlyPlanLazyColumnPreview() {
    MyPlaygroupTheme {
        MonthlyPlanLazyColumn(
            monthlyPlans = mapOf(
                "2022 August" to listOf(
                    MonthlyPlan(
                        id = 3,
                        username = "meng",
                        kidName = "ellie",
                        startDate = LocalDate.now().plusMonths(1),
                        endDate = LocalDate.now().plusMonths(2).minusDays(1),
                        daysOfWeek = listOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.FRIDAY
                        ),
                        planName = "Moring v2",
                        cancelled = true,
                        modified = false,
                        planPrice = 490
                    ),
                    MonthlyPlan(
                        id = -1,
                        username = "vegard",
                        kidName = "emma",
                        startDate = LocalDate.now().plusMonths(1),
                        endDate = LocalDate.now().plusMonths(2).minusDays(1),
                        daysOfWeek = listOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.FRIDAY
                        ),
                        planName = "Evening v2",
                        cancelled = false,
                        modified = true,
                        planPrice = 590
                    )
                ),
                "2022 July" to listOf(
                    MonthlyPlan(
                        id = 1,
                        username = "meng",
                        kidName = "ellie",
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusMonths(1).minusDays(1),
                        daysOfWeek = listOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.FRIDAY
                        ),
                        planName = "Moring v2",
                        cancelled = false,
                        modified = false,
                        planPrice = 490
                    ),
                    MonthlyPlan(
                        id = 2,
                        username = "vegard",
                        kidName = "emma",
                        startDate = LocalDate.now(),
                        endDate = LocalDate.now().plusMonths(1).minusDays(1),
                        daysOfWeek = listOf(
                            DayOfWeek.MONDAY,
                            DayOfWeek.WEDNESDAY,
                            DayOfWeek.FRIDAY
                        ),
                        planName = "Evening v2",
                        cancelled = false,
                        modified = false,
                        planPrice = 590
                    )
                ),
            )
        ) {}
    }
}