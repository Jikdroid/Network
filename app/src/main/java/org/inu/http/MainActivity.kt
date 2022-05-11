package org.inu.http

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonRequest.setOnClickListener {

            val exceptionHandler = CoroutineExceptionHandler { context, throwable ->
                println("CoroutineExceptionHandler : $throwable")
                textContent.text = "Exception 발생!(올바른 URL을 적어주세요)"
            }
            val content = CoroutineScope(Dispatchers.IO).async {
                var urlText = editUrl.text.toString()
                if (!urlText.startsWith("https")) {
                    urlText = "https://${urlText}"
                }
                val url = URL(urlText)
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                val content = StringBuilder()
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val streamReader = InputStreamReader(urlConnection.inputStream)
                    val buffered = BufferedReader(streamReader)
                    while (true) {
                        val line = buffered.readLine() ?: break
                        content.append(line)
                    }
                    buffered.close()
                    urlConnection.disconnect()
                }
                content
            }
            CoroutineScope(Dispatchers.Main+exceptionHandler).launch{
                textContent.text = content.await()
                println(34535435)
            }
            CoroutineScope(Dispatchers.Main).launch{
                println(12312321)
            }
        }
    }
}