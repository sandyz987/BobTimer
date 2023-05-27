package com.sandyz.alltimers.mine.vm

import androidx.lifecycle.MutableLiveData
import com.sandyz.alltimers.common.base.BaseViewModel
import com.sandyz.alltimers.common.bean.User
import com.sandyz.alltimers.common.network.ApiGenerator
import com.sandyz.alltimers.common.network.checkApiError
import com.sandyz.alltimers.common.network.safeSubscribeBy
import com.sandyz.alltimers.common.network.setSchedulers
import com.sandyz.alltimers.mine.network.Api

class IndividualViewModel : BaseViewModel() {

    val user = MutableLiveData<User>()

    fun getUser(userId: String) {
        ApiGenerator.getApiService(Api::class.java).getUserInfo(userId)
            .checkApiError()
            .setSchedulers()
            .doOnError {
                toastEvent.value = "获取用户信息失败！"
            }.safeSubscribeBy {
                user.postValue(it)
            }.lifeCycle()
    }
}