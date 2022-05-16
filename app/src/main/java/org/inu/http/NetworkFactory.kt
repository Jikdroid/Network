package org.inu.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkFactory {
    companion object{
        inline fun <reified T> create(): T {
            return Retrofit.Builder()
                .baseUrl("http://dog-api.kinduff.com")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(T::class.java)
        }

        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }
}