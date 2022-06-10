package com.myplaygroup.app.feature_main.presentation.admin.edit_parameters

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.domain.enums.ParametersType
import com.myplaygroup.app.feature_main.domain.interactors.EditParametersInteractor
import com.myplaygroup.app.feature_main.domain.model.ParameterItem
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
                    type = type,
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
            is EditParametersScreenEvent.UpdateValue -> {
                state.parameterItems.firstOrNull() {
                        x -> x.key == event.key
                }?.let {
                    updateValue(event.value, it)
                }
            }
        }
    }

    private fun updateValue(value: Any, item: ParameterItem) {

        val items = state.parameterItems.toMutableList()
        val itemIndex = items.indexOf(item)

        items[itemIndex] = item.copy(
            value = value
        )
        state = state.copy(
            parameterItems = items
        )
    }

    fun saveData() = viewModelScope.launch(Dispatchers.IO){
        Log.d(Constants.DEBUG_KEY, "Saving parameters")
        val validationResponse = editUseCases.validateParameters(state.parameterItems, state.type)

        state = state.copy(
            parameterItems = validationResponse
        )

        if(validationResponse.all { x -> x.error == null }){
            val result = editUseCases.storeParameterItems(state.parameterItems, state.type)
            if(result is Resource.Success){
                Log.d(Constants.DEBUG_KEY, "Save done, pop page")
                setUIEvent(
                    UiEvent.PopPage
                )
            }else{
                Log.d(Constants.DEBUG_KEY, "Save failed")
                setUIEvent(
                    UiEvent.ShowSnackbar(result.message!!)
                )
            }
        }
    }
}