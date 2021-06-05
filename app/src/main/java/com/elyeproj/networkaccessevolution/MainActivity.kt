package com.elyeproj.networkaccessevolution

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.elyeproj.networkaccessevolution.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainView {

    private lateinit var binding: ActivityMainBinding

    private val networkAccessCoroutinesLaunch = NetworkAccessCoroutinesLaunch(this)
    private val networkAccessCoroutinesAsyncAwait = NetworkAccessCoroutinesAsyncAwait(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSearchCoroutinesOkhttpLaunch.setOnClickListener {
            beginSearch(::beginSearchCoroutinesOkhttpLaunch)
        }
        binding.btnSearchCoroutinesOkhttpAsyncAwait.setOnClickListener {
            beginSearch(::beginSearchCoroutinesOkhttpAsyncAwait)
        }
        binding.btnSearchCoroutinesKtorLaunch.setOnClickListener {
            beginSearch(::beginSearchCoroutinesKtorLaunch)
        }
        binding.btnSearchCoroutinesKtorAsyncAwait.setOnClickListener {
            beginSearch(::beginSearchCoroutinesKtorAsyncAwait)
        }
        binding.btnCancelAll.setOnClickListener {
            cancelAllRequest()
        }
    }

    private fun beginSearch(searchFunc : (query: String) -> Unit) {
        if (binding.editSearch.text.toString().isNotEmpty()) {
            searchFunc(binding.editSearch.text.toString())
        }
    }


    private fun beginSearchCoroutinesOkhttpLaunch(queryString: String) {
        networkAccessCoroutinesLaunch.fetchData(Network::fetchOkHttpResult, queryString)
    }

    private fun beginSearchCoroutinesOkhttpAsyncAwait(queryString: String) {
        networkAccessCoroutinesAsyncAwait.fetchData(Network::fetchOkHttpResult, queryString)
    }

    private fun beginSearchCoroutinesKtorLaunch(queryString: String) {
        networkAccessCoroutinesLaunch.fetchData(Network::fetchKtorHttpResult, queryString)
    }

    private fun beginSearchCoroutinesKtorAsyncAwait(queryString: String) {
        networkAccessCoroutinesAsyncAwait.fetchData(Network::fetchKtorHttpResult, queryString)
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelAllRequest()
    }

    private fun cancelAllRequest() {
        networkAccessCoroutinesLaunch.terminate()
        networkAccessCoroutinesAsyncAwait.terminate()
    }

    override fun updateScreen(result: String) {
        binding.txtSearchResult.text = result
    }
}
