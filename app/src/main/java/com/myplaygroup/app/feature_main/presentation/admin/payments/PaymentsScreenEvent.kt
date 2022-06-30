package com.myplaygroup.app.feature_main.presentation.admin.payments

import java.time.LocalDate

sealed class PaymentsScreenEvent {
    object RefreshData : PaymentsScreenEvent()
    object UploadPayments : PaymentsScreenEvent()
    data class CreatePaymentDialog(val show: Boolean) : PaymentsScreenEvent()
    class CreatePayment(
        val username: String,
        val date: LocalDate,
        val amount: String
    ) : PaymentsScreenEvent()
}
