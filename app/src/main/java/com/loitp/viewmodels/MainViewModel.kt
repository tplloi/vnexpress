package com.loitp.viewmodels

import androidx.lifecycle.MutableLiveData
import com.annotation.LogTag
import com.core.base.BaseViewModel
import com.loitp.model.Feed
import com.loitp.model.NewsFeed
import com.loitp.service.RssService
import com.rss.RssConverterFactory
import com.rss.RssFeed
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

            val service = retrofit.create(RssService::class.java)
            feed.url.let { url ->
                service.getRss(url)
                        .enqueue(object : Callback<RssFeed> {
                            override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                                val listRssItem = response.body()?.items ?: emptyList()

                                //TODO room
                                val listNewsFeed = ArrayList<NewsFeed>()
                                listRssItem.forEach {
                                    val newsFeed = NewsFeed(
                                            title = it.title ?: "",
                                            link = it.link ?: "",
                                            image = it.image ?: "",
                                            publishDate = it.publishDate ?: "",
                                            description = it.description ?: "",
                                            feedType = feed.title
                                    )
                                    listNewsFeed.add(newsFeed)
                                }

                                listNewsFeedLiveData.postValue(listNewsFeed)
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

//    fun updateComic(comic: Comic) {
//        logD(">>>updateComic ${comic.title}")
//        ioScope.launch {
//            TTTDatabase.instance?.tttDao()?.update(type = comic)
//        }
//    }
}
