package com.myplaygroup.app.core.util

import kotlinx.coroutines.flow.*
import java.lang.Exception

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit =  { Unit },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    emit(Resource.Loading(true))
    val data = query().first()

    val flow = if(shouldFetch(data)){
        emit(Resource.Success(data))

        try {
            val fetchedResult = fetch()
            saveFetchResult(fetchedResult)
            query().map {
                Resource.Success(it)
            }

        } catch (t: Throwable){
            onFetchFailed(t)
            query().map {
                Resource.Error("Couldn't reach server. It might be down", it)
            }
        }
    }else{
        query().map { Resource.Success(it) }
    }
    emitAll(flow)
}