package com.myplaygroup.app.feature_main.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.myplaygroup.app.core.data.local.DailyClassEntity
import com.myplaygroup.app.core.data.local.MonthlyPlanEntity
import com.myplaygroup.app.core.domain.model.MonthlyPlan

@Dao
interface MainDao {

    //
    // MESSAGES
    //
    @Insert(onConflict = REPLACE)
    suspend fun insertMessages(
        messages: List<MessageEntity>
    )

    @Insert(onConflict = REPLACE)
    suspend fun insertMessage(
        message: MessageEntity
    )

    @Query("DELETE FROM messageentity WHERE serverId != -1")
    fun clearSyncedComments()

    @Query("SELECT * FROM messageentity ORDER BY created")
    suspend fun getMessages() : List<MessageEntity>

    //
    // Daily Classes
    //
    @Insert(onConflict = REPLACE)
    suspend fun insertDailyClasses(
        messages: List<DailyClassEntity>
    )

    @Query("SELECT * FROM dailyclassentity ORDER BY date")
    suspend fun getDailyClasses() : List<DailyClassEntity>

    @Query("DELETE FROM dailyclassentity")
    fun clearDailyClasses()

    //
    // Monthly Plans
    //
    @Insert(onConflict = REPLACE)
    suspend fun insertMonthlyPlans(
        messages: List<MonthlyPlanEntity>
    )

    @Query("SELECT * FROM monthlyplanentity ORDER BY id")
    suspend fun getMonthlyPlans() : List<MonthlyPlanEntity>

    @Query("DELETE FROM monthlyplanentity")
    fun clearMonthlyPlans()
}