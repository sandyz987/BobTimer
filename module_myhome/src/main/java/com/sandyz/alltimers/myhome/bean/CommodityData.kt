package com.sandyz.alltimers.myhome.bean

/**
 *@author zhangzhe
 *@date 2022/3/28
 *@description
 */

open class CommodityData(
    val name: String,
    val resName: String,
    val icon: Int,
    val price: Int,
    val sort: String // 格式“种类数字/特别数字”     特别数字为1表示墙纸，2表示地板
)
