package com.example.gamenews.model

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("/?data")
    suspend fun getAllNews(): Response<List<News>>


    @GET("/?search")
    suspend fun getNewsByTitle(@Query("title") term: String) :
            Response<List<News>>//NewsResponse>>

    companion object {
        val instance: NewsService by lazy{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://188.40.167.45:3001")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(NewsService::class.java)
        }
    }
}