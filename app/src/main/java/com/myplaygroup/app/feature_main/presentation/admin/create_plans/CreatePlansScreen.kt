package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.DropdownOutlinedTextField
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.classes.components.LabelledCheckbox
import com.myplaygroup.app.feature_main.presentation.admin.create_plans.components.OutlinedDateField
import com.myplaygroup.app.feature_main.presentation.admin.create_plans.components.PlansTextFieldItem
import com.myplaygroup.app.feature_main.presentation.admin.create_plans.components.UserCheckbox
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Create Multiple users")
                Switch(
                    checked = state.createMultipleUsers,
                    onCheckedChange = {
                        viewModel.onEvent(CreatePlansScreenEvent.CreateMultipleUsers(it))
                    }
                )
            }

            if(state.createMultipleUsers){
                CreateMultiplePlansBody(viewModel, state)
            }else{
                CreateSinglePlanBody(viewModel, state)
            }
        }
    }
}

@Composable
fun CreateMultiplePlansBody(
    viewModel: CreatePlansViewModel,
    state: CreatePlansState
) {
    Box(
        modifier = Modifier
            .height(30.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = "Month: " + state.multipleStartDate.month.name)
    }

    LazyColumn{
        items(state.baseMonthlyPlans){ item ->

            val daysOfWeek = item.daysOfWeek.joinToString { x -> x.name.take(1) }
            UserCheckbox(
                user = "${item.username} - ${item.kidName} ($daysOfWeek)",
                isChecked = state.basePlansSelected[item.kidName] ?: false,
                userChanged = {
                    viewModel.onEvent(CreatePlansScreenEvent.BasePlanCheckboxTapped(it, item.kidName))
                }
            )
        }
    }
}

@Composable
fun ColumnScope.CreateSinglePlanBody(
    viewModel: CreatePlansViewModel,
    state: CreatePlansState
) {
    val weekdays = state.weekdays
    val weekdayChanged: (DayOfWeek) -> Unit = { dayOfWeek ->
        viewModel.onEvent(CreatePlansScreenEvent.WeekdayChanged(dayOfWeek))
    }

    DropdownOutlinedTextField(
        label = "User",
        items = state.users.map { x -> x.username },
        selected = state.user,
        errorMessage = state.userError,
        selectedChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.UserChanged(it))
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansTextFieldItem(
        label = "Kid",
        selected = state.kid,
        errorMessage = state.kidError,
        selectedChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.KidChanged(it))
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    DropdownOutlinedTextField(
        label = "Plans",
        items = state.standardPlans.map { x -> x.name },
        selected = state.plan,
        errorMessage = state.planError,
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

    state.weekdaysError?.let{ weekdaysError ->
        Text(
            text = weekdaysError,
            color = MaterialTheme.colors.error,
            modifier = Modifier.align(Alignment.Start)
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedDateField(
        label = "Start Date",
        selected = state.startDate,
        errorMessage = state.startDateError,
        timeChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.StartDateChanged(it))
        },
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedDateField(
        label = "End Date",
        selected = state.endDate,
        errorMessage = state.endDateError,
        timeChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.EndDateChanged(it))
        },
        modifier = Modifier.fillMaxWidth(),
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansTextFieldItem(
        label = "Price",
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        selected = state.price.toString(),
        errorMessage = state.priceError,
        selectedChanged = {
            viewModel.onEvent(CreatePlansScreenEvent.PriceChanged(it))
        }
    )
}