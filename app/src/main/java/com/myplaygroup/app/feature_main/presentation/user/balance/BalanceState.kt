package com.myplaygroup.app.feature_main.presentation.user.balance

data class BalanceState (
    val balanceData: Map<String, List<BalanceDataItem>> = emptyMap(),
    val totalBalance: Long = 0,
)