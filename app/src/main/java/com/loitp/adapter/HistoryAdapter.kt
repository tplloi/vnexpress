package com.loitp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.annotation.LogTag
import com.core.adapter.BaseAdapter
import com.loitp.R
import com.loitp.model.History
import kotlinx.android.synthetic.main.row_history_item.view.*
import java.util.*

@LogTag("HistoryAdapter")
class HistoryAdapter(
    private val onClick: ((History) -> Unit)? = null
) : BaseAdapter() {

    private val listHistory = ArrayList<History>()

    fun setItems(listHistory: List<History>) {
        this.listHistory.addAll(listHistory)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.row_history_item, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HistoryViewHolder) {
            holder.bind(listHistory[position])
        }
    }

    override fun getItemCount(): Int {
        return listHistory.size
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bind(history: History) {
            itemView.tvDate.text = "Ngày giao dịch: ${history.date}"
            itemView.tvMoney.text = "Thẻ cào mệnh giá: ${history.money} VND"
            itemView.tvPhone.text = "Nạp cho số điện thoại: ${history.getPhoneSecret()}"

            itemView.cardView.setOnClickListener {
                onClick?.invoke(history)
            }
        }
    }
}
