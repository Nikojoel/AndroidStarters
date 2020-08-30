package com.example.helloworld2
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object ApiModel {
    private const val URL = "https://en.wikipedia.org/w/"

    object Model {
        data class QueryObject(val query: Query?)
        data class Query(val searchinfo: Total?)
        data class Total(val totalhits: Int?)
    }

    interface Service {
        @GET("api.php?action=query&format=json&list=search&")
        suspend fun presidentName(@Query("srsearch") name: String): Model.QueryObject
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val service: Service = retrofit.create(Service::class.java)
}