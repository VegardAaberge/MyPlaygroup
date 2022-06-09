package com.myplaygroup.app.feature_main.domain.interactors

import com.myplaygroup.app.feature_main.data.local.MainDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainDaoInteractor @Inject constructor (
    private val database: MainDatabase
){

    @OptIn(DelicateCoroutinesApi::class)
    fun clearAllTables(){
        GlobalScope.launch(Dispatchers.IO) {
            database.clearAllTables()
        }
    }
}