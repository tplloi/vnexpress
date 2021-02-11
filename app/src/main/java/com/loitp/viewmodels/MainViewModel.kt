package com.loitp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.annotation.LogTag
import com.core.base.BaseViewModel
import com.core.helper.ttt.db.TTTDatabase
import com.google.ads.interactivemedia.v3.internal.it
import com.loitp.db.AppDatabase
import com.loitp.model.Feed
import com.loitp.model.NewsFeed
import com.loitp.service.RssService
import com.rss.RssConverterFactory
import com.rss.RssFeed
import com.rss.RssItem
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

@LogTag("MainViewModel")
class MainViewModel : BaseViewModel() {

    val listNewsFeedLiveData: MutableLiveData<List<NewsFeed>> = MutableLiveData()

    fun loadDataRss(feed: Feed) {
        ioScope.launch {
            logD(">>>loadDataRss urlRss ${feed.url}")
            showLoading(true)
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://github.com")
                    .addConverterFactory(RssConverterFactory.create())
                    .build()


            fun handleResponse(listRssItem: List<RssItem>) {
                ioScope.launch {
                    val feedType = feed.title
                    val listNewsFeed = ArrayList<NewsFeed>()
                    listRssItem.forEach {
                        val newsFeed = NewsFeed(
                                title = it.title ?: "",
                                link = it.link ?: "",
                                image = it.image ?: "",
                                publishDate = it.publishDate ?: "",
                                description = it.description ?: "",
                                feedType = feedType
                        )
                        listNewsFeed.add(newsFeed)
                    }

                    //save new data to db
                    AppDatabase.instance?.appDao()?.insertListNewsFeed(list = listNewsFeed)

                    //get data from db
                    //TODO phan trang
                    val offlineListNewsFeed = AppDatabase.instance?.appDao()?.getListNewsFeed(feedType = feedType)

                    listNewsFeedLiveData.postValue(offlineListNewsFeed)
                }
            }

            val service = retrofit.create(RssService::class.java)
            feed.url.let { url ->
                service.getRss(url)
                        .enqueue(object : Callback<RssFeed> {
                            override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                                handleResponse(listRssItem = response.body()?.items ?: emptyList())
                                showLoading(false)
                            }

                            override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                                setErrorMessage("Failed to fetchRss RSS feed!")
                                showLoading(false)
                            }
                        })
            }
        }
    }

    fun deleteDb() {
        ioScope.launch {
            AppDatabase.instance?.appDao()?.deleteAll()
        }
    }
}
