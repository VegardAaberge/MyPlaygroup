package com.myplaygroup.app.feature_login.presentation.create_profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.myplaygroup.app.R
import com.myplaygroup.app.feature_login.presentation.create_profile.CreateProfileScreen

@Composable
fun ProfileImage(
    imageSize: Dp = 200.dp,
) {
    Box(
        modifier = Modifier
            .size(imageSize)
            .background(Color.LightGray, shape = CircleShape)
            .padding(2.dp)
            .background(Color.White, shape = CircleShape)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.75f)
                .align(Alignment.Center)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_person_100),
                contentDescription = "Missing Image",
                tint = Color.LightGray,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(50.dp)
                .background(Color.White, shape = CircleShape)
                .padding(2.dp)
                .background(Color.LightGray, shape = CircleShape)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_a_photo_24),
                contentDescription = "Save Button",
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
    ProfileImage()
}