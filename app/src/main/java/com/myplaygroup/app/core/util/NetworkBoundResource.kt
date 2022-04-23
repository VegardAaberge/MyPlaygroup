package com.myplaygroup.app.core.util

import kotlinx.coroutines.flow.*
import java.lang.Exception

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: suspend () -> ResultType,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit =  { Unit },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    emit(Resource.Loading(true))
    val data = query()

    if(shouldFetch(data)){
        try {
            val fetchedResult = fetch()
            saveFetchResult(fetchedResult)

            emit(Resource.Success(
                data = query()
            ))

        } catch (t: Throwable){
            onFetchFailed(t)

            emit(Resource.Error(
                message = "Couldn't reach server: ${t.message}",
                data = query()
            ))
        }
    }else{
        emit(Resource.Success(
            data = query()
        ))
    }

    emit(Resource.Loading(false))
}