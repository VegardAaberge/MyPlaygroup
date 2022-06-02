@file:JvmName("NavDrawerBodyKt")

package com.myplaygroup.app.feature_admin.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.myplaygroup.app.core.presentation.user_section.UserSection
import com.myplaygroup.app.feature_main.presentation.settings.SettingsScreenEvent

@Composable
fun NavDrawerHeader(
    viewModel: AdminViewModel
) {
    val profileImage = viewModel.state.adminUri
    val username = viewModel.username.collectAsState(initial = "").value
    val profileName = viewModel.profileName.collectAsState(initial = "").value

    UserSection(
        profileImage = profileImage,
        username = username,
        profileName = profileName,
        editProfileEvent ={
            viewModel.onEvent(AdminScreenEvent.EditProfileTapped)
        },
        editProfilePictureEvent = {
            viewModel.onEvent(AdminScreenEvent.EditProfilePictureTapped)
        },
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 20.dp)
    )
}