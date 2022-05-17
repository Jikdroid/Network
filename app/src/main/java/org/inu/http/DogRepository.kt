package org.inu.http

object DogRepository {
    private val retrofit = NetworkFactory.create<DogHttpService>()

    suspend fun getDog(): GetData? {
        return try {
            retrofit.getDog()
        } catch (e: Exception){
            null
        }
    }
}