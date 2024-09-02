package com.company.etolv.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface RetrofitInterface {

    @POST("public/api/users/login")
    suspend fun login(@Body request: LoginModel): Response<String>


}