package com.sandyz.alltimers.common.bean.networkbean

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("token")
    val token: Int = 0
)