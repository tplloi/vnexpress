package com.loitp.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.annotation.LogTag
import com.core.base.BaseApplication
import com.core.base.BaseFragment
import com.loitp.R
import com.loitp.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.frm_home.*

@LogTag("HomeFragment")
class HomeFragment : BaseFragment() {

    private var mainViewModel: MainViewModel? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupViewModels()
        context?.let {
            mainViewModel?.loadListChap(context = it)
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {

    }

    private fun setupViewModels() {
        mainViewModel = getViewModel(MainViewModel::class.java)
        mainViewModel?.let { mvm ->
            mvm.eventLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                if (isLoading) {
                    indicatorView.smoothToShow()
                } else {
                    indicatorView.smoothToHide()
                }
            })

            mvm.listChapLiveData.observe(viewLifecycleOwner, Observer { listChap ->
                logD("<<<listChapLiveData " + BaseApplication.gson.toJson(listChap))
            })
        }

    }
}
