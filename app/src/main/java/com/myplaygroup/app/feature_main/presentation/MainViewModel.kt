package com.myplaygroup.app.feature_main.presentation

import android.content.SharedPreferences
import androidx.core.content.edit
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,
) : BaseViewModel() {

    fun logout() {
        basicAuthInterceptor.username = null
        basicAuthInterceptor.password = null
        sharedPref.edit {
            putString(Constants.KEY_USERNAME, Constants.NO_USERNAME)
            putString(Constants.KEY_PASSWORD, Constants.NO_PASSWORD)
        }
        setUIEvent(
            UiEvent.PopAndNavigateTo(
                popRoute = MainScreenDestination.route,
                destination = LoginScreenDestination
            )
        )
    }
}