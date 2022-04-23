package com.myplaygroup.app.feature_main.data.repository

import android.app.Application
import com.myplaygroup.app.core.data.remote.MyPlaygroupApi
import com.myplaygroup.app.core.util.Resource
import com.myplaygroup.app.feature_main.data.local.MainDao
import com.myplaygroup.app.feature_main.data.mapper.toMessage
import com.myplaygroup.app.feature_main.data.mapper.toMessageEntity
import com.myplaygroup.app.feature_main.domain.model.Message
import com.myplaygroup.app.feature_main.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class MainRepositoryImpl @Inject constructor(
    private val api: MyPlaygroupApi,
    private val dao: MainDao
) : MainRepository {

    override suspend fun getChatMessages(
        fetchFromRemote: Boolean
    ): Flow<Resource<List<Message>>> {
        return flow{

            emit(Resource.Loading(true))
            val localListing = dao.getMessages()
            emit(Resource.Success(
                data = localListing.map { it.toMessage() }
            ))

            val shouldJustLoadFromCache = !localListing.isEmpty() && !fetchFromRemote
            if(shouldJustLoadFromCache){
                emit(Resource.Loading(false))
                return@flow
            }

            val response = try{
                api.getMessages()
            }catch (e: IOException){
                e.printStackTrace()
                emit(Resource.Error("Coulnd't load data"))
                null
            }catch (e: HttpException){
                e.printStackTrace()
                emit(Resource.Error("Coulnd't load data"))
                null
            }

            response?.let { comments ->
                dao.clearComments()
                dao.insertMessages(
                    comments.map { it }
                )
                emit(Resource.Success(
                    data = dao
                        .getMessages()
                        .map { it.toMessage() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }
}