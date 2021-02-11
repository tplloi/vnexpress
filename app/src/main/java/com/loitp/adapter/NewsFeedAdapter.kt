package com.loitp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.core.utilities.LImageUtil
import com.core.utilities.LScreenUtil
import com.core.utilities.LSharedPrefsUtil
import com.core.utilities.LUIUtil
import com.loitp.R
import com.loitp.constant.Cons
import com.loitp.model.NewsFeed
import com.skydoves.transformationlayout.TransformationLayout
import kotlinx.android.synthetic.main.row_rss_item.view.*
import java.util.*

@LogTag("NewsFeedAdapter")
class NewsFeedAdapter(
        private val onClick: ((NewsFeed, TransformationLayout) -> Unit)? = null
) : BaseAdapter() {

    private val splitString = "</a></br>"
    private val itemList = ArrayList<NewsFeed>()
    private val isSmallThumb = LSharedPrefsUtil.instance.getBoolean(Cons.IS_SMALL_THUMB, false)
    private var height = 0

    init {
        height = if (isSmallThumb) {
            LScreenUtil.screenHeight / 5
        } else {
            LScreenUtil.screenHeight / 2
        }
    }

    fun setItems(items: List<NewsFeed>) {

        fun isContain(rssItem: NewsFeed): Boolean {
            itemList.forEach {
                if (it.link == rssItem.link) {
                    return true
                }
            }
            return false
        }

        itemList.clear()
        items.forEach {
            val isContain = isContain(it)
//            logD("setItems isContain $isContain -> ${it.title}")
            if (!isContain) {
                itemList.add(it)
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFeedViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_rss_item, parent, false)
        return NewsFeedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is NewsFeedViewHolder) {
            holder.bind(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class NewsFeedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(newsFeed: NewsFeed) {
            LImageUtil.load(
                    context = itemView.ivThumb.context,
                    any = newsFeed.image,
                    imageView = itemView.ivThumb
            )
            LUIUtil.setSizeOfView(view = itemView.ivThumb, height = height)

            itemView.tvTitle.text = newsFeed.title
            itemView.tvPubDate.text = newsFeed.publishDate
//            logD("RSSViewHolder pos $bindingAdapterPosition -> " + rssItem.description)

            val des = newsFeed.description
            try {
                if (des.contains(splitString)) {
                    val arr = des.split(splitString)
                    itemView.tvDes.text = arr[1]
                } else {
                    LUIUtil.setTextFromHTML(textView = itemView.tvDes, bodyData = des)
                }
            } catch (e: Exception) {
                LUIUtil.setTextFromHTML(textView = itemView.tvDes, bodyData = des)
            }

            itemView.setOnClickListener {
                onClick?.invoke(newsFeed, itemView.layoutItemRssTransformation)
            }
        }
    }
}
