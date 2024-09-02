package com.company.etolv.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {

    fun api(token: String): RetrofitInterface {

     val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(token))
            .build()

     return Retrofit.Builder()
            .baseUrl("https://portal-api.etolv.net/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(RetrofitInterface::class.java)
    }



}