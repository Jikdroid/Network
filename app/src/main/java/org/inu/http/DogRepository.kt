package org.inu.http

object DogRepository {
    private val retrofit = NetworkFactory.create<DogHttpService>()

    suspend fun getDog(): Result<GetData> {
        val result = retrofit.getDog()
        return when (result.isSuccessful) {
            true -> {
                Result.Success(result.body()!!)
            }
            false -> {
                Result.Error(Exception("Error"))
            }
        }
    }
}