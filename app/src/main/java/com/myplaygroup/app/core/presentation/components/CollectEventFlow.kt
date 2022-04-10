package com.myplaygroup.app.core.presentation.components

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.myplaygroup.app.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CollectEventFlow(viewModel: BaseViewModel) : ScaffoldState {

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest { event ->
            when(event){
                is BaseViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
            }
        }
    }

    return scaffoldState
}