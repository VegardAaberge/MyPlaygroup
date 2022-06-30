package com.myplaygroup.app.feature_main.presentation.user.balance

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.core.util.display
import com.myplaygroup.app.feature_main.domain.model.MonthlyPlan
import com.myplaygroup.app.feature_main.domain.model.Payment
import com.myplaygroup.app.feature_main.domain.repository.MonthlyPlansRepository
import com.myplaygroup.app.feature_main.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val monthlyPlansRepository: MonthlyPlansRepository,
    private val paymentRepository: PaymentRepository,
) : BaseViewModel() {

    var state by mutableStateOf(BalanceState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            monthlyPlansRepository.getMonthlyPlans(false)
                .zip(paymentRepository.getAllPayments(false)) { plans, payments, ->
                    collectBalance(plans, payments)
                }.collect()
        }
    }

    private fun collectBalance(plans: Resource<List<MonthlyPlan>>, payments: Resource<List<Payment>>) = viewModelScope.launch(Dispatchers.Main) {
        when {
            plans is Resource.Success && payments is Resource.Success -> {
                val balanceData = getGroupedBalanceData(
                    plans = plans.data!!,
                    payments = payments.data!!
                )
                val totalBalance = payments.data.sumOf { it.amount } -  plans.data.sumOf { it.planPrice }
                state = state.copy(
                    balanceData = balanceData,
                    totalBalance = totalBalance
                )
            }
            plans is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(plans.message!!)
                )
            }
            payments is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(payments.message!!)
                )
            }
            else -> {}
        }
    }

    fun getGroupedBalanceData(plans: List<MonthlyPlan>, payments: List<Payment>) : Map<String, List<BalanceDataItem>> {
        val balandeData = plans.map { BalanceDataItem(
                title = it.planName.display(),
                detail = it.kidName,
                balance = -it.planPrice,
                date = it.startDate
            ) }.toMutableList()

        balandeData.addAll(
            payments.map { BalanceDataItem(
                title = "Payment from " + it.username.display(),
                detail = it.date.format(DateTimeFormatter.ofPattern("EEEE dd")),
                balance = it.amount,
                date = it.date
            ) }
        )

        return balandeData
            .sortedBy { x -> x.date }
            .groupBy { x -> "${x.date.month} ${x.date.year}" }
    }
}
