package com.myplaygroup.app.feature_admin.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.myplaygroup.app.core.data.local.DailyClassEntity

@Dao
interface AdminDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyClasses(
        messages: List<DailyClassEntity>
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyClass(
        message: DailyClassEntity
    )

    @Query("DELETE FROM dailyclassentity")
    fun clearDailyClasses()

    @Query("SELECT * FROM dailyclassentity ORDER BY date")
    suspend fun getDailyClasses() : List<DailyClassEntity>
}