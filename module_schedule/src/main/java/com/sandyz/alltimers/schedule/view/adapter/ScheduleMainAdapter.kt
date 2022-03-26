package com.sandyz.alltimers.schedule.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sandyz.alltimers.api_schedule.ScheduleTimeHelper
import com.sandyz.alltimers.common.utils.CalendarUtil
import com.sandyz.alltimers.common.utils.TimeUtil
import com.sandyz.alltimers.common.utils.toTimeString
import com.sandyz.alltimers.common.widgets.LogUtils
import com.sandyz.alltimers.common.widgets.OptionalDialog
import com.sandyz.alltimers.schedule.R
import com.sandyz.alltimers.common.legacy.ScheduleData
import com.sandyz.alltimers.common.legacy.ScheduleSortData
import com.sandyz.alltimers.schedule.view.custom.CarrotProgressBar
import com.sandyz.alltimers.schedule.view.custom.SnapDelete
import kotlinx.android.synthetic.main.schedule_item_main.view.*
import kotlinx.android.synthetic.main.schedule_layout_content.view.*
import kotlinx.android.synthetic.main.schedule_layout_top.view.*

/**
 *@author zhangzhe
 *@date 2022/3/10
 *@description
 */

class ScheduleMainAdapter(
    private val list: MutableList<ScheduleData>,
    private val onOpen: ((ScheduleData) -> Unit)? = null,
    private val onTopping: ((ScheduleData) -> Unit)? = null,
    private val onEdit: ((ScheduleData) -> Unit)? = null,
    private val onDelete: ((ScheduleData) -> Unit)? = null
) :
    RecyclerView.Adapter<ScheduleMainAdapter.ViewHolder>() {


    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val snapDelete: SnapDelete = v.schedule_snap_view
        val ivScheduleSort: ImageView = v.schedule_iv_item_sort
        val tvScheduleTitle: TextView = v.schedule_item_tv_title
        val tvScheduleTarget: TextView = v.schedule_item_tv_target
        val progress: CarrotProgressBar = v.schedule_carrot_progress_bar
        val tvLastTime: TextView = v.schedule_item_tv_last_time
        val tvLastTimeText: TextView = v.schedule_item_tv_last_time_text
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item_main, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.snapDelete.resumeAnim(0L)
        holder.snapDelete.action0 = {
            onOpen?.invoke(list[position])
        }
        holder.snapDelete.action1 = {
            onTopping?.invoke(list[position])
        }
        holder.snapDelete.action2 = {
            onEdit?.invoke(list[position])
        }
        holder.snapDelete.action3 = {
            OptionalDialog.show(holder.itemView.context, "真的要删除此日程吗？", onDeny = {}) {
                onDelete?.invoke(list[position])
                list.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, list.size - position)
            }
        }
        holder.tvScheduleTitle.text = list[position].name
        holder.tvScheduleTarget.text = TimeUtil.monthStrWithWeek(list[position].targetStartDate)
        holder.progress.visibility = if (list[position].showProgress) View.VISIBLE else View.GONE

        val progress = ScheduleTimeHelper.getProgress(list[position])
        if (progress < 0) {
            holder.progress.progress =
                ScheduleTimeHelper.getProgress(list[position].modifyDate, list[position].targetStartDate, CalendarUtil.getCalendar().timeInMillis)
        } else {
            holder.progress.progress = progress
        }
        LogUtils.e("progress:::::::${holder.progress.progress}, pos:${position}, data:${list[position]}")

        LogUtils.e("origin${CalendarUtil.getCalendar(list[position].targetStartDate).toTimeString()}")
        val nextTarget = ScheduleTimeHelper.getNextTarget(list[position])
        LogUtils.e("next${CalendarUtil.getCalendar(nextTarget).toTimeString()}")

        if (nextTarget != -1L) {
            holder.tvLastTime.text = "${ScheduleTimeHelper.getDiffDays(nextTarget)}"
        } else {
            holder.tvLastTime.text = "-"
        }

        if (list[position].topping) {
            holder.tvLastTime.setTextColor(Color.parseColor("#FF8854"))
            holder.tvLastTimeText.setTextColor(Color.parseColor("#FF8854"))
            holder.itemView.schedule_item_tv_topping.text = "取\n消\n置\n顶"
        } else {
            holder.tvLastTime.setTextColor(Color.parseColor("#995B3A"))
            holder.tvLastTimeText.setTextColor(Color.parseColor("#995B3A"))
            holder.itemView.schedule_item_tv_topping.text = "置\n顶"
        }

        holder.ivScheduleSort.setImageResource(ScheduleSortData.list.find { it.name == list[position].sort }?.iconItemId ?: 0)


    }


    override fun getItemCount() = list.size
}