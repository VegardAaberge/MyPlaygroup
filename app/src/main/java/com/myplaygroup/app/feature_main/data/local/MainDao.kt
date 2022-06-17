package com.myplaygroup.app.feature_main.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.myplaygroup.app.feature_main.data.model.*

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

    @Query("DELETE FROM dailyclassentity")
    suspend fun clearAllDailyClasses()

    @Query("DELETE FROM dailyclassentity WHERE modified != 1")
    suspend fun clearSyncedDailyClasses()

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

    @Query("SELECT * FROM monthlyplanentity WHERE clientId = :clientId")
    suspend fun getMonthlyPlanById(clientId: String) : MonthlyPlanEntity

    @Query("SELECT * FROM monthlyplanentity WHERE clientId IN (:clientIds)")
    suspend fun getMonthlyPlansByClientId(clientIds: List<String>): List<MonthlyPlanEntity>

    @Query("SELECT * FROM monthlyplanentity ORDER BY id")
    suspend fun getMonthlyPlans() : List<MonthlyPlanEntity>

    @Query("DELETE FROM monthlyplanentity WHERE clientId = :clientId")
    suspend fun deleteMonthlyPlansById(clientId: String)

    @Query("DELETE FROM monthlyplanentity WHERE modified != 1")
    suspend fun clearSyncedMonthlyPlans()

    @Query("DELETE FROM monthlyplanentity")
    suspend fun clearAllMonthlyPlans()

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

    @Query("SELECT * FROM appuserentity WHERE clientId = :clientId")
    suspend fun getAppUserById(clientId: String) : AppUserEntity

    @Query("SELECT * FROM appuserentity WHERE clientId IN (:clientIds)")
    suspend fun getAppUsersByClientId(clientIds: List<String> ) : List<AppUserEntity>

    @Query("DELETE FROM appuserentity WHERE clientId = :clientId")
    suspend fun deleteAppUsersById(clientId: String)

    @Query("DELETE FROM appuserentity WHERE modified != 1")
    fun clearSyncedAppUsers()

    @Query("DELETE FROM appuserentity")
    suspend fun clearAllAppUsers()

    //
    // Standard Plans
    //
    @Insert(onConflict = REPLACE)
    suspend fun insertStandardPlans(
        messages: List<StandardPlanEntity>
    )

    @Query("SELECT * FROM standardplanentity ORDER BY id")
    suspend fun getStandardPlans() : List<StandardPlanEntity>

    @Query("DELETE FROM standardplanentity")
    suspend fun clearStandardPlans()
}