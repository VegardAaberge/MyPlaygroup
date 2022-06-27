package com.myplaygroup.app.feature_profile.presentation.profile_selector

import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSelectorViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val userSettingsManager: UserSettingsManager
) : BaseViewModel() {

    fun onEvent(event: ProfileSelectorScreenEvent) {
        when(event){
            is ProfileSelectorScreenEvent.TakePictureDone -> {

                viewModelScope.launch {
                    try {
                        isBusy(true)

                        val username =
                            userSettingsManager.getFlow { x -> x.map { u -> u.username } }.first()

                        val response = imageRepository.storeProfileImage(username, event.bitmap)
                        if (response is Resource.Error) {
                            setUIEvent(
                                UiEvent.ShowSnackbar(response.message!!)
                            )
                        }

                        setUIEvent(UiEvent.PopPage)

                    } finally {
                        isBusy(false)
                    }
                }
            }
        }
    }
}