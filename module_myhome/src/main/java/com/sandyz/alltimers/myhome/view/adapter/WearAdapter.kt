package com.sandyz.alltimers.myhome.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.backgroundscroll.Rabbit
import com.sandyz.alltimers.myhome.backgroundscroll.ScrollBackgroundView
import com.sandyz.alltimers.myhome.bean.CommodityData
import com.sandyz.alltimers.myhome.rabbit.RabbitSurfaceView
import kotlinx.android.synthetic.main.myhome_item_widget.view.*

class WearAdapter(scrollBackgroundView: ScrollBackgroundView, filter: String, private val commodityList: List<CommodityData>) :
    RecyclerView.Adapter<WearAdapter.ViewHolder>() {

    data class Goods(
        val commodityData: CommodityData,
        var isSelected: Boolean = false
    )

    private var headdress: RabbitSurfaceView? = null
    private var clothes: RabbitSurfaceView? = null

    private val list = mutableListOf<Goods>()

    private var filter: String = ""

    init {
        scrollBackgroundView.postDelayed({
            val rabbitChild = (scrollBackgroundView.findWidget("Rabbit")?.scrollChild as? Rabbit)
            headdress = rabbitChild?.headdress
            clothes = rabbitChild?.clothes
            refresh(filter)
        }, 500L)
        refresh(filter)
    }

    fun refresh() {
        refresh(filter)
    }

    fun refresh(filter: String) {
        this.filter = filter
        list.clear()
        commodityList.forEach {
            if (it.sort == filter || filter == "") {
                list.add(
                    Goods(it, (headdress?.hasSurface(it.resName) == true || clothes?.hasSurface(it.resName) == true))
                )
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.myhome_tv_widget_name
        val price = v.myhome_tv_widget__price
        val pic = v.myhome_iv_widget_pic
        val selected = v.myhome_iv_widget_select

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.myhome_item_widget, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.apply {
            name.text = data.commodityData.name
            price.text = data.commodityData.price.toString()
            if (data.isSelected) {
                selected.setImageResource(R.drawable.myhome_ic_widget_bg_s)
            } else {
                selected.setImageResource(R.drawable.myhome_ic_widget_bg)
            }
            pic.setImageResource(data.commodityData.icon)
            itemView.setOnClickAction {
                val sort = data.commodityData.sort
                if (data.isSelected) {
                    headdress?.removeSurfaceImage(data.commodityData.resName)
                    clothes?.removeSurfaceImage(data.commodityData.resName)
                } else {
                    if (sort == "5") {
                        headdress?.addSurfaceImage(data.commodityData.resName)
                    } else {
                        clothes?.addSurfaceImage(data.commodityData.resName)
                    }
                }
                data.isSelected = !data.isSelected
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount() = list.size
}