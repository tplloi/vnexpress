package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.annotation.IsFullScreen
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LImageUtil
import com.loitp.R
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import kotlinx.android.synthetic.main.activity_layout_transformation_detail.*

@LogTag("DetailActivity")
@IsFullScreen(false)
class TransformationDetailActivity : BaseFontActivity() {

    companion object {
        fun startActivity(
                context: Context,
                transformationLayout: TransformationLayout
        ) {
            val intent = Intent(context, TransformationDetailActivity::class.java)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_transformation_detail
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        LImageUtil.load(context = this, any = "https://kenh14cdn.com/thumb_w/620/203336854389633024/2021/1/27/r18-1611735688832344293553.jpeg", imageView = ivProfileDetailBackground)
        tvDetailTitle.text = "AAAAAAAAAAAAAAAAAAA"
        tvDetailDescription.text = "BBBBBBBBBBBBBBBBBBBBBBBB"
    }
}
