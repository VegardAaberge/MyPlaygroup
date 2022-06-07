package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import javax.inject.Inject

@HiltViewModel
class MonthlyPlansViewModel @Inject constructor(

) : BaseViewModel() {

    var state by mutableStateOf(MonthlyPlansState())

    init {
        state = state.copy(
            monthlyPlans = listOf(
                MonthlyPlan(
                    id = -1,
                    kidName = "emma",
                    paid = true,
                    planName = "Evening v2",
                    daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
                    planPrice = 790
                ),
                MonthlyPlan(
                    id = -1,
                    kidName = "ellie",
                    paid = false,
                    planName = "Evening v2",
                    daysOfWeek = listOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
                    planPrice = 790
                )
            )
        )
    }
}
