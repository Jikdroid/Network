package org.inu.http

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("User-Agent","PostmanRuntime/7.29.0")
            .build()
        return chain.proceed(request)
    }
}