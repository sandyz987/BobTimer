package com.sandyz.alltimers.myhome.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sandyz.alltimers.common.extensions.setOnClickAction
import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.backgroundscroll.ScrollBackgroundView
import com.sandyz.alltimers.myhome.bean.CommodityData
import com.sandyz.alltimers.myhome.model.WallpaperAndFloorModel

class FurnitureAdapter(private val scrollBackgroundView: ScrollBackgroundView, filter: String, private val commodityList: List<CommodityData>) :
    RecyclerView.Adapter<FurnitureAdapter.ViewHolder>() {

    data class Goods(
        val commodityData: CommodityData,
        var isSelected: Boolean = false
    )


    private val list = mutableListOf<Goods>()

    private var filter: String = ""

    init {
        refresh(filter)
    }

    fun refresh() {
        refresh(filter)
    }

    fun refresh(filter: String) {
        this.filter = filter
        list.clear()
        commodityList.forEach {
            if ((it.sort.split("/")[0]) == filter || filter == "") {
                list.add(Goods(it, scrollBackgroundView.findWidget(it.resName) != null))
            }
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val name = v.findViewById<TextView>(R.id.myhome_tv_widget_name)
        val price = v.findViewById<TextView>(R.id.myhome_tv_widget__price)
        val pic = v.findViewById<ImageView>(R.id.myhome_iv_widget_pic)
        val selected = v.findViewById<ImageView>(R.id.myhome_iv_widget_select)

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
                val sort = data.commodityData.sort.split("/")
                if (sort.size <= 1) {
                    if (data.isSelected) {
                        scrollBackgroundView.removeWidgetByName(data.commodityData.resName)
                    } else {
                        scrollBackgroundView.addWidget(
                            data.commodityData.resName,
                            data.commodityData.resName,
                            scrollBackgroundView.getVisibleLeft() + scrollBackgroundView.width / 2,
                            650
                        )
                    }
                    data.isSelected = !data.isSelected
                    notifyItemChanged(position)
                } else {
                    if (sort[1] == "1") {
                        // 墙纸
                        val pic = ResourceGetter.getDrawableId(R.drawable::class.java, data.commodityData.resName)
                        WallpaperAndFloorModel.setWallpaper(itemView.context, pic)
                        scrollBackgroundView.setWallPaper(pic)
                    } else {
                        // 地板
                        val pic = ResourceGetter.getDrawableId(R.drawable::class.java, data.commodityData.resName)
                        WallpaperAndFloorModel.setFloor(itemView.context, pic)
                        scrollBackgroundView.setFloor(pic)
                    }
                }
            }
        }
    }

    override fun getItemCount() = list.size
}