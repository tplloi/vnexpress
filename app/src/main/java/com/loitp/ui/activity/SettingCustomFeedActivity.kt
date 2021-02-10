package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LSharedPrefsUtil
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemDragListener
import com.google.gson.reflect.TypeToken
import com.loitp.R
import com.loitp.adapter.DragDropAdapter
import com.loitp.model.Feed
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import kotlinx.android.synthetic.main.activity_layout_setting_feed.*

@LogTag("SettingCustomFeedActivity")
@IsFullScreen(false)
@IsShowAdWhenExit(false)
class SettingCustomFeedActivity : BaseFontActivity() {

    companion object {
        fun startActivity(
                context: Context,
                transformationLayout: TransformationLayout
        ) {
            val intent = Intent(context, SettingCustomFeedActivity::class.java)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }

    private var dragDropAdapter: DragDropAdapter? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_setting_feed
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setData(): ArrayList<Feed> {
        val dataSet = ArrayList<Feed>()
        dataSet.apply {
            add(Feed(title = getString(R.string.lasted_news), url = "https://vnexpress.net/rss/tin-moi-nhat.rss"))
            add(Feed(title = getString(R.string.hot_news), url = "https://vnexpress.net/rss/tin-noi-bat.rss"))
            add(Feed(title = getString(R.string.education), url = "https://vnexpress.net/rss/giao-duc.rss"))
            add(Feed(title = getString(R.string.law), url = "https://vnexpress.net/rss/phap-luat.rss"))
            add(Feed(title = getString(R.string.sport), url = "https://vnexpress.net/rss/the-thao.rss"))
            add(Feed(title = getString(R.string.entertaiment), url = "https://vnexpress.net/rss/giai-tri.rss"))
            add(Feed(title = getString(R.string.startup), url = "https://vnexpress.net/rss/startup.rss"))
            add(Feed(title = getString(R.string.business), url = "https://vnexpress.net/rss/kinh-doanh.rss"))
            add(Feed(title = getString(R.string.news), url = "https://vnexpress.net/rss/thoi-su.rss"))
            add(Feed(title = getString(R.string.global), url = "https://vnexpress.net/rss/the-gioi.rss"))
            add(Feed(title = getString(R.string.most_viewed_news), url = "https://vnexpress.net/rss/tin-xem-nhieu.rss"))
            add(Feed(title = getString(R.string.fun_news), url = "https://vnexpress.net/rss/cuoi.rss"))
            add(Feed(title = getString(R.string.chat_news), url = "https://vnexpress.net/rss/tam-su.rss"))
            add(Feed(title = getString(R.string.idea), url = "https://vnexpress.net/rss/y-kien.rss"))
            add(Feed(title = getString(R.string.car_bike), url = "https://vnexpress.net/rss/oto-xe-may.rss"))
            add(Feed(title = getString(R.string.technical), url = "https://vnexpress.net/rss/so-hoa.rss"))
            add(Feed(title = getString(R.string.science), url = "https://vnexpress.net/rss/khoa-hoc.rss"))
            add(Feed(title = getString(R.string.travel), url = "https://vnexpress.net/rss/du-lich.rss"))
            add(Feed(title = getString(R.string.family), url = "https://vnexpress.net/rss/gia-dinh.rss"))
            add(Feed(title = getString(R.string.healthy), url = "https://vnexpress.net/rss/suc-khoe.rss"))
        }
        return dataSet
    }

    private fun setupViews() {
        dragDropAdapter = DragDropAdapter(setData())

        dragDropSwipeRecyclerView.layoutManager = LinearLayoutManager(this)//list
        dragDropSwipeRecyclerView.adapter = dragDropAdapter
        dragDropSwipeRecyclerView.orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
        dragDropSwipeRecyclerView.dragListener = object : OnItemDragListener<Feed> {
            override fun onItemDragged(previousPosition: Int, newPosition: Int, item: Feed) {
                // Handle action of item being dragged from one position to another
            }

            override fun onItemDropped(initialPosition: Int, finalPosition: Int, item: Feed) {
                // Handle action of item dropped
                logD("onItemDragListener onItemDragged initialPosition $initialPosition, finalPosition $finalPosition, item $item")
                logD("onItemDragListener" + BaseApplication.gson.toJson(dragDropAdapter?.dataSet))
            }
        }
    }

    fun a() {
//        LSharedPrefsUtil.instance.putObjectList(KEY_LIST_OBJECT, list)

//        val type = object : TypeToken<List<User>>() {
//        }.type
//        val value: ArrayList<User> = LSharedPrefsUtil.instance.getObjectList(KEY_LIST_OBJECT, type)
    }
}
