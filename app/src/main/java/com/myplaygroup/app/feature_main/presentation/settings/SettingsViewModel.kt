package com.myplaygroup.app.feature_main.presentation.settings

import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.data.remote.BasicAuthInterceptor
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants.KEY_ACCESS_TOKEN
import com.myplaygroup.app.core.util.Constants.KEY_PROFILE_NAME
import com.myplaygroup.app.core.util.Constants.KEY_REFRESH_TOKEN
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.core.util.Constants.NO_VALUE
import com.myplaygroup.app.destinations.LoginScreenDestination
import com.myplaygroup.app.destinations.MainScreenDestination
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import com.myplaygroup.app.feature_main.presentation.home.HomeScreenEvent
import com.myplaygroup.app.feature_main.presentation.home.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val basicAuthInterceptor: BasicAuthInterceptor,
    private val imageRepository: ImageRepository,
) : ViewModel() {

    lateinit var mainViewModel: MainViewModel
    lateinit var username: String
    lateinit var profileName: String

    var state by mutableStateOf(HomeState())

    init {
        username = sharedPref.getString(KEY_USERNAME, NO_VALUE) ?: NO_VALUE
        profileName = sharedPref.getString(KEY_PROFILE_NAME, NO_VALUE) ?: NO_VALUE

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

    fun logout(){
        basicAuthInterceptor.accessToken = null
        sharedPref.edit {
            putString(KEY_USERNAME, NO_VALUE)
            putString(KEY_ACCESS_TOKEN, NO_VALUE)
            putString(KEY_REFRESH_TOKEN, NO_VALUE)
        }
        mainViewModel.setUIEvent(
            BaseViewModel.UiEvent.PopAndNavigateTo(
                popRoute = MainScreenDestination.route,
                destination = LoginScreenDestination
            )
        )
    }
}