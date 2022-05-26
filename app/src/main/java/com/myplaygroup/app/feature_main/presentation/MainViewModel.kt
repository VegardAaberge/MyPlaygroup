package com.myplaygroup.app.feature_main.presentation

import androidx.datastore.core.DataStore
import com.myplaygroup.app.core.data.pref.UserSettings
import com.myplaygroup.app.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    dataStore: DataStore<UserSettings>
) : BaseViewModel(){

    val username: Flow<String> = dataStore.data.map { u -> u.username }

}