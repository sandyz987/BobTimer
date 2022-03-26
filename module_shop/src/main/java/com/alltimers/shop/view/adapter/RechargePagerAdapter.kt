package com.alltimers.shop.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.alltimers.shop.R
import com.alltimers.shop.bean.RechargeValue
import kotlinx.android.synthetic.main.shop_layout_recharge_item.view.*

class RechargePagerAdapter(private val list: MutableList<RechargeValue>) : PagerAdapter() {
    override fun getCount(): Int = list.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.shop_layout_recharge_item, container, false)
        container.addView(view)
        view.shop_tv_with_discount.visibility = if (list[position].withDiscount) {
            View.VISIBLE
        } else {
            View.GONE
        }
        view.shop_tv_carrot_count.text = list[position].carrotCount.toString()
        view.shop_tv_price.text = "￥${list[position].money}"
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as? View ?: return
        container.removeView(view)
    }
}