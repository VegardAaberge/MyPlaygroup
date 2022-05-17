package com.myplaygroup.app.feature_main.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.myplaygroup.app.feature_main.data.models.MessageEntity

@Dao
interface MainDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertMessages(
        messages: List<MessageEntity>
    )

    @Insert(onConflict = REPLACE)
    suspend fun insertMessage(
        message: MessageEntity
    )

    @Query("DELETE FROM messageentity")
    fun clearComments()

    @Query("SELECT * FROM messageentity ORDER BY created")
    suspend fun getMessages() : List<MessageEntity>
}