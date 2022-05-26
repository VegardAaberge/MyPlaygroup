package com.myplaygroup.app.core.util

import android.util.Log
import kotlinx.coroutines.flow.*
import retrofit2.Response
import java.lang.Exception

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: suspend () -> ResultType,
    crossinline fetch: suspend () -> Response<RequestType>,
    crossinline saveFetchResult: suspend (RequestType) -> ResultType,
    crossinline onFetchError: suspend (Response<RequestType>) -> String,
    crossinline onFetchException: suspend (Throwable) -> String,
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {

    emit(Resource.Loading(true))

    val data = query()
    emit(Resource.Success(data))

    if(shouldFetch(data)){
        var fetchedResult = fetchApi(fetch, saveFetchResult, onFetchError, onFetchException)

        if (fetchedResult is Resource.Error && fetchedResult.message == Constants.AUTHENTICATION_ERROR_MESSAGE) {
            fetchedResult = fetchApi(fetch, saveFetchResult, onFetchError, onFetchException)
        }

        emit(fetchedResult)
    }

    emit(Resource.Loading(false))
}

inline fun <ResultType, RequestType> fetchNetworkResource(
    crossinline fetch: suspend () -> Response<RequestType>,
    crossinline processFetch: suspend (RequestType) -> ResultType,
    crossinline onFetchError: suspend (Response<RequestType>) -> String,
    crossinline onFetchException: suspend (Throwable) -> String,
) = flow {

    emit(Resource.Loading(true))

    var fetchedResult = fetchApi(fetch, processFetch, onFetchError, onFetchException)

    if (fetchedResult is Resource.Error && fetchedResult.message == Constants.AUTHENTICATION_ERROR_MESSAGE) {
        fetchedResult = fetchApi(fetch, processFetch, onFetchError, onFetchException)
    }

    emit(fetchedResult)
    emit(Resource.Loading(false))
}

suspend inline fun <ResultType, RequestType> fetchApi(
    crossinline fetch: suspend () -> Response<RequestType>,
    crossinline processFetch: suspend (RequestType) -> ResultType,
    crossinline onFetchError: suspend (Response<RequestType>) -> String,
    crossinline onFetchException: suspend (Throwable) -> String,
) : Resource<ResultType> {
    return try {
        val response = fetch()

        if(response.isSuccessful && response.code() == 200){
            val body = response.body()!!
            val data = processFetch(body)

            Resource.Success(data = data)
        }else{
            val localisedMessage = onFetchError(response)

            Log.e(Constants.DEBUG_KEY, "Code: ${response.code()} Error: ${response.message()} Localised: $localisedMessage")
            Resource.Error(localisedMessage)
        }
    } catch (t: Throwable) {
        t.printStackTrace()
        val fetchMessage = onFetchException(t)
        Resource.Error(fetchMessage)
    }
}