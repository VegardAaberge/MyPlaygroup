package com.myplaygroup.app.feature_main.presentation.admin.create_plans

sealed class CreatePlansScreenEvent {
    object GenerateData : CreatePlansScreenEvent()
}