package com.myplaygroup.app.feature_main.presentation.user.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.user_section.UserSection
import com.myplaygroup.app.feature_main.presentation.user.MainViewModel

@Composable
fun SettingsScreen(
    mainViewModel: MainViewModel,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    viewModel.mainViewModel = mainViewModel

    val profileImage = viewModel.mainViewModel.state.usernameUri
    val username = viewModel.mainViewModel.username.collectAsState("").value
    val profileName = viewModel.profileName.collectAsState("").value

    Column(modifier = Modifier
        .fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))

        UserSection(
            profileImage = profileImage,
            username = username,
            profileName = profileName,
            editProfileEvent ={
                viewModel.onEvent(SettingsScreenEvent.EditProfileTapped)
            },
            editProfilePictureEvent = {
                viewModel.onEvent(SettingsScreenEvent.EditProfilePictureTapped)
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
                viewModel.onEvent(SettingsScreenEvent.LogoutButtonTapped)
            }
        ) {
            Text(text = "Logout")
        }
    }
}