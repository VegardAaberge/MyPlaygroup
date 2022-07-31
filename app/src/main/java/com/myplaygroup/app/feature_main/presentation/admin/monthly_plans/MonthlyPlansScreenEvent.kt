package com.myplaygroup.app.feature_main.presentation.admin.monthly_plans

sealed class MonthlyPlansScreenEvent {
    data class OnSearchChanged(val searchValue: String) : MonthlyPlansScreenEvent()
    object RefreshData : MonthlyPlansScreenEvent()
    object UploadMonthlyPlans : MonthlyPlansScreenEvent()
    object TriggerSearch : MonthlyPlansScreenEvent()
}
