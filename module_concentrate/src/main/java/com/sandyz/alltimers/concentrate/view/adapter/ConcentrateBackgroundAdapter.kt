package com.sandyz.alltimers.concentrate.view.adapter

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
import com.sandyz.alltimers.concentrate.R
import com.sandyz.alltimers.concentrate.view.fragment.FragmentConcentrate
import kotlinx.android.synthetic.main.concentrate_item_background_selection.view.*

/**
 *@author zhangzhe
 *@date 2022/2/21
 *@description
 */

class ConcentrateBackgroundAdapter(private val concentrateFragment: FragmentConcentrate, private val rv: RecyclerView) :
    RecyclerView.Adapter<ConcentrateBackgroundAdapter.ViewHolder>() {


    private var selectedPos = 0

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvBgName: TextView = v.concentrate_tv_bg_name
        val ivBgIcon: ImageView = v.concentrate_iv_bg_icon
    }

    private val list = mutableListOf(
        BackgroundData("运动", R.drawable.concentrate_ic_icon_sport, R.drawable.concentrate_ic_bg_sport),
        BackgroundData("学习", R.drawable.concentrate_ic_icon_learn, R.drawable.concentrate_ic_bg_learn),
        BackgroundData("兴趣", R.drawable.concentrate_ic_icon_interest, R.drawable.concentrate_ic_bg_interest),
        BackgroundData("工作", R.drawable.concentrate_ic_icon_work, R.drawable.concentrate_ic_bg_work),
        BackgroundData("冥想", R.drawable.concentrate_ic_icon_think, R.drawable.concentrate_ic_bg_think)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.concentrate_item_background_selection, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickAction(null)
        if (selectedPos == position) {
            selectAnim(holder.itemView, 0)
        }else {
            unSelectAnim(holder.itemView, 0)
        }
        if (position == 5) {
            holder.tvBgName.text = ""
            holder.ivBgIcon.setImageResource(R.drawable.concentrate_ic_icon_add)
        } else {
            holder.tvBgName.text = list[position].name
            holder.ivBgIcon.setImageResource(list[position].iconId)
            holder.itemView.setOnClickAction {
                if (selectedPos != position) {
                    concentrateFragment.setBackground(list[position].bgId)
                    holder.itemView.let {
                        selectAnim(it, 200)
                    }
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
                play(ObjectAnimator.ofFloat(v, "scaleX", v.scaleX, 1.1f)).with(ObjectAnimator.ofFloat(v, "scaleY", v.scaleY, 1.1f))
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

    data class BackgroundData(
        val name: String,
        val iconId: Int,
        val bgId: Int
    )

    override fun getItemCount() = 6
}