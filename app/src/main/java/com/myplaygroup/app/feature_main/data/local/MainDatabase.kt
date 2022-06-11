package com.myplaygroup.app.feature_main.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myplaygroup.app.feature_main.data.model.*

@Database(
    entities = [
        MessageEntity::class,
        DailyClassEntity::class,
        MonthlyPlanEntity::class,
        AppUserEntity::class,
        StandardPlanEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun mainDao(): MainDao
}