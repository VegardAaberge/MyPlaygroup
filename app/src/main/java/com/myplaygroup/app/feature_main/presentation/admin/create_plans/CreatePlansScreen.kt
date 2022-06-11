package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.LabelledCheckbox
import com.myplaygroup.app.feature_main.presentation.admin.create_plans.components.OutlinedDateField
import com.myplaygroup.app.feature_main.presentation.admin.create_plans.components.PlansDropdownMenuItem
import com.myplaygroup.app.feature_main.presentation.admin.create_plans.components.PlansTextFieldItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import java.time.DayOfWeek

@Destination
@Composable
fun CreatePlansScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePlansViewModel = hiltViewModel()
) {
    val state = viewModel.state
    val scaffoldState = collectEventFlow(viewModel, navigator)

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(
                title = "Generate",
                navigationIcon = {
                    AppBarBackButton(navigator)
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.onEvent(CreatePlansScreenEvent.GenerateData)
                        },
                    ) {
                        Text(text = "Save")
                    }
                }
            )
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            CreateSinglePlanBody(viewModel, state)
        }
    }
}

@Composable
fun CreateSinglePlanBody(
    viewModel: CreatePlansViewModel,
    state: CreatePlansState
) {
    val weekdays = state.weekdays
    val weekdayChanged: (DayOfWeek) -> Unit = { dayOfWeek ->
        viewModel.onEvent(CreatePlansScreenEvent.WeekdayChanged(dayOfWeek))
    }

    PlansDropdownMenuItem(
        label = "User",
        items = state.users.map { x -> x.username },
        selected = state.user,
        selectedChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.UserChanged(it))
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansTextFieldItem(
        label = "Kid",
        selected = state.kid,
        selectedChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.KidChanged(it))
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansDropdownMenuItem(
        label = "Plans",
        items = state.standardPlans.map { x -> x.name },
        selected = state.plan,
        selectedChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.PlanChanged(it))
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 3.dp, end = 16.dp)
            .fillMaxWidth()
    ) {
        Column {
            LabelledCheckbox(DayOfWeek.MONDAY, weekdays[DayOfWeek.MONDAY] ?: false, weekdayChanged)
            LabelledCheckbox(DayOfWeek.THURSDAY, weekdays[DayOfWeek.THURSDAY] ?: false, weekdayChanged)
        }
        Column {
            LabelledCheckbox(DayOfWeek.TUESDAY, weekdays[DayOfWeek.TUESDAY] ?: false, weekdayChanged)
            LabelledCheckbox(DayOfWeek.FRIDAY, weekdays[DayOfWeek.FRIDAY] ?: false, weekdayChanged)
        }
        Column {
            LabelledCheckbox(DayOfWeek.WEDNESDAY, weekdays[DayOfWeek.WEDNESDAY] ?: false, weekdayChanged)
            LabelledCheckbox(DayOfWeek.SATURDAY, weekdays[DayOfWeek.SATURDAY] ?: false, weekdayChanged)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedDateField(
        label = "Start Date",
        selected = state.startDate,
        timeChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.StartDateChanged(it))
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedDateField(
        label = "End Date",
        selected = state.endDate,
        timeChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.EndDateChanged(it))
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansTextFieldItem(
        label = "Price",
        selected = state.price,
        selectedChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.PriceChanged(it))
        }
    )
}