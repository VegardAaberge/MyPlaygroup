package com.myplaygroup.app.feature_main.presentation.admin.users

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.presentation.admin.AdminViewModel
import com.plcoding.stockmarketapp.presentation.company_listings.components.UserItem

@Composable
fun UsersScreen(
    adminViewModel: AdminViewModel,
    viewModel: UsersViewModel = hiltViewModel()
) {
    val scaffoldState = collectEventFlow(viewModel = viewModel)

    adminViewModel.state = adminViewModel.state.copy(
        actionButtons = listOf()
    )
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
                        .padding(16.dp)
                        .clickable {

                        }
                )
                if(i < state.appUsers.size){
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }
        }
    }
}