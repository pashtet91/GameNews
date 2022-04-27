package com.example.gamenews.repository

import com.example.gamenews.model.NewsService

class NewsRepo(private val newsService: NewsService) {

    suspend fun getAllNews() =
        newsService.getAllNews()

    suspend fun searchByTitle(title:String) =
        newsService.getNewsByTitle(title)

}