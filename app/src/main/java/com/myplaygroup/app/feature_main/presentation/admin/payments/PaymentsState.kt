package com.myplaygroup.app.feature_main.presentation.admin.payments

import com.myplaygroup.app.feature_main.domain.model.Payment

data class PaymentsState (
    val payments: Map<String, List<Payment>> = emptyMap(),
    val users: List<String> = emptyList(),
    val showCreatePayment: Boolean = false,
    val usernameError: String? = null,
    val amountError: String? = null,
    val dateError: String? = null,
    val isSearching: Boolean = false,
    val searchValue: String = ""
)