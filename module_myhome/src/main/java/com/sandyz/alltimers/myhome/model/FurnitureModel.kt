package com.sandyz.alltimers.myhome.model

import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.bean.CommodityData
import kotlin.random.Random

/**
 *@author zhangzhe
 *@date 2022/3/28
 *@description
 */

// TODO 临时解决方案（赶工），待改进
object FurnitureModel {

    fun getFurniture(): List<CommodityData> {
        val list = mutableListOf<CommodityData>()
        (1..4).forEach {
            list.add(
                CommodityData(
                    getFurnitureName(it).first,
                    "Widget${it}",
                    ResourceGetter.getDrawableId(R.drawable::class.java,"myhome_widget_${it}"),
                    Random(2).nextInt(200, 400),
                    getFurnitureName(it).second
                )
            )
        }
        return list
    }

    /**
     * 返回第一个：家具名称，第二个：类别
     */
    fun getFurnitureName(id: Int): Pair<String, String> {
        return when (id) {
            1 -> Pair("阳光沙发", "3")
            else -> Pair("家具", "1")
        }
    }


}