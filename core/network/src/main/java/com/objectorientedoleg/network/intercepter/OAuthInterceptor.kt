package com.objectorientedoleg.network.intercepter

import okhttp3.Interceptor
import okhttp3.Response

internal class OAuthInterceptor(private val accessToken: String) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val updatedRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()
        return chain.proceed(updatedRequest)
    }
}