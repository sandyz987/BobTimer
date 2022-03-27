package com.sandyz.alltimers.schedule.api

import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.sandyz.alltimers.api_schedule.IScheduleService
import com.sandyz.alltimers.api_schedule.SCHEDULE_DATA
import com.sandyz.alltimers.api_schedule.ScheduleData
import com.sandyz.alltimers.schedule.model.ScheduleReader

/**
 *@author zhangzhe
 *@date 2022/3/26
 *@description
 */
@Route(path = SCHEDULE_DATA, name = SCHEDULE_DATA)
class ScheduleService: IScheduleService {
    override fun obtainScheduleDataAll(): List<ScheduleData>? {
        return ScheduleReader.db?.scheduleDao()?.all
    }

    lateinit var mContext: Context

    override fun init(context: Context) {
        mContext = context
    }
}