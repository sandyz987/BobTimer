package com.sandyz.alltimers.myhome.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.bean.SortData
import kotlinx.android.synthetic.main.myhome_item_sort.view.*

/**
 *@author zhangzhe
 *@date 2022/3/28
 *@description
 */

class SortAdapter(private val list: List<SortData>, private val rv: RecyclerView) : RecyclerView.Adapter<SortAdapter.ViewHolder>() {

    private var selectedPos: Int = 0

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvSort: ImageView = v.myhome_iv_sort
    }

    var onSelected: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.myhome_item_sort, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (selectedPos == position) {
            holder.tvSort.setImageResource(list[position].selectedIcon)
        } else {
            holder.tvSort.setImageResource(list[position].noSelectedIcon)
        }
        holder.itemView.setOnClickAction {
            if (selectedPos != position) {
                rv.findViewHolderForAdapterPosition(selectedPos)?.itemView?.myhome_iv_sort?.setImageResource(list[selectedPos].noSelectedIcon)
                holder.tvSort.setImageResource(list[position].selectedIcon)
                selectedPos = position
            }
            onSelected?.invoke((position + 1).toString())
        }
    }

    override fun getItemCount() = list.size
}