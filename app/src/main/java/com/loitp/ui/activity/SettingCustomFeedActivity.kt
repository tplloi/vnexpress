package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.loitp.R
import com.loitp.adapter.DragDropAdapter
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

    private fun setData(): ArrayList<String> {
        val dataSet = ArrayList<String>()
        for (i in 0..20) {
            dataSet.add(element = "Item $i")
        }
        return dataSet
    }

    private fun setupViews() {
        dragDropAdapter = DragDropAdapter(setData())

        dragDropSwipeRecyclerView.layoutManager = LinearLayoutManager(this)//list
        dragDropSwipeRecyclerView.adapter = dragDropAdapter
        dragDropSwipeRecyclerView.orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
    }
}
