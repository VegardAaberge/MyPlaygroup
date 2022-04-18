package com.myplaygroup.app.feature_main.presentation

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants.KEY_PASSWORD
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.core.util.Constants.NO_PASSWORD
import com.myplaygroup.app.core.util.Constants.NO_USERNAME
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sharedPref: SharedPreferences
) :BaseViewModel() {

    fun onEvent(event: MainScreenEvent){
        when(event){
            is MainScreenEvent.LogoutButtonTapped -> {
                logout()
            }
        }
    }

    private fun logout(){
        viewModelScope.launch {
            sharedPref.edit {
                putString(KEY_USERNAME, NO_USERNAME)
                putString(KEY_PASSWORD, NO_PASSWORD)
            }
            _eventFlow.emit(
                UiEvent.PopAndNavigateTo(
                    popRoute = MainScreenDestination.route,
                    destination = LoginScreenDestination
                )
            )
        }
    }
}