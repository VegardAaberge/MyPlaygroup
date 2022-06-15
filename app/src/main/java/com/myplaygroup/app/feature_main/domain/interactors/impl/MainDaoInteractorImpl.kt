package com.myplaygroup.app.feature_main.domain.interactors.impl

import com.myplaygroup.app.feature_main.data.local.MainDatabase
import com.myplaygroup.app.feature_main.domain.interactors.MainDaoInteractor
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainDaoInteractorImpl @Inject constructor (
    private val database: MainDatabase
) : MainDaoInteractor {

    @OptIn(DelicateCoroutinesApi::class)
    override fun clearAllTables() {
        GlobalScope.launch(Dispatchers.IO) {
            database.clearAllTables()
        }
    }
}