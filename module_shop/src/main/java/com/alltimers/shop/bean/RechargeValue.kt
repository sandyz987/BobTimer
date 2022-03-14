package com.alltimers.shop.bean

data class RechargeValue(
    val carrotCount: Int,
    val money: Float,
    val withDiscount: Boolean = false
)