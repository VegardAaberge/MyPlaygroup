package com.myplaygroup.app.core.data.remote

import com.myplaygroup.app.core.util.Constants.IGNORE_AUTH_URLS
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BasicAuthInterceptor @Inject constructor() : Interceptor {

    var username: String? = null
    var password: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if(request.url.encodedPath in IGNORE_AUTH_URLS){
            return chain.proceed(request)
        }
        val authenticatedRequest = request.newBuilder()
            .header(
                "Authorization",
                Credentials.basic(username ?: "", password ?: "")
            )
            .build()
        return chain.proceed(authenticatedRequest)
    }
}