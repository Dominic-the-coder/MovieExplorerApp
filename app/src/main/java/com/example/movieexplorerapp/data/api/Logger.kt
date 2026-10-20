package com.example.movieexplorerapp.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer

class Logger: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val buffer = Buffer()
        request.body?.writeTo(buffer)
        val myLog = customLog("Custom Logger")
        myLog("${request.method} request to: ${request.url}")
        myLog(buffer.readUtf8())
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(request)
        val duration = System.currentTimeMillis() - startTime
        val responseText = response.peekBody(Int.MAX_VALUE.toLong())
        myLog(responseText.string())
        myLog("It took: $duration ms")
        return response
    }

    fun customLog(key: String): (String) -> Unit {
        val func: (String) -> Unit = {
            Log.d(key, it)
        }
        return func
    }
}