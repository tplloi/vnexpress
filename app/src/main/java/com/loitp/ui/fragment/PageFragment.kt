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
import com.loitp.adapter.NewsFeedAdapter
import com.loitp.constant.Cons
import com.loitp.model.Feed
import com.loitp.model.NewsFeed
import com.loitp.ui.activity.ReadNewsActivity
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_page.*

@LogTag("PageFragment")
class PageFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    companion object {
        const val KEY_FEED = "KEY_FEED"
    }

    private var mainViewModel: MainViewModel? = null
    private val concatAdapter = ConcatAdapter()
    private var newsFeedAdapter: NewsFeedAdapter? = null
    private var loadMoreAdapter = LoadMoreAdapter()
    private var previousTime = SystemClock.elapsedRealtime()
    private var feed: Feed? = null
    private var pageIndex = 0//page dem tu 0
    private var isRefreshAllPage = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            feed = it.getSerializable(KEY_FEED) as Feed?
        }

        setupViews()
        setupViewModels()

        getTotalPage()
    }

    private fun getTotalPage() {
        feed?.let {
            mainViewModel?.getTotalPage(feed = it)
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_page
    }

    private fun setupViews() {
        newsFeedAdapter = NewsFeedAdapter { newsFeed, layoutItemRssTransformation ->
            context?.let { c ->
                val now = SystemClock.elapsedRealtime()
                if (now - previousTime >= layoutItemRssTransformation.duration) {
                    ReadNewsActivity.startActivity(context = c, transformationLayout = layoutItemRssTransformation, newsFeed = newsFeed)
                    previousTime = now
                }
            }
        }
        newsFeedAdapter?.let {
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

                    pageIndex--
                    if (pageIndex >= 0) {
                        fetchRss()
                    } else {
                        showShortInformation(getString(R.string.this_is_last_page))
                        concatAdapter.removeAdapter(loadMoreAdapter)
                    }
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
                    animationView.visibility = View.GONE
                }

            })
            mvm.totalPageLiveData.observe(viewLifecycleOwner, Observer { totalPage ->
                logD("totalPageLiveData observe totalPage $totalPage")
                pageIndex = totalPage - 1
                fetchRss()
            })
            mvm.listNewsFeedLiveData.observe(viewLifecycleOwner, Observer { listNewsFeed ->
//                logD("<<<listRssItemLiveData " + BaseApplication.gson.toJson(listNewsFeed))
//                logD("--------------------------------")
//                listNewsFeed.forEach {
//                    logD("-> " + it.title)
//                }
                onRssItemsLoaded(listNewsFeed = listNewsFeed)
            })
        }

    }

    private fun fetchRss() {
        logD("fetchRss pageIndex $pageIndex, feed " + BaseApplication.gson.toJson(feed))
        feed?.let { f ->
            mainViewModel?.loadDataRss(feed = f, pageIndex = pageIndex)
        }
    }

    private fun onRssItemsLoaded(listNewsFeed: List<NewsFeed>) {
        concatAdapter.removeAdapter(loadMoreAdapter)
        newsFeedAdapter?.setItems(items = listNewsFeed, isRefreshAllPage = isRefreshAllPage)
        if (isRefreshAllPage) {
            isRefreshAllPage = false
        }
        if (recyclerView.visibility != View.VISIBLE) {
            recyclerView.visibility = View.VISIBLE
        }
        if (newsFeedAdapter?.itemCount == 0) {
            tvNoData.visibility = View.VISIBLE
        } else {
            tvNoData.visibility = View.GONE
        }
        animationView.visibility = View.GONE
    }

    private fun showLoading() {
        swRefresh.isRefreshing = true
    }

    private fun hideLoading() {
        swRefresh.isRefreshing = false
    }

    override fun onRefresh() {
        pageIndex = 0
        isRefreshAllPage = true
        getTotalPage()
    }
}
