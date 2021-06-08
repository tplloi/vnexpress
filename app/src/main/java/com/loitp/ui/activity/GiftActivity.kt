package com.loitp.ui.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.annotation.IsFullScreen
import com.annotation.IsShowAdWhenExit
import com.annotation.LogTag
import com.core.base.BaseFontActivity
import com.core.common.Constants
import com.core.utilities.LDateUtil
import com.core.utilities.LDialogUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.loitp.model.History
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.transformationlayout.onTransformationEndContainer
import com.skydoves.transformationlayout.onTransformationStartContainer
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_layout_gift.*
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

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

    private var previousTime = SystemClock.elapsedRealtime()

    override fun setLayoutResourceId(): Int {
        return R.layout.activity_layout_gift
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        onTransformationEndContainer(intent.getParcelableExtra(Constants.activityTransitionName))
        super.onCreate(savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        collapsingToolbarLayout.title = getString(R.string.my_gift)
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE)
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE)
        ivLeft.setSafeOnClickListener {
            onBackPressed()
        }
        ivRight.setSafeOnClickListener {
            val now = SystemClock.elapsedRealtime()
            if (now - previousTime >= layoutTransformation.duration) {
                GiftHistoryActivity.startActivity(
                    context = this,
                    transformationLayout = layoutTransformation
                )
                previousTime = now
            }
        }
        tvInformation.text = Cons.getCurrentMoneyInString()

        LUIUtil.setSafeOnClickListenerElastic(view = btCard50, runnable = Runnable {
            handleChange(CARD_TYPE_50)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btCard200, runnable = Runnable {
            handleChange(CARD_TYPE_200)
        })
        LUIUtil.setSafeOnClickListenerElastic(view = btCard500, runnable = Runnable {
            handleChange(CARD_TYPE_500)
        })

        btConfirmPhone.setSafeOnClickListener {
            handleButtonConfirmPhone()
        }
    }

    override fun onResume() {
        super.onResume()
        checkToShowHistoryButton()
    }

    private fun checkToShowHistoryButton() {
        if (Cons.getListHistory().isNullOrEmpty()) {
            ivRight.visibility = View.GONE
        } else {
            ivRight.visibility = View.VISIBLE
        }
    }

    private fun handleChange(cardType: String) {
        cardViewPhone.visibility = View.GONE
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
                if (currentMoney >= BigDecimal(200000)) {
                    LDialogUtil.showDialog2(
                        context = this,
                        title = getString(
                            R.string.warning_vn
                        ),
                        msg = "Bạn có muốn đổi thẻ cào 200.000VND không?",
                        button1 = getString(R.string.yes),
                        button2 = getString(R.string.no),
                        onClickButton1 = {
                            showDialogProgress()
                            LUIUtil.setDelay(10000, Runnable {
                                hideDialogProgress()
                                showShortError("Đã hết thẻ cào mệnh giá 200.000, vui lòng chọn phần thưởng khác")
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
            CARD_TYPE_500 -> {
                if (currentMoney >= BigDecimal(500000)) {
                    LDialogUtil.showDialog2(
                        context = this,
                        title = getString(
                            R.string.warning_vn
                        ),
                        msg = "Bạn có muốn đổi thẻ cào 500.000VND không?",
                        button1 = getString(R.string.yes),
                        button2 = getString(R.string.no),
                        onClickButton1 = {
                            cardViewPhone.visibility = View.VISIBLE
                        },
                        onClickButton2 = {
                            //do nothing
                        }
                    )
                } else {
                    showShortError(getString(R.string.not_valid_money))
                }
            }
        }

    }

    private fun handleButtonConfirmPhone() {
        val phone = etPhone.text.toString().trim()
        if (phone.isEmpty() || phone.length < 10) {
            showShortError("Số điện thoại không hợp lệ")
        } else {
            showDialogProgress()
            LUIUtil.setDelay(5000, Runnable {
                cardViewPhone.visibility = View.GONE
                Cons.minusMoney(500000.0)

                val c = Calendar.getInstance()
                val df = SimpleDateFormat("HH:mm:ss dd-MM-yyyy", Locale.getDefault())
                val date = df.format(c.time)

                Cons.addHistory(
                    History(
                        date = date,
                        money = "500.000",
                        phone = phone
                    )
                )

                tvInformation.text = Cons.getCurrentMoneyInString()
                etPhone.setText("")
                hideDialogProgress()
                showShortInformation("Đã nạp thẻ thành công")
                checkToShowHistoryButton()
            })
        }
    }
}
