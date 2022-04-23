package com.myplaygroup.app.feature_main.presentation.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.myplaygroup.app.feature_main.presentation.MainViewModel
import com.myplaygroup.app.feature_main.presentation.home.HomeScreenEvent

@Composable
fun SettingsScreen(
    mainViewModel: MainViewModel,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    viewModel.mainViewModel = mainViewModel

    Column(modifier = Modifier
        .fillMaxSize()) {
        Spacer(modifier = Modifier.height(16.dp))
        UserSection(viewModel)

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            onClick = {
                viewModel.onEvent(HomeScreenEvent.LogoutButtonTapped)
            }
        ) {
            Text(text = "Logout")
        }
    }
}

@Composable
fun UserSection(viewModel: SettingsViewModel) {

    val profileImage = viewModel.state.imageUri
    val username = viewModel.username
    val profileName = viewModel.profileName

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        RoundImage(
            image = rememberImagePainter(data = profileImage),
            modifier = Modifier
                .size(100.dp)
                .weight(3f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        ProfileInfo(
            header = username,
            subHeader = profileName,
            modifier = Modifier
                .weight(7f)
        )
    }
}

@Composable
private fun RoundImage(
    image: Painter,
    modifier: Modifier
){
    Image(
        painter = image,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                1.dp,
                Color.LightGray,
                CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun ProfileInfo(
    header: String,
    subHeader: String,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Text(
            text = header,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = subHeader)
    }
}