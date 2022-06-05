@file:JvmName("NavDrawerBodyKt")

package com.myplaygroup.app.feature_admin.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myplaygroup.app.core.presentation.user_section.UserSection
import com.myplaygroup.app.feature_main.presentation.admin.AdminScreenEvent
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel

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