package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType.*
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EditParametersScreen(
    id: Long,
    parametersType: ParametersType,
    viewModel: EditParametersViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val scaffoldState = collectEventFlow(viewModel, navigator)
    val state = viewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(
                title = "Edit",
                navigationIcon = {
                    AppBarBackButton(navigator)
                },
                actions = {
                    IconButton(
                        onClick = {

                        },
                    ) {
                        Text(text = "Save")
                    }
                }
            )
        }
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ){
            items(state.parameterItems){ item ->
                EditParameterItem(
                    item = item,
                    valueChanged = { value, key ->
                        viewModel.onEvent(EditParametersScreenEvent.UpdateValue(value, key))
                    }
                )
            }
        }
    }
}

@Composable
fun EditParameterItem(
    item: ParameterItem,
    valueChanged: (Any, String) -> Unit
) {
    when(item.type){
        INFO -> {
            InfoParamItem(item)
        }
        STRING, NUMBER -> {
            TextParamItem(item, valueChanged)
        }
        DATE -> {
            DateParamItem(item, valueChanged)
        }
        SWITCH -> {
            SwitchParamItem(item, valueChanged)
        }
        TIME -> {
            TimeParamItem(item, valueChanged)
        }
    }
}