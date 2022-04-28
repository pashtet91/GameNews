package com.example.gamenews.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamenews.databinding.NewsItemBinding
import com.example.gamenews.viewmodel.MainViewModel

class NewsListAdapter(
    private var newsSummaryViewList: List<MainViewModel.NewsSummaryViewData>?,
    private val parentActivity: Activity): RecyclerView.Adapter<NewsListAdapter.ViewHolder>() {

//    private var newsFilterList = ArrayList<MainViewModel.NewsSummaryViewData>()
//
//    init{
//        newsFilterList = newsSummaryViewList as ArrayList<MainViewModel.NewsSummaryViewData>
//    }


    inner class ViewHolder(
        databinding: NewsItemBinding,
    ) : RecyclerView.ViewHolder(databinding.root){

        var newsSummaryViewData: MainViewModel.NewsSummaryViewData? = null

        var newsImage: ImageView = databinding.newsImage
        var newsTitleTextView: TextView = databinding.newsTitleTextView
        var clickUrlTextView: TextView = databinding.clickUrlTextView
        var timeTextView: TextView = databinding.timeTextView


    }

    fun setSearchData(newsSummaryViewData: List<MainViewModel.NewsSummaryViewData>){
        newsSummaryViewList = newsSummaryViewData
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent:ViewGroup,
        viewType: Int
    ): NewsListAdapter.ViewHolder{
        return ViewHolder(NewsItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchViewList = newsSummaryViewList ?: return
        val searchView = searchViewList[position]
        holder.newsSummaryViewData = searchView

        Glide.with(parentActivity)
            .load(searchView.img)
            .into(holder.newsImage)

        holder.newsTitleTextView.text = searchView.title
        holder.clickUrlTextView.text = searchView.click_url
        holder.timeTextView.text = searchView.time

    }


    override fun getItemCount(): Int {
        return newsSummaryViewList?.size ?: 0
    }

//    override fun getFilter(): Filter {
//        return object : Filter() {
//            override fun performFiltering(constraint: CharSequence?): FilterResults {
//                val charSearch = constraint.toString()
//                if(!charSearch.isEmpty()) {
//                    val resultList = ArrayList<MainViewModel.NewsSummaryViewData>()
//
//                        for(row in newsSummaryViewList){
//                            if(row)
//
//                            resultList.add(row)
//                        }
//
//                }
//
//            }
//
//            @Suppress("UNCHECKED_CAST")
//            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//
//            }
//    }


}