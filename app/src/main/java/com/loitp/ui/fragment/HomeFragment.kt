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
import com.loitp.model.Feed
import com.views.viewpager.viewpagertransformers.ZoomOutSlideTransformer
import kotlinx.android.synthetic.main.frm_home.*
import java.util.*

@LogTag("HomeFragment")
class HomeFragment : BaseFragment() {

    private val listFeed = ArrayList<Feed>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        setupViews()
    }

    private fun setupData() {
        //TODO setting customize feed
        with(listFeed) {
            add(Feed(title = getString(R.string.lasted_news), url = "https://vnexpress.net/rss/tin-moi-nhat.rss"))
            add(Feed(title = getString(R.string.hot_news), url = "https://vnexpress.net/rss/tin-noi-bat.rss"))
            add(Feed(title = getString(R.string.education), url = "https://vnexpress.net/rss/giao-duc.rss"))
            add(Feed(title = getString(R.string.law), url = "https://vnexpress.net/rss/phap-luat.rss"))
            add(Feed(title = getString(R.string.sport), url = "https://vnexpress.net/rss/the-thao.rss"))
            add(Feed(title = getString(R.string.entertaiment), url = "https://vnexpress.net/rss/giai-tri.rss"))
            add(Feed(title = getString(R.string.startup), url = "https://vnexpress.net/rss/startup.rss"))
            add(Feed(title = getString(R.string.business), url = "https://vnexpress.net/rss/kinh-doanh.rss"))
            add(Feed(title = getString(R.string.news), url = "https://vnexpress.net/rss/thoi-su.rss"))
            add(Feed(title = getString(R.string.global), url = "https://vnexpress.net/rss/the-gioi.rss"))
            add(Feed(title = getString(R.string.most_viewed_news), url = "https://vnexpress.net/rss/tin-xem-nhieu.rss"))
            add(Feed(title = getString(R.string.fun_news), url = "https://vnexpress.net/rss/cuoi.rss"))
            add(Feed(title = getString(R.string.chat_news), url = "https://vnexpress.net/rss/tam-su.rss"))
            add(Feed(title = getString(R.string.idea), url = "https://vnexpress.net/rss/y-kien.rss"))
            add(Feed(title = getString(R.string.car_bike), url = "https://vnexpress.net/rss/oto-xe-may.rss"))
            add(Feed(title = getString(R.string.technical), url = "https://vnexpress.net/rss/so-hoa.rss"))
            add(Feed(title = getString(R.string.science), url = "https://vnexpress.net/rss/khoa-hoc.rss"))
            add(Feed(title = getString(R.string.travel), url = "https://vnexpress.net/rss/du-lich.rss"))
            add(Feed(title = getString(R.string.family), url = "https://vnexpress.net/rss/gia-dinh.rss"))
            add(Feed(title = getString(R.string.healthy), url = "https://vnexpress.net/rss/suc-khoe.rss"))
        }
    }

    override fun setLayoutResourceId(): Int {
        return R.layout.frm_home
    }

    private fun setupViews() {
        viewPager.setPageTransformer(true, ZoomOutSlideTransformer())
        viewPager.adapter = SamplePagerAdapter(parentFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
        LUIUtil.changeTabsFont(tabLayout = tabLayout, fontName = Constants.FONT_PATH)
    }

    private inner class SamplePagerAdapter(fm: FragmentManager)
        : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return PageFragment()
        }

        override fun getCount(): Int {
            return listFeed.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return listFeed[position].title
        }
    }
}
