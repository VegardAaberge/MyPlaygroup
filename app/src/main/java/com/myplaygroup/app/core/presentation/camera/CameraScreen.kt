package com.myplaygroup.app.core.presentation.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.camera.components.CameraFooter
import com.myplaygroup.app.core.presentation.camera.components.CameraUIAction
import com.myplaygroup.app.core.presentation.camera.components.CameraView
import com.myplaygroup.app.core.presentation.components.collectEventFlow

@Composable
fun CameraScreen(
    shouldCrop: Boolean,
    viewModel: CameraViewModel = hiltViewModel(),
    takePhotoCallback: (Bitmap) -> Unit
) {
    val scaffoldState = collectEventFlow(viewModel)

    viewModel.takePhotoCallback = takePhotoCallback
    viewModel.setShouldCrop(shouldCrop)

    var canvasSize = Size(9999f, 9999f)
    var cutRect = Rect(0f,0f,0f,0f)
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    var cutPercentage by remember { mutableStateOf(0.85f) }

    val photoBitmap = viewModel.state.photoBitmap
    val shouldCrop = viewModel.state.shouldCrop

    Scaffold(
        scaffoldState = scaffoldState,
    ) {

        if(photoBitmap != null)
        {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        if(shouldCrop){
                            val newZoom = cutPercentage * zoom
                            val newOffsetX = offsetX + pan.x
                            val newOffsetY = offsetY + pan.y

                            cutPercentage = when {
                                newZoom < 0.4f -> 0.4f
                                newZoom > 1f -> 1f
                                else -> newZoom
                            }

                            val maxOffsetX = (canvasSize.width - cutRect.width) / 2
                            val maxOffsetY = (canvasSize.height - cutRect.width) / 2

                            offsetX = when {
                                newOffsetX < -maxOffsetX -> -maxOffsetX
                                newOffsetX > maxOffsetX -> maxOffsetX
                                else -> newOffsetX
                            }

                            offsetY = when {
                                newOffsetY < -maxOffsetY -> -maxOffsetY
                                newOffsetY > maxOffsetY -> maxOffsetY
                                else -> newOffsetY
                            }
                        }
                    }
                }) {
                Image(
                    bitmap = photoBitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )

                if(shouldCrop){
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                    ){
                        canvasSize = size
                        cutRect = Rect(
                            Offset(
                                center.x + offsetX,
                                center.y + offsetY
                            ),
                            cutPercentage * canvasSize.minDimension / 2
                        )
                        val squarePath = Path().apply {
                            addRect(cutRect)
                        }
                        clipPath(squarePath, clipOp = ClipOp.Difference) {
                            drawRect(SolidColor(Color.Black.copy(alpha = 0.5f)))
                        }
                    }
                }

                CameraFooter(false){ action ->
                    when(action){
                        is CameraUIAction.OnRejectClick -> {
                            viewModel.onEvent(CameraScreenEvent.RejectPhoto)
                        }
                        is CameraUIAction.OnAcceptClick -> {
                            viewModel.onEvent(CameraScreenEvent.AcceptPhoto(cutRect, canvasSize))
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