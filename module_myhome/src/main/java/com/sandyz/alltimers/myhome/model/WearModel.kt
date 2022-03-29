package com.sandyz.alltimers.myhome.model

import com.sandyz.alltimers.common.utils.ResourceGetter
import com.sandyz.alltimers.myhome.R
import com.sandyz.alltimers.myhome.bean.CommodityData

/**
 *@author zhangzhe
 *@date 2022/3/29
 *@description
 */

// TODO 临时解决方案（赶工），待改进
object WearModel {

    fun getWear(): List<CommodityData> {
        val list = mutableListOf<CommodityData>()
        (1..45).forEach {
            list.add(
                CommodityData(
                    getWearName(it).first,
                    "myhome_wear_${it}",
                    ResourceGetter.getDrawableId(R.drawable::class.java, "myhome_wear_ic_${it}"),
                    200,
                    getWearName(it).second
                )
            )
        }
        return list
    }

    /**
     * 返回第一个：服饰名称，第二个：类别
     */
    fun getWearName(id: Int): Pair<String, String> {
        return when (id) {
            1 -> Pair("春日游园帽", "5")
            2 -> Pair("蓝色海军帽", "5")
            3 -> Pair("小兔叽眼罩", "5")
            4 -> Pair("帅气网球帽", "5")
            5 -> Pair("勤劳嗡发箍", "5")
            6 -> Pair("BOB发夹（黄）", "5")
            7 -> Pair("BOB发夹（粉）", "5")
            8 -> Pair("复古红蝴蝶结", "5")
            9 -> Pair("学院圆顶帽", "5")
            10 -> Pair("春日洋洋鞋", "4")
            11 -> Pair("暖暖粉红鞋", "4")
            12 -> Pair("小兔叽拖鞋", "4")
            13 -> Pair("夏日海洋鞋", "4")
            14 -> Pair("夏日阳光鞋", "4")
            15 -> Pair("经典学院鞋", "4")
            16 -> Pair("小家碧玉鞋", "4")
            17 -> Pair("橙色暖风鞋", "4")
            18 -> Pair("蓝色暖风鞋", "4")
            19 -> Pair("背带裤套装", "3")
            20 -> Pair("暖暖淑女裙", "3")
            21 -> Pair("勤劳嗡套装", "3")
            22 -> Pair("中华旗袍", "3")
            23 -> Pair("波波吊带裙", "3")
            24 -> Pair("开心BOB连身裤", "3")
            25 -> Pair("卡通T恤（粉）", "1")
            26 -> Pair("卡通T恤（黄）", "1")
            27 -> Pair("休闲长T恤", "1")
            28 -> Pair("网球达人上衣", "1")
            29 -> Pair("热血篮球上衣", "1")
            30 -> Pair("文艺小马甲", "1")
            31 -> Pair("乖乖女外套", "1")
            32 -> Pair("学院派外套", "1")
            33 -> Pair("海军服上衣", "1")
            34 -> Pair("春日洋洋装", "1")
            35 -> Pair("宝宝蓝毛衣", "1")
            36 -> Pair("宝宝黄毛衣", "1")
            37 -> Pair("热血篮球裤", "2")
            38 -> Pair("酷酷牛仔裤", "2")
            39 -> Pair("运动达人裤（蓝）", "2")
            40 -> Pair("运动达人裤（黄）", "2")
            41 -> Pair("碎花短裙", "2")
            42 -> Pair("蓝色海军裙", "2")
            43 -> Pair("学院半身裙", "2")
            44 -> Pair("牛仔九分裤", "2")
            45 -> Pair("运动达人裤（粉）", "2")


            else -> Pair("服饰", "1")
        }
    }


}