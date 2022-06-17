package com.myplaygroup.app.feature_main.presentation.admin.payments

import com.myplaygroup.app.feature_main.domain.model.Payment

data class PaymentsState (
    val payments: Map<String, List<Payment>> = emptyMap(),
    val showCreatePayment: Boolean = false,
    val createErrorMessage: String? = null,
)