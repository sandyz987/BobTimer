package com.sandyz.alltimers.common.network


import com.sandyz.alltimers.common.bean.ApiWrapper
import com.sandyz.alltimers.common.bean.InfoWrapper
import com.sandyz.alltimers.common.bean.User
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 *@author zhangzhe
 *@date 2021/3/3
 *@description
 */

interface Api {

    @FormUrlEncoded
    @POST("Counter4Sql?action=register")
    fun register(
        @Field("user_id") userId: String,
        @Field("password") password: String
    ): Observable<InfoWrapper>


}