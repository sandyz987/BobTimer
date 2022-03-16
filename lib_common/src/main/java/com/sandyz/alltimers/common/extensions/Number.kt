package com.sandyz.alltimers.common.extensions

import kotlin.math.max
import kotlin.math.min

/**
 *@author zhangzhe
 *@date 2022/3/15
 *@description
 */

fun Float.fixToRange(lower: Float, upper: Float): Float {
    return min(max(lower, this), upper)
}
