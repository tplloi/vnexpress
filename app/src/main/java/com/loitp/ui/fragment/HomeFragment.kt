package com.loitp.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.annotation.LogTag
import com.core.base.BaseFragment
import com.core.common.Constants
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.loitp.model.Feed
import com.views.viewpager.viewpagertransformers.ZoomOutSlideTransformer
import kotlinx.android.synthetic.main.frm_home.*
import java.util.*

@LogTag("HomeFragment")
class HomeFragment : BaseFragment() {

    private val listFeed = ArrayList<Feed>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listFeed.addAll(Cons.getListFeed())
        setupViews()
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        viewPager.setPageTransformer(true, ZoomOutSlideTransformer())
        viewPager.adapter = HomePagerAdapter(parentFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        LUIUtil.changeTabsFont(tabLayout = tabLayout, fontName = Constants.FONT_PATH)
    }

    private inner class HomePagerAdapter(fm: FragmentManager)
        : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            val pageFragment = PageFragment()
            val bundle = Bundle()
            bundle.putSerializable(PageFragment.KEY_FEED, listFeed[position])
            pageFragment.arguments = bundle
            return pageFragment
        }

        override fun getCount(): Int {
            return listFeed.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return listFeed[position].title
        }
    }
}
