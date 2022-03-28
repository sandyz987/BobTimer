package com.sandyz.alltimers.myhome.model

import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.myhome.bean.SortData

/**
 *@author zhangzhe
 *@date 2022/3/28
 *@description
 */

// TODO 临时解决方案（赶工），待改进
object SortModel {

    fun getSortDataFurniture(): List<SortData> {
        val list = mutableListOf<SortData>()
        (1..7).forEach {
            list.add(
                SortData(
                    "$it",
                    ResourceGetter.getDrawableId("myhome_ic_furniture_sort_${it}"),
                    ResourceGetter.getDrawableId("myhome_ic_furniture_sort_${it}_s")
                )
            )
        }
        return list
    }


    fun getSortDataWear(): List<SortData> {
        val list = mutableListOf<SortData>()
        (1..5).forEach {
            list.add(
                SortData(
                    "$it",
                    ResourceGetter.getDrawableId("myhome_wear_ic_sort_${it}"),
                    ResourceGetter.getDrawableId("myhome_wear_ic_sort_${it}_s")
                )
            )
        }
        return list
    }

}