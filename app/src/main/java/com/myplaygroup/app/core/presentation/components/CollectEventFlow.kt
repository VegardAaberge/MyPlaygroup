package com.myplaygroup.app.core.presentation.components

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun collectEventFlow(
    viewModel: BaseViewModel,
    navigator: DestinationsNavigator? = null
) : ScaffoldState {

    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    LaunchedEffect(viewModel, context){
        viewModel.eventChannelFlow.collect { event ->
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
                    navigator?.navigate(
                        direction = event.destination
                    )
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