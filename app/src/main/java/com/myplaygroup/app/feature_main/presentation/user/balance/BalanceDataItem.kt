package com.myplaygroup.app.feature_main.presentation.user.balance

import java.time.LocalDate

data class BalanceDataItem(
    val title: String,
    val detail: String,
    val balance: Long,
    val date: LocalDate
)
