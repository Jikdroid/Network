package org.inu.http

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface DogHttpService {
    @GET("/api/facs")
    suspend fun getDog(): GetData
}