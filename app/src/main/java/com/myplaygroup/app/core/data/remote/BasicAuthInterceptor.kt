package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.core.util.Constants.IGNORE_AUTH_URLS
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BasicAuthInterceptor @Inject constructor() : Interceptor {

    var accessToken: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if(request.url.encodedPath in IGNORE_AUTH_URLS){
            return chain.proceed(request)
        }
        val authenticatedRequest = request.newBuilder()
            .header(
                "Authorization",
                "Bearer " + accessToken
            )
            .build()
        return chain.proceed(authenticatedRequest)
    }
}