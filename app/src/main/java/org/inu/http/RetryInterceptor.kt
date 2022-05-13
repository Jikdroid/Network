package org.inu.http

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response


class RetryInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request =  chain.request()
        val response = chain.proceed(request)

        if (response.code == 404){
            response.close()
            return refreshCall(request,chain)
        }
        return response
    }
    private fun  refreshCall(req: Request, chain: Interceptor.Chain): Response {
        println("Retrying new request")
        val newRequest : Request = req.newBuilder()
            .url("http://dog-api.kinduff.com/api/facts")
            .build()
        val newResponse: Response = chain.proceed(newRequest)
        while (newResponse.code != 200){
            refreshCall(newRequest,chain)
        }
        return newResponse
    }
}

