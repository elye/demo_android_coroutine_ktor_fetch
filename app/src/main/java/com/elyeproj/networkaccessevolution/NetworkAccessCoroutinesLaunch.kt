package com.elyeproj.networkaccessevolution

import kotlinx.coroutines.*
import okhttp3.HttpUrl

class NetworkAccessCoroutinesLaunch(private val view: MainView) : NetworkAccess {
    private var coroutineScope: CoroutineScope? = null

    private val errorHandler = CoroutineExceptionHandler { context, error ->
        logOut("Launch Exception")
        coroutineScope?.launch(Dispatchers.Main) {
            logOut("Launch Exception Result")
            view.updateScreen(error.localizedMessage ?: "")
        }
    }

    override fun fetchData(fetching: suspend (String) -> Network.Result, searchText: String) {
        coroutineScope?.cancel()
        coroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        coroutineScope?.launch(errorHandler) {
            try {
                logOut("Launch Fetch Started")
                val result = fetching(searchText)
                logOut("Launch Fetch Done")

                yield()
                launch(Dispatchers.Main) {
                    when(result) {
                        is Network.Result.NetworkError -> {
                            view.updateScreen(result.message)
                            logOut("Launch Post Error Result")
                        }
                        is Network.Result.NetworkResult -> {
                            view.updateScreen(result.message)
                            logOut("Launch Post Success Result")
                        }
                    }
                }
            } catch (e: CancellationException) {
                logOut("Launch Cancel Result")
            }
        }
    }

    override fun terminate() {
        coroutineScope?.cancel()
    }
}

