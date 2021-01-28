package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LAppResource
import com.core.utilities.LImageUtil
import com.loitp.R
import com.rss.RssItem
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import kotlinx.android.synthetic.main.activity_layout_read_news.*

@LogTag("loitppReadNewsActivity")
@IsFullScreen(false)
class ReadNewsActivity : BaseFontActivity() {

    companion object {
        private const val KEY_RSS_ITEM = "KEY_RSS_ITEM"

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
    }
}
