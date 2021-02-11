package com.loitp.ui.fragment

import android.os.Bundle
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.core.utilities.LSharedPrefsUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.adapter.LoadMoreAdapter
import com.loitp.adapter.RssItemsAdapter
import com.loitp.constant.Cons
import com.loitp.model.Feed
import com.loitp.model.NewsFeed
import com.loitp.ui.activity.ReadNewsActivity
import com.loitp.viewmodels.MainViewModel
import com.rss.RssItem
import kotlinx.android.synthetic.main.frm_page.*

@LogTag("PageFragment")
class PageFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val KEY_FEED = "KEY_FEED"
    }

    private var mainViewModel: MainViewModel? = null
    private val concatAdapter = ConcatAdapter()
    private var rssItemsAdapter: RssItemsAdapter? = null
    private var loadMoreAdapter = LoadMoreAdapter()
    private var previousTime = SystemClock.elapsedRealtime()
    private var feed: Feed? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            feed = it.getSerializable(KEY_FEED) as Feed?
        }

        setupViews()
        setupViewModels()

        logD("onViewCreated")
        fetchRss()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_page
    }

    private fun setupViews() {
        rssItemsAdapter = RssItemsAdapter { newsFeed, layoutItemRssTransformation ->
            context?.let { c ->
                val now = SystemClock.elapsedRealtime()
                if (now - previousTime >= layoutItemRssTransformation.duration) {
                    ReadNewsActivity.startActivity(context = c, transformationLayout = layoutItemRssTransformation, newsFeed = newsFeed)
                    previousTime = now
                }
            }
        }
        rssItemsAdapter?.let {
            concatAdapter.addAdapter(it)
        }

        val isGridView = LSharedPrefsUtil.instance.getBoolean(Cons.IS_GRID_VIEW, false)
        val spanCount = if (isGridView) {
            2
        } else {
            1
        }
        val gridLayoutManager = GridLayoutManager(context, spanCount)
        if (isGridView) {
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == concatAdapter.itemCount - 1) {
                        if (isContainLoadMoreAdapter()) {
                            2
                        } else {
                            1
                        }
                    } else {
                        1
                    }
                }
            }
        }
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = concatAdapter
        LUIUtil.setScrollChange(
                recyclerView = recyclerView,
                onTop = {
                },
                onBottom = {
                    logD("onBottom")
                    val isContainLoadMoreAdapter = isContainLoadMoreAdapter()
                    logD("isContainLoadMoreAdapter $isContainLoadMoreAdapter")
                    if (!isContainLoadMoreAdapter) {
                        concatAdapter.addAdapter(loadMoreAdapter)
                    }
                    concatAdapter.itemCount.let {
                        recyclerView.scrollToPosition(it - 1)
                    }

                    //TODO scroll next page
//                    if (currentIndex < (listFeed.size - 1)) {
//                        logD("if")
//                        currentIndex++
//                        fetchRss()
//                    } else {
//                        logD("else")
//                        concatAdapter.removeAdapter(loadMoreAdapter)
//                        showSnackBarInfor("Bạn đã cuộn đến trang cuối")
//                    }
                },
                onScrolled = {
                }
        )
        swRefresh.setOnRefreshListener(this)
    }

    private fun isContainLoadMoreAdapter(): Boolean {
        var isContainLoadMoreAdapter = false
        concatAdapter.adapters.forEach {
            logD(">>> " + it.javaClass.simpleName + "~" + LoadMoreAdapter::class.java.simpleName)
            if (it.javaClass.simpleName == LoadMoreAdapter::class.java.simpleName) {
                isContainLoadMoreAdapter = true
            }
        }
        return isContainLoadMoreAdapter
    }

    private fun setupViewModels() {
        mainViewModel = getSelfViewModel(MainViewModel::class.java)
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
            mvm.listNewsFeedLiveData.observe(viewLifecycleOwner, Observer { listNewsFeed ->
//                logD("<<<listRssItemLiveData " + BaseApplication.gson.toJson(listNewsFeed))
                onRssItemsLoaded(listNewsFeed = listNewsFeed)
            })
        }

    }

    private fun fetchRss() {
        logD("fetchRss feed " + BaseApplication.gson.toJson(feed))
        feed?.let {
            mainViewModel?.loadDataRss(feed = it)
        }
    }

    private fun onRssItemsLoaded(listNewsFeed: List<NewsFeed>) {
        concatAdapter.removeAdapter(loadMoreAdapter)
        rssItemsAdapter?.setItems(listNewsFeed)
        if (recyclerView.visibility != View.VISIBLE) {
            recyclerView.visibility = View.VISIBLE
        }
        tvNoData.visibility = View.GONE
        animationView.visibility = View.GONE
    }

    private fun showLoading() {
        swRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        swRefresh.isRefreshing = false
    }

    override fun onRefresh() {
        //TODO refresh
//        fetchRss()
    }
}
