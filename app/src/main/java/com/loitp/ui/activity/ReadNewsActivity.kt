package com.loitp.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LAppResource
import com.core.utilities.LImageUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.rss.RssItem
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.views.LWebView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_layout_read_news.*
import java.util.concurrent.TimeUnit

@LogTag("ReadNewsActivity")
@IsFullScreen(false)
@IsShowAdWhenExit(true)
class ReadNewsActivity : BaseFontActivity() {

    companion object {
        private const val KEY_RSS_ITEM = "KEY_RSS_ITEM"
        private const val TIME_IN_S_TO_GET_MONEY = 20

        fun startActivity(
                context: Context,
                transformationLayout: TransformationLayout,
                rssItem: RssItem
        ) {
            val intent = Intent(context, ReadNewsActivity::class.java)
            intent.putExtra(KEY_RSS_ITEM, rssItem)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }

    private var rssItem: RssItem? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_read_news
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        rssItem = intent?.getSerializableExtra(KEY_RSS_ITEM) as RssItem?
//        logD("onCreate rssItem " + BaseApplication.gson.toJson(rssItem))

        setupViews()
        getMoney()
    }

    private fun setupViews() {
        rssItem?.let { item ->
            LImageUtil.load(context = this, any = item.image, imageView = ivBkg)
            collapsingToolbarLayout.title = item.title
        }

        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)

        toolbar.navigationIcon = LAppResource.getDrawable(R.drawable.ic_keyboard_backspace_white_48dp)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val isDarkMode = LUIUtil.isDarkTheme()
        try {
            webView.setDarkMode(isDarkMode)
        } catch (e: Exception) {
            showSnackBarError("setOnCheckedChangeListener $e")
        }
        webView.callback = object : LWebView.Callback {
            override fun onScroll(l: Int, t: Int, oldl: Int, oldt: Int) {
            }

            override fun onScrollTopToBottom() {
//                logD("onScrollTopToBottom")
            }

            override fun onScrollBottomToTop() {
//                logD("onScrollBottomToTop")
            }

            override fun onProgressChanged(progress: Int) {
//                logD("onProgressChanged $progress")
                pb.progress = progress
                if (progress == 100) {
                    pb.visibility = View.GONE
                    webView.visibility = View.VISIBLE
                    collapsingToolbarLayout.title = webView.title
                } else {
                    pb.visibility = View.VISIBLE
                    webView.visibility = View.GONE
                }
            }

            override fun shouldOverrideUrlLoading(url: String) {

            }
        }
        rssItem?.link?.let {
            webView.loadUrl(it)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun getMoney() {
        compositeDisposable.add(observable
                .subscribeOn(Schedulers.io()) // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer))
    }

    private val observable: Observable<out Long>
        get() = Observable.interval(0, 1, TimeUnit.SECONDS)

    private val observer: DisposableObserver<Long?>
        get() = object : DisposableObserver<Long?>() {
            @SuppressLint("SetTextI18n")
            override fun onNext(value: Long) {
                logD("\nonNext : value : $value")
                if (value >= TIME_IN_S_TO_GET_MONEY) {
                    tvLoadingMoney.visibility = View.GONE
                    Cons.addMoney()
                    showLongInformation(getString(R.string.get_50_money))
                    dispose()
                } else {
                    tvLoadingMoney.visibility = View.VISIBLE
                    tvLoadingMoney.text = "Còn " + (TIME_IN_S_TO_GET_MONEY - value) + "s để nhận thưởng"
                }
            }

            override fun onError(e: Throwable) {
                logD("\nonError : ${e.message}")
            }

            override fun onComplete() {
                logD("\nonComplete")
            }
        }

}
