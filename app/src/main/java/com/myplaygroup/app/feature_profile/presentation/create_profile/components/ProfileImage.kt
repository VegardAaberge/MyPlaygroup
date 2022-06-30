package com.myplaygroup.app.feature_profile.presentation.create_profile.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.R

@Composable
fun ProfileImage(
    profileBitmap: Bitmap?,
    takePicture: () -> Unit,
    imageSize: Dp = 200.dp,
    profileBitmapError: String?,
) {
    val drawColor = if(profileBitmapError == null){
        Color.LightGray
    }else MaterialTheme.colors.error

    Box(
        modifier = Modifier
            .size(imageSize)
            .background(drawColor, shape = CircleShape)
            .padding(2.dp)
            .background(Color.White, shape = CircleShape)
            .clickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = takePicture
            )
    ) {
        if(profileBitmap != null)
        {
            Image(
                bitmap = profileBitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        }else{
            Box(
                modifier = Modifier
                    .fillMaxSize(0.75f)
                    .align(Alignment.Center)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_person_100),
                    contentDescription = null,
                    tint = drawColor,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }

        Box(
            modifier = Modifier
                .clip(CircleShape)
                .padding(4.dp)
                .size(50.dp)
                .background(Color.White, shape = CircleShape)
                .padding(2.dp)
                .background(Color.LightGray, shape = CircleShape)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProfileImage(
        profileBitmap = null,
        takePicture = {},
        profileBitmapError = "Set the profile image"
    )
}