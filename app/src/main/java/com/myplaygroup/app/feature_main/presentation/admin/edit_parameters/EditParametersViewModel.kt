package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.interactors.EditParametersInteractor
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditParametersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val editUseCases: EditParametersInteractor
) : BaseViewModel() {

    var state by mutableStateOf(EditParametersState())

    init {
        viewModelScope.launch {
            val type = ParametersType.valueOf(
                savedStateHandle.get<String>("parametersType")?: return@launch
            )
            val id = savedStateHandle.get<Long>("id") ?: return@launch

            val result = editUseCases.fetchParameterItems(type, id)
            if(result is Resource.Success){
                state = state.copy(
                    parameterItems = result.data!!
                )
            }else if (result is Resource.Error){
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
        }
    }

    fun onEvent(event: EditParametersScreenEvent) {
        when (event) {
            is EditParametersScreenEvent.SaveData -> {
                saveData()
            }
        }
    }

    fun saveData() = viewModelScope.launch(Dispatchers.IO){
        val validationResponse = editUseCases.validateParameters(state.parameterItems)

        state = state.copy(
            parameterItems = validationResponse
        )

        if(validationResponse.all { x -> x.error == null }){
            val result = editUseCases.storeParameterItems(state.parameterItems)
            if(result is Resource.Success){
                setUIEvent(
                    UiEvent.PopPage
                )
            }else{
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
        }
    }
}