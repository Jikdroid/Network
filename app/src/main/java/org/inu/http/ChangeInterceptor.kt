package org.inu.http

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class ChangeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
//        val type = object : TypeToken<ResponseWrapper<*>>() {}.type
        val responseJson = response.extractResponseJson()

        val dataPayload = if (responseJson.has(KEY_DATA)) responseJson[KEY_DATA] else EMPTY_JSON

        // json 으로 변환
        val dataJson = JSONObject()
        dataJson.put("facts", dataPayload)

        return response.newBuilder()
            .body(dataJson.toString().toResponseBody())
            .build()
    }

    // Json 이 아니라면 예외 처리
    private fun Response.extractResponseJson(): JSONObject {
        val jsonString = this.body?.string() ?: EMPTY_JSON
        return try {
            JSONObject(jsonString)
        } catch (exception: Exception) {
            println("VinylaResponseUnboxingInterceptor 서버 응답이 json이 아님 : $jsonString")
            throw IllegalStateException(exception)
        }
    }

    companion object {
        private const val EMPTY_JSON = "{}"
        private const val KEY_DATA = "facts"
    }
}