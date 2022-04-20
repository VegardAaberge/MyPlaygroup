package com.myplaygroup.app.core.presentation.components

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavOptions
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.feature_login.presentation.create_profile.CreateProfileScreen
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CollectEventFlow(
    viewModel: BaseViewModel,
    navigator: DestinationsNavigator? = null
) : ScaffoldState {

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is BaseViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is BaseViewModel.UiEvent.PopPage -> {
                    navigator?.popBackStack()
                }
                is BaseViewModel.UiEvent.NavigateTo -> {
                    navigator?.navigate(event.destination)
                }
                is BaseViewModel.UiEvent.PopAndNavigateTo -> {
                    navigator?.navigate(event.destination) {
                        popUpTo(event.popRoute){
                            inclusive =  true
                        }
                    }
                }
            }
        }
    }

    return scaffoldState
}