package com.myplaygroup.app.core.presentation.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R

@Composable
fun BoxScope.CameraFooter(
    takePhotoMode: Boolean,
    cameraUIAction: (CameraUIAction) -> Unit
) {
    Column(
        modifier = Modifier.align(Alignment.BottomCenter),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .requiredHeight(100.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(takePhotoMode){
                CameraPhotoControls(cameraUIAction)
            }else{
                CameraAcceptControls(cameraUIAction)
            }
        }
    }
}

@Composable
fun CameraPhotoControls(cameraUIAction: (CameraUIAction) -> Unit) {
    CameraControl(
        label = stringResource(R.string.camera_flip),
        painter = painterResource(id = R.drawable.ic_baseline_flip_camera_android_24),
        modifier= Modifier.size(64.dp),
        onClick = { cameraUIAction(CameraUIAction.OnSwitchCameraClick) }
    )

    CameraPhotoControl(
        painter = painterResource(id = R.drawable.ic_baseline_lens_24),
        modifier= Modifier
            .size(64.dp)
            .padding(1.dp)
            .border(1.dp, Color.White, CircleShape),
        onClick = { cameraUIAction(CameraUIAction.OnCameraClick) }
    )

    CameraControl(
        label = stringResource(R.string.camera_gallery),
        painter = painterResource(id = R.drawable.ic_baseline_photo_library_24),
        modifier= Modifier.size(64.dp),
        onClick = { cameraUIAction(CameraUIAction.OnGalleryViewClick) }
    )
}

@Composable
fun CameraAcceptControls(cameraUIAction: (CameraUIAction) -> Unit) {
    CameraControl(
        label = stringResource(R.string.camera_reject),
        vector = Icons.Sharp.Close,
        modifier= Modifier.size(64.dp),
        onClick = { cameraUIAction(CameraUIAction.OnRejectClick) }
    )

    Spacer(
        modifier= Modifier
            .size(65.dp),
    )

    CameraControl(
        label = stringResource(R.string.camera_accept),
        vector = Icons.Sharp.Check,
        modifier= Modifier.size(64.dp),
        onClick = { cameraUIAction(CameraUIAction.OnAcceptClick) }
    )
}


@Composable
fun CameraPhotoControl(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = modifier,
            tint = Color.White
        )
    }
}

@Composable
fun CameraControl(
    label: String,
    vector: ImageVector? = null,
    painter: Painter? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
    ) {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            if(vector != null){
                VectorIcon(vector)
            }else if(painter != null){
                PainterIcon(painter)
            }

            Spacer(modifier = Modifier.height(1.dp))

            Text(
                text = label,
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
fun ColumnScope.VectorIcon(imageVector: ImageVector) {
    Icon(
        imageVector,
        contentDescription = null,
        modifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),
        tint = Color.White
    )
}

@Composable
fun ColumnScope.PainterIcon(painter: Painter) {
    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .weight(2f)
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally),
        tint = Color.White
    )
}

