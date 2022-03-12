package com.sandyz.alltimers.schedule.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sandyz.alltimers.common.BaseApp
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.schedule.bean.ScheduleData
import com.sandyz.alltimers.schedule.view.castom.CarrotProgressBar
import com.sandyz.alltimers.schedule.view.castom.SnapDelete
import kotlinx.android.synthetic.main.schedule_item_main.view.*
import kotlinx.android.synthetic.main.schedule_layout_content.view.*

/**
 *@author zhangzhe
 *@date 2022/3/10
 *@description
 */

class ScheduleMainAdapter(private val list: MutableList<ScheduleData>) :
    RecyclerView.Adapter<ScheduleMainAdapter.ViewHolder>() {


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val snapDelete: SnapDelete = v.schedule_snap_view
        val ivScheduleSort: ImageView = v.schedule_iv_item_sort
        val tvScheduleTitle: TextView = v.schedule_item_tv_title
        val tvScheduleTarget: TextView = v.schedule_item_tv_target
        val progress: CarrotProgressBar = v.schedule_carrot_progress_bar
        val tvLastTime: TextView = v.schedule_item_tv_last_time
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item_main, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.snapDelete.resumeAnim(0L)
        holder.snapDelete.action1 = {
            Toast.makeText(BaseApp.context, "置顶", Toast.LENGTH_SHORT).show()
        }
        holder.snapDelete.action2 = {
            Toast.makeText(BaseApp.context, "编辑", Toast.LENGTH_SHORT).show()
        }
        holder.snapDelete.action3 = {
            Toast.makeText(BaseApp.context, "删除", Toast.LENGTH_SHORT).show()
            list.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, list.size - position)
        }
        holder.tvScheduleTitle.text = list[position].name
        holder.tvScheduleTarget.text = list[position].targetDate.toString()
        holder.progress.progress = 0.7f
        holder.tvLastTime.text = "8"

    }


    override fun getItemCount() = list.size
}