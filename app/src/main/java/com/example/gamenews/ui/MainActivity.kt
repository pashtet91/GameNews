package com.example.gamenews.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.example.gamenews.R
import com.example.gamenews.model.News
import com.example.gamenews.model.NewsService
import com.example.gamenews.repository.NewsRepo
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    val TAG = javaClass.simpleName

    lateinit var results: Response<List<News>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        performSearch()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchMenuItem = menu?.findItem(R.id.search_item)
        val searchView = searchMenuItem?.actionView  as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE)
            as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent){
        if(Intent.ACTION_SEARCH == intent.action){
            val query = intent.getStringExtra(SearchManager.QUERY) ?:
            return
            performSearch(query)
        }
    }

    private fun performSearch(title:String = ""){
        val newsService = NewsService.instance
        val newsRepo = NewsRepo(newsService)


        GlobalScope.launch {
            if(title.isEmpty())
                results = newsRepo.getAllNews()
            else
                results = newsRepo.searchByTitle(title)

            Log.i(TAG, "Results = ${results.body()}")
        }
    }
}