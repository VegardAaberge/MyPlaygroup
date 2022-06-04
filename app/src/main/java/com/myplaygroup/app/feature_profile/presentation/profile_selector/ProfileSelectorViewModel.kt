package com.myplaygroup.app.feature_profile.presentation.profile_selector

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.domain.settings.UserSettingsManager
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_profile.presentation.edit_profile.EditProfileScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
                    val username = userSettingsManager.getFlow { x -> x.map { u -> u.username } }.first()

                    imageRepository.storeProfileImage(username, event.bitmap)

                    setUIEvent(UiEvent.PopPage)
                }
            }
        }
    }
}