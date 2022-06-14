package com.myplaygroup.app.core.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class NetworkBehaviourInterceptor(
    private val networkDelay: Long
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            Thread.sleep(networkDelay)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return chain.proceed(chain.request())
    }
}