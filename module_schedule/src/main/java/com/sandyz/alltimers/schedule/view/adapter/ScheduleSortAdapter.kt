package com.sandyz.alltimers.schedule.view.adapter

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.api_schedule.SortData
import com.sandyz.alltimers.schedule.R
import kotlin.math.max

/**
 *@author zhangzhe
 *@date 2022/2/27
 *@description
 */

class ScheduleSortAdapter(
    private val rv: RecyclerView,
    private val list: MutableList<SortData>,
    private var selectedPos: Int
) :
    RecyclerView.Adapter<ScheduleSortAdapter.ViewHolder>() {

    init {
        selectedPos = max(0, selectedPos)
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvBgName: TextView = v.findViewById(R.id.schedule_tv_sort)
        val ivBgIcon: ImageView = v.findViewById(R.id.schedule_iv_sort)
    }

    fun getSelected(): String {
        return list[selectedPos].name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.schedule_item_sort, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickAction(null)
        if (selectedPos == position) {
            selectAnim(holder.itemView, 0)
        } else {
            unSelectAnim(holder.itemView, 0)
        }
        if (position == list.size) {
            holder.tvBgName.text = "添加"
            holder.ivBgIcon.setImageResource(R.drawable.schedule_ic_sort_add)
        } else {
            holder.tvBgName.text = list[position].name
            holder.ivBgIcon.setImageResource(list[position].iconId)
            holder.itemView.setOnClickAction {
                if (selectedPos != position) {
                    selectAnim(holder.itemView, 200)
                    rv.findViewHolderForAdapterPosition(selectedPos)?.itemView?.let {
                        unSelectAnim(it, 200)
                    }
                    selectedPos = position
                }
            }
        }
    }

    private var selectAnimator: AnimatorSet? = null
    private fun selectAnim(v: View, duration: Long) {
        v.post {
            v.pivotX = v.width / 2f
            v.pivotY = v.height / 2f
            selectAnimator?.cancel()
            selectAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(v, "scaleX", v.scaleX, 1.2f)).with(ObjectAnimator.ofFloat(v, "scaleY", v.scaleY, 1.2f))
                this.duration = duration
                interpolator = OvershootInterpolator()
                start()
            }
        }
    }

    private var unSelectAnimator: AnimatorSet? = null
    private fun unSelectAnim(v: View, duration: Long) {
        v.post {
            v.pivotX = v.width / 2f
            v.pivotY = v.height / 2f
            unSelectAnimator?.cancel()
            unSelectAnimator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(v, "scaleX", v.scaleX, 1f)).with(ObjectAnimator.ofFloat(v, "scaleY", v.scaleY, 1f))
                this.duration = duration
                interpolator = OvershootInterpolator()
                start()
            }
        }
    }



    override fun getItemCount() = list.size + 1
}