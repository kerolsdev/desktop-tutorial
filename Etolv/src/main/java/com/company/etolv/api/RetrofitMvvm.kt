package com.company.etolv.api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.await

class RetrofitMvvm (private val retrofitRepository: RetrofitRepository) : ViewModel() {


      var getLiveData : MutableLiveData<String> = MutableLiveData()

      fun getDataForScope() = viewModelScope.launch(Dispatchers.IO) {

         val response  = retrofitRepository.getData()

         withContext(Dispatchers.Main) {

             if (response.isSuccessful) {
                 getLiveData.value = response.body()
             } else {
                 Log.e(RetrofitMvvm::class.java.name, "getDataForScope: ${response.message()}", )
             }

         }

    }


}