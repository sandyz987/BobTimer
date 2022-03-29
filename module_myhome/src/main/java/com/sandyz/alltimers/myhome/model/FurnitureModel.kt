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
        (1..27).forEach {
            list.add(
                CommodityData(
                    getFurnitureName(it).first,
                    "Widget${it}",
                    ResourceGetter.getDrawableId(R.drawable::class.java, "myhome_widget_ic_${it}"),
                    200,
                    getFurnitureName(it).second
                )
            )
        }
        (28..31).forEach {
            list.add(
                CommodityData(
                    getFurnitureName(it).first,
                    when (it) {
                        28 -> "myhome_ic_floor_2"
                        29 -> "myhome_ic_floor_1"
                        30 -> "myhome_ic_wallpaper_1"
                        31 -> "myhome_ic_wallpaper_2"
                        else -> ""
                    },
                    ResourceGetter.getDrawableId(R.drawable::class.java, "myhome_widget_ic_${it}"),
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
            2 -> Pair("可爱熊落地", "3")
            3 -> Pair("波点小彩旗", "1")
            4 -> Pair("薄荷碎花椅", "3")
            5 -> Pair("翠绿小浴缸", "7")
            6 -> Pair("厨具挂勾", "6")
            7 -> Pair("兔子草", "5")
            8 -> Pair("郁金香小花丛", "5")
            9 -> Pair("紫色波纹花盆", "5")
            10 -> Pair("绿萝小挂篮", "5")
            11 -> Pair("玻璃金鱼缸", "1")
            12 -> Pair("煎蛋地毯", "1")
            13 -> Pair("悬挂小花相册", "1")
            14 -> Pair("星空画框", "1")
            15 -> Pair("花朵格子地毯", "1")
            16 -> Pair("简约小音箱", "2")
            17 -> Pair("复古天蓝电视机", "2")
            18 -> Pair("清新小冰箱", "2")
            19 -> Pair("黄色蘑菇灯", "2")
            20 -> Pair("树木挂衣架", "3")
            21 -> Pair("奶油月饼小床", "3")
            22 -> Pair("少女洗浴台", "3")
            23 -> Pair("暖阳小柜", "3")
            24 -> Pair("原木储物柜", "3")
            25 -> Pair("蔷薇床头柜", "3")
            26 -> Pair("复古兔子大钟", "3")
            27 -> Pair("简约拱形小窗", "3")
            28 -> Pair("黄绿色方格地板", "1/2")
            29 -> Pair("淡黄色木制地板", "1/2")
            30 -> Pair("bob兔星星壁纸", "1/1")
            31 -> Pair("蓝色条纹壁纸", "1/1")

            else -> Pair("家具", "1")
        }
    }


}