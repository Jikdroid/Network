package org.inu.http

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.Gson
import kotlinx.coroutines.*
import okhttp3.*
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
    private val client =
        OkHttpClient().newBuilder()
            .addInterceptor(ChangeInterceptor())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonRequest.setOnClickListener {
            run()
        }
    }

    private fun run() {
        val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
            println("CoroutineExceptionHandler : $throwable")
        }
        val facts = CoroutineScope(Dispatchers.IO).async(exceptionHandler) {
            val request = Request.Builder()
                .url("http://dog-api.kinduff.com/api/facts")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                for ((name, value) in response.headers) {
                    println("$name: $value")
                }
                val result = response.body!!.string()
//                val data = Gson().fromJson(result,GetData::class.java)
//                data.facts[0]
                result
            }
        }
        CoroutineScope(Dispatchers.Main).launch {
            textContent.text = facts.await()
        }
    }
}