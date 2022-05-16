package org.inu.http

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DogRepository {
    private val retrofit = NetworkFactory.create<DogHttpService>()

    fun getDog(
        success: (GetData) -> Unit,
    ) {
        retrofit.getDog().enqueue(object : Callback<GetData> {
            override fun onResponse(call: Call<GetData>, response: Response<GetData>) {
                success(response.body()!!)
            }

            override fun onFailure(call: Call<GetData>, t: Throwable) {
                throw Exception(t)
            }
        })
    }
}