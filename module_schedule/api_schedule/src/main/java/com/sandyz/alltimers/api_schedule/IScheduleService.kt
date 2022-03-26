package com.sandyz.alltimers.api_schedule

import com.alibaba.android.arouter.facade.template.IProvider
import com.sandyz.alltimers.common.legacy.ScheduleData

interface IScheduleService : IProvider {

    fun obtainScheduleDataAll(): List<ScheduleData>?

}