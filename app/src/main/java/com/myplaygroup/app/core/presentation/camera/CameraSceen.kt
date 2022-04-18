package com.myplaygroup.app.core.presentation.camera

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.camera.components.CameraView
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Destination
@Composable
fun CameraScreen(
    navigator: DestinationsNavigator,
    viewModel: CameraViewModel = hiltViewModel()
) {

    DefaultTopAppBar("Camera", navigator)

    val scaffoldState = CollectEventFlow(viewModel, navigator)
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        CameraView(onImageCaptured = { uri, fromGallery ->
            scope.launch(Dispatchers.Main) {
                navigator.popBackStack()
            }
        }, onError = { imageCaptureException ->

        })
    }
}