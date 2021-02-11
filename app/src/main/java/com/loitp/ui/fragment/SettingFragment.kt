package com.loitp.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.utilities.LActivityUtil
import com.core.utilities.LDialogUtil
import com.core.utilities.LSharedPrefsUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.loitp.ui.activity.MainActivity
import com.loitp.ui.activity.SettingCustomFeedActivity
import com.loitp.viewmodels.MainViewModel
import com.views.setSafeOnClickListener
import kotlinx.android.synthetic.main.frm_setting.*

@LogTag("SettingFragment")
class SettingFragment : BaseFragment() {

    private var dialog: AlertDialog? = null
    private var previousTime = SystemClock.elapsedRealtime()
    private var mainViewModel: MainViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()
    }

    override fun onDestroyView() {
        dialog?.dismiss()
        super.onDestroyView()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_setting
    }

    private fun setupViews() {
        val isDarkTheme = LUIUtil.isDarkTheme()
        swEnableDarkMode.isChecked = isDarkTheme

        swEnableDarkMode.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchDarkTheme(isChecked = isChecked)
        }

        val isSmallThumb = LSharedPrefsUtil.instance.getBoolean(Cons.IS_SMALL_THUMB, false)
        swSmallSizeItem.isChecked = isSmallThumb
        swSmallSizeItem.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchSmallThumb(isChecked = isChecked)
        }

        val isGridView = LSharedPrefsUtil.instance.getBoolean(Cons.IS_GRID_VIEW, false)
        swIsGridView.isChecked = isGridView
        swIsGridView.setOnCheckedChangeListener { _, isChecked ->
            handleSwitchGridView(isChecked = isChecked)
        }

        btCustomFeed.setSafeOnClickListener {
            context?.let { c ->
                val now = SystemClock.elapsedRealtime()
                if (now - previousTime >= layoutTransformation.duration) {
                    SettingCustomFeedActivity.startActivity(
                            context = c,
                            transformationLayout = layoutTransformation
                    )
                    previousTime = now
                }
            }
        }

        LUIUtil.setSafeOnClickListenerElastic(
                view = btClearDb,
                runnable = Runnable {
                    showBottomSheetOptionFragment(
                            title = getString(R.string.warning_vn),
                            message = getString(R.string.delete_db_msg),
                            textButton1 = getString(R.string.delete),
                            textButton2 = getString(R.string.no),
                            onClickButton1 = {
                                showShortInformation(getString(R.string.delete_successfully))
                                mainViewModel?.deleteDb()
                            },
                            onClickButton2 = {
                                //do nothing
                            }
                    )
                }
        )
    }

    private fun setupViewModels() {
        mainViewModel = getSelfViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            //do sth
        }
    }

    private fun handleSwitchDarkTheme(isChecked: Boolean) {
        context?.let { c ->
            val isDarkTheme = LUIUtil.isDarkTheme()
            if (isDarkTheme == isChecked) {
                return@let
            }

            dialog = LDialogUtil.showDialog2(
                    context = c,
                    title = getString(com.R.string.warning_vn),
                    msg = getString(com.R.string.app_will_be_restarted_vn),
                    button1 = getString(com.R.string.cancel),
                    button2 = getString(com.R.string.ok),
                    onClickButton1 = {
                        swEnableDarkMode?.isChecked = LUIUtil.isDarkTheme()
                    },
                    onClickButton2 = {
                        if (isChecked) {
                            LUIUtil.setDarkTheme(isDarkTheme = true)
                        } else {
                            LUIUtil.setDarkTheme(isDarkTheme = false)
                        }

                        val intent = Intent(context, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        context?.let {
                            LActivityUtil.transActivityNoAnimation(it)
                        }

                        dialog?.dismiss()
                    }
            )
            dialog?.setOnCancelListener {
                swEnableDarkMode?.isChecked = LUIUtil.isDarkTheme()
            }
        }
    }

    private fun handleSwitchSmallThumb(isChecked: Boolean) {
        LSharedPrefsUtil.instance.putBoolean(Cons.IS_SMALL_THUMB, isChecked)
        showShortInformation(getString(R.string.setting_success))
    }

    private fun handleSwitchGridView(isChecked: Boolean) {
        LSharedPrefsUtil.instance.putBoolean(Cons.IS_GRID_VIEW, isChecked)
        showShortInformation(getString(R.string.setting_success))
    }
}
