package com.loitp.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeAdapter
import com.loitp.R
import com.loitp.model.Feed

/**
 * Created by ©Loitp93 on 2/6/2021.
 * VinHMS
 * www.muathu@gmail.com
 */
class DragDropAdapter(dataSet: List<Feed> = emptyList())
    : DragDropSwipeAdapter<Feed, DragDropAdapter.ViewHolder>(dataSet) {

    class ViewHolder(itemView: View) : DragDropSwipeAdapter.ViewHolder(itemView) {
        val tv: TextView = itemView.findViewById(R.id.tv)
        val ivDrag: ImageView = itemView.findViewById(R.id.ivDrag)
    }

    override fun getViewHolder(itemView: View) = ViewHolder(itemView)

    override fun onBindViewHolder(item: Feed, viewHolder: ViewHolder, position: Int) {
        viewHolder.tv.text = item.title
    }

    override fun getViewToTouchToStartDraggingItem(item: Feed, viewHolder: ViewHolder, position: Int): View? {
        // We return the view holder's view on which the user has to touch to drag the item
        return viewHolder.ivDrag
    }
}
