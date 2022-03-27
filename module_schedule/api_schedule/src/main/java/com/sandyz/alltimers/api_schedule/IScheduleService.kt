package com.sandyz.alltimers.api_schedule

import com.alibaba.android.arouter.facade.template.IProvider

interface IScheduleService : IProvider {

    fun obtainScheduleDataAll(): List<ScheduleData>?

}