package com.myplaygroup.app.feature_main.presentation.home

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.repository.ImageRepository
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
class HomeViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val imageRepository: ImageRepository
) :BaseViewModel() {

    var state by mutableStateOf(HomeScreenState())

    init {
        viewModelScope.launch {
            val uri = imageRepository.getProfileImage()
            state = state.copy(
                imageUri = uri
            )
        }

    }

    fun onEvent(event: HomeScreenEvent){
        when(event){
            is HomeScreenEvent.LogoutButtonTapped -> {
                logout()
            }
        }
    }

    private fun logout(){
        viewModelScope.launch {
            basicAuthInterceptor.username = null
            basicAuthInterceptor.password = null
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