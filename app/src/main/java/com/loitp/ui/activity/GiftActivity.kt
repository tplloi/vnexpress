package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LAppResource
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import kotlinx.android.synthetic.main.activity_layout_gift.*
import java.math.BigDecimal

@LogTag("GiftActivity")
@IsFullScreen(true)
@IsShowAdWhenExit(false)
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
        collapsingToolbarLayout.title = getString(R.string.app_name)
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
        toolbar.navigationIcon = LAppResource.getDrawable(R.drawable.ic_keyboard_backspace_white_48dp)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
        tvInformation.text = Cons.getCurrentMoneyInString()

        LUIUtil.setSafeOnClickListenerElastic(view = btViettel50, runnable = Runnable {
            handleChange()
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btViettel200, runnable = Runnable {
            handleChange()
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btViettel500, runnable = Runnable {
            handleChange()
        })

        LUIUtil.setSafeOnClickListenerElastic(view = btMobifone50, runnable = Runnable {
            handleChange()
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btMobifone200, runnable = Runnable {
            handleChange()
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btMobifone500, runnable = Runnable {
            handleChange()
        })

        LUIUtil.setSafeOnClickListenerElastic(view = btVinaphone50, runnable = Runnable {
            handleChange()
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btVinaphone200, runnable = Runnable {
            handleChange()
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btVinaphone500, runnable = Runnable {
            handleChange()
        })
    }

    private fun handleChange() {
        val currentMoney = Cons.getCurrentMoneyInBigDecimal()
        if (currentMoney < BigDecimal(50000)) {
            showShortError(getString(R.string.not_valid_money))
        } else {
            showDialogProgress()
            LUIUtil.setDelay(30000, Runnable {
                hideDialogProgress()
                showShortError(getString(R.string.cannot_change_money))
            })
        }
    }
}
