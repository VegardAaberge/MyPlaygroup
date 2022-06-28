package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.app_bar.AppBarBackButton
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType.*
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
import com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EditParametersScreen(
    id: String,
    parametersType: String,
    navigator: DestinationsNavigator,
    viewModel: EditParametersViewModel = hiltViewModel(),
) {
    val focusManager = LocalFocusManager.current
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
                            viewModel.onEvent(EditParametersScreenEvent.SaveData)
                        },
                    ) {
                        Text(text = "Save")
                    }
                },
            )
        },
        floatingActionButton = {
            if(state.parameterItems.any { x -> x.type.name == DELETE.name }){
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(EditParametersScreenEvent.DeleteItem)
                    },
                    backgroundColor = MaterialTheme.colors.error
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
        },
        modifier = Modifier
            .clickable (
                interactionSource = MutableInteractionSource(),
                indication = null,
                onClick = {
                    focusManager.clearFocus()
                }
            ),
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

                item.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                }
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
        STRING -> {
            TextParamItem(item, valueChanged)
        }
        NUMBER -> {
            TextParamItem(item){ value, key ->
                try {
                    valueChanged(value.toString().toLong(), key)
                }catch (nfe: NumberFormatException) {
                    // not a valid long
                }
            }
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
        WEEKDAYS -> {
            SelectWeekdaysItem(item, valueChanged)
        }
        OPTIONS -> {
            OptionsParamItem(item, valueChanged)
        }
    }
}
