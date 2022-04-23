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
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Constants.KEY_USERNAME
import com.myplaygroup.app.core.util.Constants.NO_USERNAME
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
        username = sharedPref.getString(KEY_USERNAME, NO_USERNAME) ?: NO_USERNAME
        profileName = "Vegard Profile Name"

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
        basicAuthInterceptor.username = null
        basicAuthInterceptor.password = null
        sharedPref.edit {
            putString(Constants.KEY_USERNAME, Constants.NO_USERNAME)
            putString(Constants.KEY_PASSWORD, Constants.NO_PASSWORD)
        }
        mainViewModel.setUIEvent(
            BaseViewModel.UiEvent.PopAndNavigateTo(
                popRoute = MainScreenDestination.route,
                destination = LoginScreenDestination
            )
        )
    }
}