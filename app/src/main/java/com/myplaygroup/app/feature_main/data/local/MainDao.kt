package com.myplaygroup.app.feature_main.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.myplaygroup.app.feature_main.data.model.AppUserEntity
import com.myplaygroup.app.feature_main.data.model.DailyClassEntity
import com.myplaygroup.app.feature_main.data.model.MessageEntity
import com.myplaygroup.app.feature_main.data.model.MonthlyPlanEntity

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

    @Query("DELETE FROM messageentity WHERE id != -1")
    fun clearSyncedComments()

    @Query("SELECT * FROM messageentity ORDER BY created")
    suspend fun getMessages() : List<MessageEntity>

    //
    // Daily Classes
    //
    @Insert(onConflict = IGNORE)
    suspend fun insertDailyClasses(
        dailyClass: List<DailyClassEntity>
    )

    @Insert(onConflict = REPLACE)
    suspend fun insertDailyClass(
        dailyClass: DailyClassEntity
    )

    @Query("SELECT * FROM dailyclassentity WHERE id = :id")
    suspend fun getDailyClassById(id: Long) : DailyClassEntity

    @Query("SELECT * FROM dailyclassentity ORDER BY date")
    suspend fun getDailyClasses() : List<DailyClassEntity>

    @Query("DELETE FROM dailyclassentity WHERE modified != 1")
    suspend fun clearDailyClasses()

    //
    // Monthly Plans
    //
    @Insert(onConflict = IGNORE)
    suspend fun insertMonthlyPlans(
        messages: List<MonthlyPlanEntity>
    )

    @Insert(onConflict = REPLACE)
    suspend fun insertMonthlyPlan(
        message: MonthlyPlanEntity
    )

    @Query("SELECT * FROM monthlyplanentity WHERE id = :id")
    suspend fun getMonthlyPlanById(id: Long) : MonthlyPlanEntity

    @Query("SELECT * FROM monthlyplanentity ORDER BY id")
    suspend fun getMonthlyPlans() : List<MonthlyPlanEntity>

    @Query("DELETE FROM monthlyplanentity WHERE modified != 1")
    suspend fun clearMonthlyPlans()

    //
    // App Users
    //
    @Insert(onConflict = IGNORE)
    suspend fun insertAppUsers(
        users: List<AppUserEntity>
    )

    @Insert(onConflict = REPLACE)
    suspend fun insertAppUser(
        user: AppUserEntity
    )

    @Query("SELECT * FROM appuserentity ORDER BY id")
    suspend fun getAppUsers() : List<AppUserEntity>

    @Query("SELECT * FROM appuserentity WHERE id = :id")
    suspend fun getAppUserById(id: Long) : AppUserEntity

    @Query("SELECT * FROM appuserentity WHERE clientId IN (:clientIds)")
    suspend fun getAppUsersByClientId(clientIds: List<String> ) : List<AppUserEntity>

    @Query("DELETE FROM appuserentity WHERE modified != 1")
    fun clearAppUsers()

    @Query("DELETE FROM appuserentity")
    suspend fun clearAllAppUsers()
}