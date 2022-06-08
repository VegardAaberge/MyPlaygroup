package com.myplaygroup.app.feature_main.presentation.admin.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.R
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.AdminState
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.myplaygroup.app.feature_main.presentation.admin.users.components.ShowAlertDialog
import com.plcoding.stockmarketapp.presentation.company_listings.components.UserItem

@Composable
fun UsersScreen(
    adminViewModel: AdminViewModel,
    viewModel: UsersViewModel = hiltViewModel()
) {
    CreateToolbarActionItems(
        viewModel = viewModel,
        adminViewModel = adminViewModel
    )

    val scaffoldState = collectEventFlow(viewModel = viewModel)
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier.fillMaxWidth(),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(state.appUsers.size){ i ->
                val appUser = state.appUsers[i]
                UserItem(
                    appUser = appUser,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        }
                        .padding(16.dp)
                )
                if(i < state.appUsers.size){
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }
        }

        if(state.showCreateUser){
            Dialog(
                onDismissRequest = {
                    viewModel.onEvent(UsersScreenEvent.CreateUserDialog(false))
                },
                properties = DialogProperties()
            ) {
                ShowAlertDialog(
                    createErrorMessage = state.createErrorMessage,
                    createUser = {
                        viewModel.onEvent(UsersScreenEvent.CreateUser(it))
                    }
                )
            }
        }
    }
}

@Composable
private fun CreateToolbarActionItems(
    viewModel: UsersViewModel,
    adminViewModel: AdminViewModel
){
    val actionButtons = mutableListOf<AdminState.ActionButton>()
    if(viewModel.getUnsyncedUsers().any()){
        actionButtons.add(
            AdminState.ActionButton(
                action = {
                    viewModel.onEvent(UsersScreenEvent.UploadAppUsers)
                },
                icon = ImageVector.vectorResource(id = R.drawable.ic_baseline_cloud_upload_24)
            ),
        )
    }
    actionButtons.add(
        AdminState.ActionButton(
            action = {
                viewModel.onEvent(UsersScreenEvent.CreateUserDialog(true))
            },
            icon = Icons.Default.Add
        )
    )

    adminViewModel.state = adminViewModel.state.copy(
        actionButtons = actionButtons
    )
}