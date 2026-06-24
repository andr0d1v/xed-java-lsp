package com.androdev.java.lsp.utils

import okhttp3.OkHttpClient
import okhttp3.Request

class JdtlsApi {
    private val client = OkHttpClient()
    private val baseUrl = "https://download.eclipse.org/jdtls/snapshots"

    fun fetchLatestVersion(): String? {
        return runCatching {
            val request = Request.Builder()
                .url("$baseUrl/latest.txt")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) return null

                response.body.string().trim()
            }
        }.getOrNull()
    }
}