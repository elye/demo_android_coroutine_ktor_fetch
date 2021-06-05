package com.elyeproj.networkaccessevolution

import android.util.Log
import okhttp3.HttpUrl

interface NetworkAccess {
    fun fetchData(fetching: suspend (String) -> Network.Result, parameterName: String)
    fun terminate()
    fun logOut(message: String) {
        Log.d("Track", "$message ${Thread.currentThread()}")
    }
}