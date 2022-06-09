package com.myplaygroup.app.feature_main.domain.use_cases.data

import com.myplaygroup.app.feature_main.data.local.MainDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class ClearAllTables(
    private val database: MainDatabase
) {
    operator fun invoke(){
        GlobalScope.launch(Dispatchers.IO) {
            database.clearAllTables()
        }
    }
}
