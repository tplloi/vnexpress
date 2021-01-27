package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.core.utilities.LSocialUtil
import com.loitp.R
import com.loitp.adapter.RssItemsAdapter
import com.loitp.service.RssService
import com.loitp.viewmodels.MainViewModel
import com.rss.RssConverterFactory
import com.rss.RssFeed
import com.rss.RssItem
import kotlinx.android.synthetic.main.frm_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

@LogTag("HomeFragment")
class HomeFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        private const val KEY_FEED = "FEED"

        fun newInstance(feedUrl: String): HomeFragment {
            val rssFragment = HomeFragment()
            val bundle = Bundle()
            bundle.putSerializable(KEY_FEED, feedUrl)
            rssFragment.arguments = bundle
            return rssFragment
        }
    }

    private var mainViewModel: MainViewModel? = null
    private var feedUrl: String? = null
    private var mAdapter: RssItemsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedUrl = arguments?.getString(KEY_FEED)
        setupViews()
        setupViewModels()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        mAdapter = RssItemsAdapter { rssItem ->
            LSocialUtil.openUrlInBrowser(context = context, url = rssItem.link)
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        swRefresh.setOnRefreshListener(this)

        fetchRss()
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.eventLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                if (isLoading) {
//                    indicatorView.smoothToShow()
                } else {
//                    indicatorView.smoothToHide()
                }
            })

            mvm.listChapLiveData.observe(viewLifecycleOwner, Observer { listChap ->
                logD("<<<listChapLiveData " + BaseApplication.gson.toJson(listChap))
            })
        }

    }

    private fun fetchRss() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://github.com")
                .addConverterFactory(RssConverterFactory.create())
                .build()

        showLoading()
        val service = retrofit.create(RssService::class.java)

        feedUrl?.apply {
            service.getRss(this)
                    .enqueue(object : Callback<RssFeed> {
                        override fun onResponse(call: Call<RssFeed>, response: Response<RssFeed>) {
                            response.body()?.items?.let {
                                onRssItemsLoaded(rssItems = it)
                            }
                            hideLoading()
                        }

                        override fun onFailure(call: Call<RssFeed>, t: Throwable) {
                            showSnackBarError(msg = "Failed to fetchRss RSS feed!", isFullWidth = true)
                        }
                    })
        }
    }

    fun onRssItemsLoaded(rssItems: List<RssItem>) {
        mAdapter?.setItems(rssItems)
        if (recyclerView.visibility != View.VISIBLE) {
            recyclerView.visibility = View.VISIBLE
        }
    }

    private fun showLoading() {
        swRefresh.isRefreshing = true
    }

    fun hideLoading() {
        swRefresh.isRefreshing = false
    }

    override fun onRefresh() {
        fetchRss()
    }
}
