package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.loitp.R
import com.loitp.constant.Cons
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import kotlinx.android.synthetic.main.activity_layout_gift.*

@LogTag("GiftActivity")
@IsFullScreen(false)
@IsShowAdWhenExit(true)
class GiftActivity : BaseFontActivity() {

    companion object {
        fun startActivity(
                context: Context,
                transformationLayout: TransformationLayout
        ) {
            val intent = Intent(context, GiftActivity::class.java)
            TransformationCompat.startActivity(transformationLayout, intent)
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_gift
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        tvInformation.text = Cons.getCurrentMoneyInString()
    }

}
