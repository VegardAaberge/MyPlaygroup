package com.myplaygroup.app.feature_main.presentation

import com.myplaygroup.app.core.domain.Settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userSettingsManager: UserSettingsManager
) : BaseViewModel(){

    val username = userSettingsManager.getFlow {
        it.map { u -> u.username }
    }
}