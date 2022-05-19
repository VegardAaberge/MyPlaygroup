package com.myplaygroup.app.core.data.remote

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

class NullHostNameVerifier : HostnameVerifier {
    override fun verify(hostname: String, session: SSLSession): Boolean {
        return true
    }
}