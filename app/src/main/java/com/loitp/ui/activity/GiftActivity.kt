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
import com.core.utilities.LDialogUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_layout_gift.*
import java.math.BigDecimal

@LogTag("GiftActivity")
@IsFullScreen(false)
@IsShowAdWhenExit(false)
class GiftActivity : BaseFontActivity() {

    companion object {
        const val CARD_TYPE_50 = "CARD_TYPE_50"
        const val CARD_TYPE_200 = "CARD_TYPE_200"
        const val CARD_TYPE_500 = "CARD_TYPE_500"

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
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
        ivLeft.setSafeOnClickListener {
            onBackPressed()
        }
        tvInformation.text = Cons.getCurrentMoneyInString()

        LUIUtil.setSafeOnClickListenerElastic(view = btViettel50, runnable = Runnable {
            handleChange(CARD_TYPE_50)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btViettel200, runnable = Runnable {
            handleChange(CARD_TYPE_200)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btViettel500, runnable = Runnable {
            handleChange(CARD_TYPE_500)
        })

        LUIUtil.setSafeOnClickListenerElastic(view = btMobifone50, runnable = Runnable {
            handleChange(CARD_TYPE_50)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btMobifone200, runnable = Runnable {
            handleChange(CARD_TYPE_200)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btMobifone500, runnable = Runnable {
            handleChange(CARD_TYPE_500)
        })

        LUIUtil.setSafeOnClickListenerElastic(view = btVinaphone50, runnable = Runnable {
            handleChange(CARD_TYPE_50)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btVinaphone200, runnable = Runnable {
            handleChange(CARD_TYPE_200)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btVinaphone500, runnable = Runnable {
            handleChange(CARD_TYPE_500)
        })
    }

    private fun handleChange(cardType: String) {
        val currentMoney = Cons.getCurrentMoneyInBigDecimal()
        if (currentMoney < BigDecimal(50000)) {
            showShortError(getString(R.string.not_valid_money))
            return
        }
        when (cardType) {
            CARD_TYPE_50 -> {
                if (currentMoney >= BigDecimal(50000)) {
                    LDialogUtil.showDialog2(
                        context = this,
                        title = getString(
                            R.string.warning_vn
                        ),
                        msg = "Bạn có muốn đổi thẻ cào 50.000VND không?",
                        button1 = getString(R.string.yes),
                        button2 = getString(R.string.no),
                        onClickButton1 = {
                            showDialogProgress()
                            LUIUtil.setDelay(3000, Runnable {
                                hideDialogProgress()
                                showShortError("Đã hết thẻ cào mệnh giá 50.000, vui lòng chọn phần thưởng khác")
                            })
                        },
                        onClickButton2 = {
                            //do nothing
                        }
                    )
                } else {
                    showShortError(getString(R.string.not_valid_money))
                }
            }
            CARD_TYPE_200 -> {

            }
            CARD_TYPE_500 -> {

            }
        }

    }
}
