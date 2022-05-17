package org.inu.http

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val buttonRequest: AppCompatButton by lazy {
        findViewById(R.id.buttonRequest)
    }
    private val editUrl: EditText by lazy {
        findViewById(R.id.editUrl)
    }
    private val textContent: TextView by lazy {
        findViewById(R.id.textContent)
    }

    private var result: GetData? = null

    private val client =
        OkHttpClient().newBuilder()
            .addInterceptor(ChangeInterceptor())
            .addInterceptor(RetryInterceptor())
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonRequest.setOnClickListener {
            runWithRetrofit()
        }


    }

    private fun runWithHttp() {
        val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            println("CoroutineExceptionHandler : $throwable")
        }
        val facts = CoroutineScope(Dispatchers.IO).async(exceptionHandler) {
            val request = Request.Builder()
                .url("http://dog-api.kinduff.com/api/facs")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                for ((name, value) in response.headers) {
                    println("$name: $value")
                }
                val result = response.body!!.string()
                val data = Gson().fromJson<GetData>(result, GetData::class.java)
                data.facts[0]
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            textContent.text = facts.await()
        }
    }

    private fun runWithRetrofit() {
        CoroutineScope(Dispatchers.Main).launch {
            when(val result= DogRepository.getDog()){
                is Result.Success -> textContent.text = result.data.facts[0]
                is Result.Error -> Toast.makeText(this@MainActivity, "${result.exception}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}