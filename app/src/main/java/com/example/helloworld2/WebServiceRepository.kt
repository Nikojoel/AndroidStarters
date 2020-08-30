package com.example.helloworld2

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers

class WebServiceRepository {
    private val call = ApiModel.service
    suspend fun getPresident(name: String) = call.presidentName(name)
}

class MainViewModel : ViewModel() {
    private val repository: WebServiceRepository = WebServiceRepository()
    private val presidents = MutableLiveData<String>()

    fun changePresident(pres: String) {
        presidents.value = pres
    }

    val data = presidents.switchMap {
        liveData(Dispatchers.IO) {
            emit(repository.getPresident(it))
        }
    }
}