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
import com.loitp.constant.Cons
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
        showLongInformation(msg = getString(R.string.drag_feed), isTopAnchor = false)
    }

    private fun setupViews() {
        dragDropAdapter = DragDropAdapter(dataSet = Cons.getListFeed())

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

                dragDropAdapter?.dataSet?.let {
                    Cons.saveListFeed(listFeed = it)
                }
            }
        }
    }

}
