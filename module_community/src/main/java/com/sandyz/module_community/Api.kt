package com.sandyz.module_community


import com.sandyz.alltimers.common.bean.ApiWrapper
import com.sandyz.alltimers.common.bean.InfoWrapper
import com.sandyz.alltimers.common.bean.User
import com.sandyz.api_community.bean.DynamicItem
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
    @POST("Counter4Sql?action=getAllDynamic")
    fun getAllDynamic(
        @Field("pos") pos: Int,
        @Field("size") size: Int,
        @Field("topic") topic: String
    ): Observable<ApiWrapper<List<DynamicItem>>>

    @FormUrlEncoded
    @POST("Counter4Sql?action=releaseDynamic")
    fun releaseDynamic(
        @Field("text") text: String,
        @Field("topic") topic: String
    ): Observable<InfoWrapper>

    // 发布有图片的帖子，图片url用逗号分割
    @FormUrlEncoded
    @POST("Counter4Sql?action=releaseDynamic")
    fun releaseDynamic(
        @Field("text") text: String,
        @Field("topic") topic: String,
        @Field("pic_url") picUrl: String
    ): Observable<InfoWrapper>


    @FormUrlEncoded
    @POST("Counter4Sql?action=reply")
    fun reply(
        @Field("text") text: String,
        @Field("reply_id") replyId: Int,
        @Field("which") which: Int
    ): Observable<InfoWrapper>


    @FormUrlEncoded
    @POST("Counter4Sql?action=getUserInfo")
    fun getUserInfo(
        @Field("user_id") userId: String
    ): Observable<ApiWrapper<User>>

    @FormUrlEncoded
    @POST("Counter4Sql?action=reversePraise")
    fun reversePraise(
        @Field("id") id: Int,
        @Field("which") which: Int
    ): Observable<InfoWrapper>

    @FormUrlEncoded
    @POST("Counter4Sql?action=deleteComment")
    fun deleteComment(
        @Field("id") id: Int,
        @Field("which") which: Int
    ): Observable<InfoWrapper>

    @FormUrlEncoded
    @POST("Counter4Sql?action=deleteDynamic")
    fun deleteDynamic(
        @Field("dynamic_id") dynamicId: Int
    ): Observable<InfoWrapper>


    @FormUrlEncoded
    @POST("Counter4Sql?action=changeUserInfo")
    fun changeUserInfo(
        @Field("user_info") userInfo: String
    ): Observable<InfoWrapper>


}