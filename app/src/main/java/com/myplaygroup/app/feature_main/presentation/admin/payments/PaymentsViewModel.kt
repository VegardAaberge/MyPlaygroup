package com.myplaygroup.app.feature_main.presentation.admin.payments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.interactors.MainValidationInteractors
import com.myplaygroup.app.feature_main.domain.model.AppUser
import com.myplaygroup.app.feature_main.domain.model.Payment
import com.myplaygroup.app.feature_main.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val repository: PaymentRepository,
    private val mainValidationInteractors: MainValidationInteractors
) : BaseViewModel() {

    var state by mutableStateOf(PaymentsState())

    fun init(
        paymentFlow: MutableStateFlow<List<Payment>>,
        usersFlow: MutableStateFlow<List<AppUser>>,
    ) {
        paymentFlow.onEach { payments ->
            state = state.copy(
                payments = getGroupedData(payments)
            )
        }.launchIn(viewModelScope)

        usersFlow.onEach { users ->
            state = state.copy(
                users = users.map { x -> x.username }
            )
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: PaymentsScreenEvent) {
        when (event) {
            is PaymentsScreenEvent.RefreshData -> {
                viewModelScope.launch(Dispatchers.IO) {
                    repository
                        .getAllPayments(false)
                        .collect{ collectPayments(it, false) }
                }
            }
            is PaymentsScreenEvent.UploadPayments -> {
                val unsyncedPayments = getUnsyncedPayments()
                viewModelScope.launch(Dispatchers.IO) {
                    repository
                        .uploadPayments(unsyncedPayments)
                        .collect{ collectPayments(it, true) }
                }
            }
            is PaymentsScreenEvent.CreatePaymentDialog -> {
                state = state.copy(
                    showCreatePayment = event.show,
                    usernameError = null,
                    amountError = null,
                    dateError = null
                )
            }
            is PaymentsScreenEvent.CreatePayment -> {
                addPayment(
                    username = event.username,
                    amount = event.amount,
                    date = event.date
                )
            }
        }
    }

    private fun addPayment(
        username: String,
        amount: Int,
        date: LocalDate
    ) = viewModelScope.launch {
        val usernameResult = mainValidationInteractors.usernameValidator(username)
        val amountResult = mainValidationInteractors.amountValidator(amount)
        val dateResult = mainValidationInteractors.dateValidator(date)

        val hasError = listOf(
            usernameResult, amountResult, dateResult
        ).any { !it.successful }

        state = state.copy(
            usernameError = usernameResult.errorMessage,
            amountError = amountResult.errorMessage,
            dateError = dateResult.errorMessage
        )

        if (!hasError) {
            val result = repository.addPaymentToDatabase(
                Payment(
                    username = username,
                    amount = amount.toLong(),
                    date = date
                )
            );

            if (result is Resource.Success) {
                val payments = state.payments.flatMap { it.value }.toMutableList()
                payments.add(result.data!!)

                state = state.copy(
                    showCreatePayment = false,
                    payments = getGroupedData(payments)
                )
            } else {
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
        }
    }

    private fun collectPayments(result: Resource<List<Payment>>, fetchFromRemote: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        when(result){
            is Resource.Success -> {
                state = state.copy(
                    payments = getGroupedData(result.data!!)
                )
            }
            is Resource.Error -> {
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
            is Resource.Loading -> {
                if(fetchFromRemote){
                    isBusy(result.isLoading)
                }
            }
        }
    }

    fun getUnsyncedPayments(): List<Payment> {
        if(isBusy)
            return emptyList()
        return state.payments.flatMap { x -> x.value }.filter { x -> x.modified }
    }

    fun getGroupedData(data: List<Payment>) : Map<String, List<Payment>> {
        return data
            .sortedByDescending { x -> x.date }
            .groupBy { x -> "${x.date.month} ${x.date.year}" }
    }
}
