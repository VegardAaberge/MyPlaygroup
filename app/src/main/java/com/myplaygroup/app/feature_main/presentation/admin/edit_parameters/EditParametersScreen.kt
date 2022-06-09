package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.myplaygroup.app.core.presentation.components.DefaultTopAppBar
import com.myplaygroup.app.core.presentation.components.collectEventFlow
import com.myplaygroup.app.feature_main.domain.enums.ParameterDisplayType
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.presentation.admin.edit_parameters.components.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EditParametersScreen(
    id: Long,
    parametersType: ParametersType,
    editViewModel: EditParametersViewModel = hiltViewModel(),
    destination: DestinationsNavigator
) {
    val scaffoldState = collectEventFlow(editViewModel, destination)
    val state = editViewModel.state

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            DefaultTopAppBar(title = "Edit")
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(state.parameterItems.size){ i ->
                val item = state.parameterItems[i]

                when(item.type){
                    ParameterDisplayType.INFO -> {
                        InfoParamItem(item)
                    }
                    ParameterDisplayType.STRING, ParameterDisplayType.NUMBER -> {
                        TextParamItem(item){

                        }
                    }
                    ParameterDisplayType.DATE -> {
                        DateParamItem(item){

                        }
                    }
                    ParameterDisplayType.SWITCH -> {
                        SwitchParamItem(item){

                        }
                    }
                    ParameterDisplayType.TIME -> {
                        TimeParamItem(item){

                        }
                    }
                }


                if(i < state.parameterItems.size){
                    Divider(modifier = Modifier.padding(
                        horizontal = 16.dp
                    ))
                }
            }
        }
    }
}