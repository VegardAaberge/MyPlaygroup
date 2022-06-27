package com.myplaygroup.app.feature_profile.presentation.profile_selector

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.camera.CameraScreen
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun ProfileSelectorScreen(
    navigator: DestinationsNavigator,
    viewModel: ProfileSelectorViewModel = hiltViewModel(),
) {
    val scaffoldState = collectEventFlow(viewModel, navigator)

    CameraScreen(
        shouldCrop = true,
        scaffoldState = scaffoldState,
        isBusy = viewModel.isBusy
    ){
        viewModel.onEvent(
            ProfileSelectorScreenEvent.TakePictureDone(it)
        )
    }
}