package com.myplaygroup.app.feature_main.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.myplaygroup.app.feature_main.data.models.MessageEntity

@Database(
    entities = [MessageEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MainDatabase : RoomDatabase() {
    abstract fun mainDao(): MainDao
}