package com.myplaygroup.app.feature_main.presentation.admin.payments

sealed class PaymentsScreenEvent {
    object RefreshData : PaymentsScreenEvent()
    object UploadPayments : PaymentsScreenEvent()
    data class CreatePaymentDialog(val show: Boolean) : PaymentsScreenEvent()
}
