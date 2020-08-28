package com.example.helloworld2

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers

class WebServiceRepository {
    private val call = ApiModel.service
    suspend fun getPresident(name: String) = call.presidentName(name)
}

class MainViewModel : ViewModel() {
    private val repository: WebServiceRepository = WebServiceRepository()
    var president = ""

    fun changePresident(pres: String) {
        president = pres
        Log.d("MyLogs", "after change $president")
    }

    val data = liveData(Dispatchers.IO) {
        Log.d("MyLogs", "in data $president")
        Log.d("MyLogs", "launch in data")
        val retrievedData = repository.getPresident(president)
        emit(retrievedData)
    }
}