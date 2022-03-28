package com.sandyz.alltimers.myhome.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.backgroundscroll.ScrollBackgroundView
import com.sandyz.alltimers.myhome.model.FurnitureModel
import com.sandyz.alltimers.myhome.model.SortModel
import kotlinx.android.synthetic.main.myhome_layout_edit.view.*

class EditPagerAdapter(private val scrollBackgroundView: ScrollBackgroundView) : RecyclerView.Adapter<EditPagerAdapter.ViewHolder>() {
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val rvSort = v.myhome_rv_sort
        val rvShop = v.myhome_rv_shop
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.myhome_layout_edit, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rvSort.layoutManager = LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
        holder.rvShop.layoutManager = GridLayoutManager(holder.itemView.context, 3, RecyclerView.VERTICAL, false)

        if (position == 0) {
            holder.rvSort.adapter = SortAdapter(SortModel.getSortDataWear(), holder.rvSort)
        } else {
            val adapter = SortAdapter(SortModel.getSortDataFurniture(), holder.rvSort)
            holder.rvSort.adapter = adapter
            holder.rvShop.adapter = WidgetContentAdapter(scrollBackgroundView, "1", FurnitureModel.getFurniture()).apply {
                adapter.onSelected = {
                    this.refresh(it)
                }
            }
        }
    }

    override fun getItemCount() = 2
}