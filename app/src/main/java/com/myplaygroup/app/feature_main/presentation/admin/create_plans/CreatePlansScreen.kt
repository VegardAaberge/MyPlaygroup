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
import io.ktor.util.date.*
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
    val maxMonthInt = monthlyPlans.map { x -> x.month }.maxOfOrNull { x -> x.ordinal } ?: 1
    val maxYearInt = monthlyPlans.map { x -> x.year }.maxOfOrNull { x -> x } ?: LocalDate.now().year

    val latestMonth = Month.from(maxMonthInt).name.lowercase().replaceFirstChar { x -> x.uppercase() }

    val users = viewModel.state.users

    val weekdays by remember { mutableStateOf(initWeekdays()) }
    val weekdayChanged: (DayOfWeek) -> Unit = { dayOfWeek ->
        val currentValue = weekdays[dayOfWeek] ?: false
        weekdays.put(dayOfWeek, !currentValue)
    }

    val years = listOf(
        LocalDate.now().minusYears(1).year.toString(),
        LocalDate.now().year.toString(),
        LocalDate.now().plusYears(1).year.toString()
    )
    val months = Month.values().map { x -> x.name.lowercase().replaceFirstChar { y -> y.uppercase() } }
    val plans = listOf("Evening V2", "Evening V3", "Morning V2", "Morning V3", "Morning V4", "Morning V5")

    var selectedUser by remember { mutableStateOf("") }
    var selectedKid by remember { mutableStateOf("") }
    var selectedPlan by remember { mutableStateOf("") }
    var selectedPrice by remember { mutableStateOf("0") }
    var selectedYear by remember { mutableStateOf(maxYearInt.toString()) }
    var selectedMonth by remember { mutableStateOf(latestMonth) }
    var selectedStartDate by remember { mutableStateOf(LocalDate.of(maxYearInt, maxMonthInt, 1)) }

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
        label = "Year",
        items = years,
        selected = selectedYear,
        selectedChanged = {
            selectedYear = it
        }
    )

    PlansDropdownMenuItem(
        label = "Month",
        items = months,
        selected = selectedMonth,
        selectedChanged = {
            selectedMonth = it
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    PlansDropdownMenuItem(
        label = "Plans",
        items = plans,
        selected = selectedPlan,
        selectedChanged = {
            selectedPlan = it
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

    PlansTextFieldItem(
        label = "Price",
        selected = selectedPrice,
        selectedChanged = {
            selectedPrice = it
        }
    )

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedDateField(
        label = "Start Date",
        selected = selectedStartDate,
        timeChanged = {
            selectedStartDate = it
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