package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.loitp.R
import com.loitp.adapter.HistoryAdapter
import com.loitp.constant.Cons
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_layout_gift_history.*

@LogTag("GiftHistoryActivity")
@IsFullScreen(false)
@IsShowAdWhenExit(true)
class GiftHistoryActivity : BaseFontActivity() {
    companion object {

        fun startActivity(
            context: Context,
            transformationLayout: TransformationLayout
        ) {
            val intent = Intent(context, GiftHistoryActivity::class.java)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }

    private val concatAdapter = ConcatAdapter()
    private var historyAdapter: HistoryAdapter? = null

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_gift_history
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        setupViews()
        setData()
    }

    private fun setupViews() {
        collapsingToolbarLayout.title = getString(R.string.history)
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
        ivLeft.setSafeOnClickListener {
            onBackPressed()
        }
        historyAdapter = HistoryAdapter { history ->
            //do nothing
        }
        historyAdapter?.let {
            concatAdapter.addAdapter(it)
        }
        val gridLayoutManager = GridLayoutManager(this, 1)
        rvHistory.layoutManager = gridLayoutManager
        rvHistory.adapter = concatAdapter
    }

    private fun setData() {
        val listHistory = Cons.getListHistory()
        historyAdapter?.setItems(listHistory)
    }

}
