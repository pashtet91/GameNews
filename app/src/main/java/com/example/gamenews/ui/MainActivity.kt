package com.example.gamenews.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamenews.R
import com.example.gamenews.adapter.NewsListAdapter
import com.example.gamenews.databinding.ActivityMainBinding
import com.example.gamenews.model.NewsService
import com.example.gamenews.repository.NewsRepo
import com.example.gamenews.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private val TAG = javaClass.simpleName
    private var topNewsAreShowing: Boolean = false

    private lateinit var newsListAdapter: NewsListAdapter

    lateinit var binding:ActivityMainBinding
    lateinit var results: List<MainViewModel.NewsSummaryViewData>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        setupViewModels()
        updateControls()
        hideTopNewsButton()
        performSearch()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_search, menu)

        val searchMenuItem = menu.findItem(R.id.search_item)
        val searchView = searchMenuItem?.actionView  as SearchView

        val searchManager = getSystemService(Context.SEARCH_SERVICE)
            as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    private fun setupToolbar(){
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.toolbar_title);
    }

    private fun setupViewModels(){
        val service = NewsService.instance
        mainViewModel.newsRepo = NewsRepo(service)
    }

    private fun updateControls(){
        binding.newsRecyclerView.setHasFixedSize(true)

        val layoutManager = LinearLayoutManager(this)
        binding.newsRecyclerView.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            binding.newsRecyclerView.context,
            layoutManager.orientation
        )

        binding.newsRecyclerView.addItemDecoration(dividerItemDecoration)

        newsListAdapter = NewsListAdapter(null, this)
        binding.newsRecyclerView.adapter = newsListAdapter
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun hideTopNewsButton(){
        topNewsButtonConnectListener()
        binding.topNewsButton.visibility = View.INVISIBLE
    }

    private fun showTopNewsButton(){
        binding.topNewsButton.visibility = View.VISIBLE
    }

    private fun topNewsButtonConnectListener()
    {
        binding.topNewsButton.setOnClickListener {
            if (!topNewsAreShowing)
                performSearch("", true)
            else
                performSearch()

            topNewsAreShowing = !topNewsAreShowing
        }
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

    private fun performSearch(title:String = "", findTopNewsOnly:Boolean = false){
        showProgressBar()

        val newsService = NewsService.instance
        val newsRepo = NewsRepo(newsService)
        val titleIsEmpty = title.isEmpty()

        GlobalScope.launch {
            if(!findTopNewsOnly)
                if(titleIsEmpty)
                    results = mainViewModel.searchNews()
                else
                    results = mainViewModel.searchNews(title)
            else
                results = mainViewModel.searchTopNewsOnly()

            withContext(Dispatchers.Main){
                hideProgressBar()
                binding.toolbar.title = if (titleIsEmpty) getString(R.string.toolbar_title) else title
                newsListAdapter.setSearchData(results)
                if(!results.isEmpty())
                    showTopNewsButton()
            }

            Log.i(TAG, "Results = $results")
        }
    }
}