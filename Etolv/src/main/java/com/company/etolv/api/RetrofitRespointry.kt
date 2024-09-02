package com.company.etolv.api

import retrofit2.Response

class RetrofitRepository (var retrofitInterface: RetrofitInterface , var loginModel : LoginModel ) {

    suspend fun getData() = retrofitInterface.login(loginModel)

}
