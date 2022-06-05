package com.myplaygroup.app.feature_main.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myplaygroup.app.core.data.local.Converters
import com.myplaygroup.app.core.data.local.DailyClassEntity
import com.myplaygroup.app.core.data.local.MonthlyPlanEntity

@Database(
    entities = [MessageEntity::class, DailyClassEntity::class, MonthlyPlanEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun mainDao(): MainDao
}