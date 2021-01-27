package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.adapter.RssItemsAdapter
import com.loitp.viewmodels.MainViewModel
import com.rss.RssItem
import kotlinx.android.synthetic.main.frm_home.*

@LogTag("loitppHomeFragment")
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

        fetchRss()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        mAdapter = RssItemsAdapter { rssItem ->
            //TODO
        }
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = mAdapter
        swRefresh.setOnRefreshListener(this)
    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.eventLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                logD("eventLoading isLoading $isLoading")
                if (isLoading) {
                    showLoading()
                } else {
                    hideLoading()
                }
            })
            mvm.eventErrorMessage.observe(viewLifecycleOwner, Observer { msg ->
                logE("eventErrorMessage observe $msg")
                msg?.let {
                    recyclerView.visibility = View.GONE
                    tvNoData.visibility = View.VISIBLE
                }

            })
            mvm.listRssItemLiveData.observe(viewLifecycleOwner, Observer { listRssItem ->
//                logD("<<<listRssItemLiveData " + BaseApplication.gson.toJson(listRssItem))
                onRssItemsLoaded(rssItems = listRssItem)
            })
        }

    }

    private fun fetchRss() {
        mainViewModel?.loadDataRss(feedUrl)
    }

    private fun onRssItemsLoaded(rssItems: List<RssItem>) {
        mAdapter?.setItems(rssItems)
        if (recyclerView.visibility != View.VISIBLE) {
            recyclerView.visibility = View.VISIBLE
        }
        tvNoData.visibility = View.GONE
    }

    private fun showLoading() {
        swRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        swRefresh.isRefreshing = false
    }

    override fun onRefresh() {
        fetchRss()
    }
}
