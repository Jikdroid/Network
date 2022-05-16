package org.inu.http

import retrofit2.Call
import retrofit2.http.GET

interface DogHttpService {
    @GET("/api/facts")
    fun getDog(): Call<GetData>
}