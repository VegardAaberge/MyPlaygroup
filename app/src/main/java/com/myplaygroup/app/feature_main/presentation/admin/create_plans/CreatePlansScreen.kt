package com.myplaygroup.app.feature_main.presentation.admin.create_plans

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import java.time.LocalDate
import java.util.*

@Destination
@Composable
fun CreatePlansScreen(
    navigator: DestinationsNavigator,
    viewModel: CreatePlansViewModel = hiltViewModel()
) {
    val scaffoldState = collectEventFlow(viewModel, navigator)
    val state = viewModel.state

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
            CreateSinglePlanBody(viewModel)
        }
    }
}

@Composable
fun CreateSinglePlanBody(
    viewModel: CreatePlansViewModel
) {
    val monthlyPlans = viewModel.state.monthlyPlans
    val maxDate = monthlyPlans.maxOfOrNull { x -> x.startDate } ?: LocalDate.now()
    val startDate = LocalDate.of(maxDate.year, maxDate.month, 1)
    val endDate = startDate.plusMonths(1).minusDays(1)

    val users = viewModel.state.users
    val plans = viewModel.state.standardPlans

    val weekdays by remember { mutableStateOf(initWeekdays()) }
    val weekdayChanged: (DayOfWeek) -> Unit = { dayOfWeek ->
        val currentValue = weekdays[dayOfWeek] ?: false
        weekdays.put(dayOfWeek, !currentValue)
    }

    var selectedUser by remember { mutableStateOf("") }
    var selectedKid by remember { mutableStateOf("") }
    var selectedPlan by remember { mutableStateOf("") }
    var selectedPrice by remember { mutableStateOf("0") }
    var selectedStartDate by remember { mutableStateOf(startDate) }
    var selectedEndDate by remember { mutableStateOf(endDate) }

    PlansDropdownMenuItem(
        label = "User",
        items = users.map { x -> x.username },
        selected = selectedUser,
        selectedChanged = {
            selectedUser = it
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansTextFieldItem(
        label = "Kid",
        selected = selectedKid,
        selectedChanged = {
            selectedKid = it
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansDropdownMenuItem(
        label = "Plans",
        items = plans.map { x -> x.name },
        selected = selectedPlan,
        selectedChanged = {
            val currentPlan = plans.first { x -> x.name == it }
            selectedPrice = currentPlan.price.toString()
            selectedPlan = currentPlan.name
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
        selected = selectedStartDate,
        timeChanged = {
            selectedStartDate = it
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedDateField(
        label = "End Date",
        selected = selectedEndDate,
        timeChanged = {
            selectedEndDate = it
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansTextFieldItem(
        label = "Price",
        selected = selectedPrice,
        selectedChanged = {
            selectedPrice = it
        }
    )
}

private fun initWeekdays() : EnumMap<DayOfWeek, Boolean> {
    val weekdays : EnumMap<DayOfWeek, Boolean> = EnumMap(DayOfWeek::class.java)
    weekdays.put(DayOfWeek.MONDAY, true)
    weekdays.put(DayOfWeek.TUESDAY, true)
    weekdays.put(DayOfWeek.WEDNESDAY, true)
    weekdays.put(DayOfWeek.THURSDAY, true)
    weekdays.put(DayOfWeek.FRIDAY, true)
    weekdays.put(DayOfWeek.SATURDAY, false)
    return weekdays
}