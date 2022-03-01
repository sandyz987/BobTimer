package com.sandyz.alltimers.schedule.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sandyz.alltimers.schedule.bean.ScheduleData

@Database(entities = [ScheduleData::class], version = 1)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun counterDao(): ScheduleDao?
}