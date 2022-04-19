package com.myplaygroup.app.core.presentation.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.camera.components.CameraAcceptControls
import com.myplaygroup.app.core.presentation.camera.components.CameraFooter
import com.myplaygroup.app.core.presentation.camera.components.CameraUIAction
import com.myplaygroup.app.core.presentation.camera.components.CameraView
import com.myplaygroup.app.core.presentation.components.CollectEventFlow
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun CameraScreen(
    navigator: DestinationsNavigator,
    viewModel: CameraViewModel = hiltViewModel()
) {
    val scaffoldState = CollectEventFlow(viewModel, navigator)
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(stringResource(R.string.camera_title), navigator)
        }
    ) {

        if(state.photoUri != null)
        {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = rememberImagePainter(state.photoUri),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )

                CameraFooter(false){ action ->
                    when(action){
                        is CameraUIAction.OnRejectClick -> {
                            viewModel.onEvent(CameraScreenEvent.RejectPhoto)
                        }
                        is CameraUIAction.OnAcceptClick -> {
                            viewModel.onEvent(CameraScreenEvent.AcceptPhoto)
                        }
                    }
                }
            }
        }else{
            CameraView(onImageCaptured = { uri, fromGallery ->
                viewModel.onEvent(CameraScreenEvent.TakePhoto(uri, fromGallery))
            }, onError = { imageCaptureException ->
            })
        }
    }
}