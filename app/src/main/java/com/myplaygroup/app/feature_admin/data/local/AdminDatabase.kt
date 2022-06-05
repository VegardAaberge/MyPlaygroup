package com.myplaygroup.app.feature_admin.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myplaygroup.app.core.data.local.Converters
import com.myplaygroup.app.core.data.local.DailyClassEntity

@Database(
    entities = [DailyClassEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AdminDatabase : RoomDatabase() {
    abstract fun mainDao(): AdminDao
}