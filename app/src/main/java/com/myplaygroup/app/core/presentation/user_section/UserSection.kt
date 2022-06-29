package com.myplaygroup.app.core.presentation.user_section

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.myplaygroup.app.core.presentation.user_section.components.EditProfileArrow
import com.myplaygroup.app.core.presentation.user_section.components.ProfileInfo
import com.myplaygroup.app.core.presentation.user_section.components.RoundImage


@Composable
fun UserSection(
    profileImage: Uri?,
    username: String,
    profileName: String,
    isAdminScreen: Boolean,
    editProfilePictureEvent: () -> Unit,
    modifier: Modifier = Modifier,
    editProfileEvent: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        RoundImage(
            image = rememberImagePainter(data = profileImage),
            modifier = Modifier
                .size(100.dp)
                .clickable {
                    editProfilePictureEvent()
                }
        )
        Spacer(modifier = Modifier.width(16.dp))
        ProfileInfo(
            header = username,
            subHeader = profileName,
            modifier = Modifier
                .weight(1f)
        )

        if(isAdminScreen){
            Spacer(modifier = Modifier.width(16.dp))
            EditProfileArrow(
                editProfileEvent = {
                    editProfileEvent()
                }
            )
        }
    }
}





