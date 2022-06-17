package com.myplaygroup.app.feature_main.presentation.admin.payments

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.model.Payment
import com.myplaygroup.app.feature_main.domain.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentsViewModel @Inject constructor(
    private val repository: PaymentRepository
) : BaseViewModel() {

    var state by mutableStateOf(PaymentsState())

    fun init(userFlow: MutableStateFlow<List<Payment>>) {
        userFlow.onEach { payments ->
            state = state.copy(
                payments = getGroupedData(payments)
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
                    createErrorMessage = null
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
