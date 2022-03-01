package com.sandyz.alltimers.schedule.model

import androidx.room.Room
import com.sandyz.alltimers.common.BaseApp

object ScheduleReader {
    var db: ScheduleDatabase? = null

    init {
        db = Room.databaseBuilder(
            BaseApp.context,
            ScheduleDatabase::class.java, "schedule_list"
        ).allowMainThreadQueries().build()
    }

}