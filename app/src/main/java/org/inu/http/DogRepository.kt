package org.inu.http

object DogRepository {
    private val retrofit = NetworkFactory.create<DogHttpService>()

    suspend fun getDog(): Result<GetData> {
        return try {
            Result.Success(retrofit.getDog())
        } catch (e: Exception){
            Result.Error(e)
        }
    }
}