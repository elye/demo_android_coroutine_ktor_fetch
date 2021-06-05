package com.elyeproj.networkaccessevolution

import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.Duration

object Network {
    private val ktorHttpClient = HttpClient(CIO)

    suspend fun fetchKtorHttpResult(queryString: String): Result {
        val response: HttpResponse = ktorHttpClient.submitForm(
            url = "https://en.wikipedia.org/w/api.php",
            formParameters = Parameters.build {
                append("action", "query")
                append("format", "json")
                append("list", "search")
                append("srsearch", queryString)
            },
            encodeInQuery = true
        )

        if (response.status == HttpStatusCode.OK) {
            val raw = response.readText()
            val result = Gson().fromJson(raw, Model.Result::class.java)
            return Result.NetworkResult(result.query.searchinfo.totalhits.toString())
        }

        return Result.NetworkError(response.status.description)
    }

    private val okHttpClient = OkHttpClient
        .Builder()
        .connectTimeout(Duration.ofSeconds(1L))
        .build()

    fun fetchOkHttpResult(queryString: String): Result {
        val httpUrl = HttpUrl.Builder()
            .host("en.wikipedia.org")
            .addPathSegment("w")
            .scheme("https")
            .addPathSegment("api.php")
            .addQueryParameter("action", "query")
            .addQueryParameter("format", "json")
            .addQueryParameter("list", "search")
            .addQueryParameter("srsearch", queryString)
            .build()

        val request = Request.Builder().get().url(httpUrl).build()
        val response = okHttpClient.newCall(request).execute()
        if (!response.isSuccessful)
            return Result.NetworkError("Error ${response.code}:${response.message}")

        val raw = response.body?.string()
        val result = Gson().fromJson(raw, Model.Result::class.java)
        return Result.NetworkResult(result.query.searchinfo.totalhits.toString())
    }

    sealed class Result {
        class NetworkError(val message: String) : Result()
        class NetworkResult(val message: String) : Result()
    }
}
