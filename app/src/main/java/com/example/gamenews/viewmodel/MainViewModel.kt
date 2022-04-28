package com.example.gamenews.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gamenews.model.News
import com.example.gamenews.repository.NewsRepo
import retrofit2.Response

class MainViewModel(application: Application) :
    AndroidViewModel(application){
        var newsRepo: NewsRepo? = null


    suspend fun searchNews(title:String = ""):
        List<NewsSummaryViewData>{

        val results =  if (title.isEmpty())  newsRepo?.getAllNews() else  newsRepo?.searchByTitle(title)

//        if(results != null && results.isSuccessful){
//            val news = results.body()
//            if(!news.isNullOrEmpty()){
//                return news.map{n->
//                    newsToNewsSummaryView(n)
//                }
//            }
//        }
//        return emptyList()
        return handleResults(results)
    }

    suspend fun searchTopNewsOnly(): List<NewsSummaryViewData>{

        val results =  newsRepo?.getAllNews()

        val filteredResults = ArrayList<News>()
        results?.body()?.iterator()?.forEach { i ->
            if(i.top > 0)
                filteredResults.add(i)
        }

        return handleResults(results)
    }

    suspend fun handleResults(results: Response<List<News>>?): List<NewsSummaryViewData>{
        if(results != null && results.isSuccessful){
            val news = results.body()
            if(!news.isNullOrEmpty()){
                return news.map{n->
                    newsToNewsSummaryView(n)
                }
            }
        }
        return emptyList()
    }

    private fun newsToNewsSummaryView(
        news: News
    ): NewsSummaryViewData{
        return NewsSummaryViewData(
            news.title,
            news.type,
            news.img,
            news.click_url,
            news.time,
            news.top
        )
    }

    data class NewsSummaryViewData(
        val title: String? = "",
        val type: String? = "",
        val img: String? = "",
        val click_url: String? = "",
        val time: String? = "",
        val top: Int? = 0)
}