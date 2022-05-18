package com.myplaygroup.app.feature_profile.presentation.profile_selector

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_profile.presentation.edit_profile.EditProfileScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileSelectorViewModel @Inject constructor(
    private val imageRepository: ImageRepository
) : BaseViewModel() {

    fun onEvent(event: ProfileSelectorScreenEvent) {
        when(event){
            is ProfileSelectorScreenEvent.TakePictureDone -> {

                viewModelScope.launch {
                    imageRepository.storeProfileImage(event.bitmap)

                    setUIEvent(UiEvent.PopPage)
                }
            }
        }
    }
}