package com.myplaygroup.app.core.data.network

import android.util.Log
import com.myplaygroup.app.core.util.Constants
import com.myplaygroup.app.core.util.Resource
import kotlinx.coroutines.flow.flow
import retrofit2.Response

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

inline suspend fun <ResultType, RequestType> fetchApi(
    crossinline fetch: suspend () -> Response<RequestType>,
    crossinline processFetch: suspend (RequestType) -> ResultType,
    crossinline onFetchError: suspend (Response<RequestType>) -> String,
    crossinline onFetchException: suspend (Throwable) -> String,
) : Resource<ResultType>{
    return try {
        fetchApiBody(fetch, processFetch, onFetchError);
    } catch (t: Throwable) {
        t.printStackTrace()
        val fetchMessage = onFetchException(t)
        Resource.Error(fetchMessage)
    }
}

inline suspend fun <ResultType, RequestType> fetchApiBody(
    crossinline fetch: suspend () -> Response<RequestType>,
    crossinline processFetch: suspend (RequestType) -> ResultType,
    crossinline onFetchError: suspend (Response<RequestType>) -> String,
) : Resource<ResultType> {

    val response = fetch()

    return if(response.isSuccessful && response.code() == 200){
        val body = response.body()!!
        val data = processFetch(body)

        Resource.Success(data = data)
    }else{
        val localisedMessage = onFetchError(response)

        Log.e(Constants.DEBUG_KEY, "Code: ${response.code()} Error: ${response.message()} Localised: $localisedMessage")
        Resource.Error(localisedMessage)
    }
}

