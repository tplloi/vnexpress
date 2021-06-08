package com.loitp.constant

import com.core.utilities.*
import com.google.gson.reflect.TypeToken
import com.loitp.R
import com.loitp.model.Feed
import java.math.BigDecimal

/**
 * Created by ©Loitp93 on 1/27/2021.
 * VinHMS
 * www.muathu@gmail.com
 */
object Cons {
    const val IS_SMALL_THUMB = "IS_SMALL_THUMB"
    const val IS_GRID_VIEW = "IS_GRID_VIEW"
    const val PAGE_SIZE = 50
    private const val KEY_LIST_FEED = "KEY_LIST_FEED"
    private const val KEY_MONEY = "KEY_MONEY"

    fun getListFeed(): ArrayList<Feed> {
        val type = object : TypeToken<List<Feed>>() {
        }.type
        val value: ArrayList<Feed> = LSharedPrefsUtil.instance.getObjectList(KEY_LIST_FEED, type)
        if (value.isNullOrEmpty()) {
            val listFeed = ArrayList<Feed>()
            listFeed.apply {
                add(
                    Feed(
                        title = LAppResource.getString(R.string.lasted_news),
                        url = "https://vnexpress.net/rss/tin-moi-nhat.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.hot_news),
                        url = "https://vnexpress.net/rss/tin-noi-bat.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.education),
                        url = "https://vnexpress.net/rss/giao-duc.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.law),
                        url = "https://vnexpress.net/rss/phap-luat.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.sport),
                        url = "https://vnexpress.net/rss/the-thao.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.entertaiment),
                        url = "https://vnexpress.net/rss/giai-tri.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.startup),
                        url = "https://vnexpress.net/rss/startup.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.business),
                        url = "https://vnexpress.net/rss/kinh-doanh.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.news),
                        url = "https://vnexpress.net/rss/thoi-su.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.global),
                        url = "https://vnexpress.net/rss/the-gioi.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.most_viewed_news),
                        url = "https://vnexpress.net/rss/tin-xem-nhieu.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.fun_news),
                        url = "https://vnexpress.net/rss/cuoi.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.chat_news),
                        url = "https://vnexpress.net/rss/tam-su.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.idea),
                        url = "https://vnexpress.net/rss/y-kien.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.car_bike),
                        url = "https://vnexpress.net/rss/oto-xe-may.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.technical),
                        url = "https://vnexpress.net/rss/so-hoa.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.science),
                        url = "https://vnexpress.net/rss/khoa-hoc.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.travel),
                        url = "https://vnexpress.net/rss/du-lich.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.family),
                        url = "https://vnexpress.net/rss/gia-dinh.rss"
                    )
                )
                add(
                    Feed(
                        title = LAppResource.getString(R.string.healthy),
                        url = "https://vnexpress.net/rss/suc-khoe.rss"
                    )
                )
            }
            return listFeed
        } else {
            return value
        }
    }

    fun saveListFeed(listFeed: List<Feed>) {
        LSharedPrefsUtil.instance.putObjectList(KEY_LIST_FEED, listFeed)
    }

    fun getCurrentMoneyInString(): String {
        val currentMoney = getCurrentMoneyInBigDecimal()
        return LConvertUtil.convertToPrice(price = currentMoney) + " $"
    }

    fun getCurrentMoneyInBigDecimal(): BigDecimal {
        val value = LEncryptionSharedPrefsUtil.instance.getString(KEY_MONEY, "0")
        return try {
            value.toBigDecimal()
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }

    fun addMoney(): Int {
        var currentMoney = getCurrentMoneyInBigDecimal()
        val bonus = LStoreUtil.getRandomNumber(40)
        currentMoney += bonus.toBigDecimal()
        LEncryptionSharedPrefsUtil.instance.put(KEY_MONEY, currentMoney.toString())
        return bonus
    }
}
