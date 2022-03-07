package com.sandyz.alltimers.schedule.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "schedule_list")
data class ScheduleData(
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "sort")
    var sort: String,
    @ColumnInfo(name = "target_date")
    var targetDate: Long,
    @ColumnInfo(name = "topping")
    var topping: Boolean,
    @ColumnInfo(name = "remarks")
    var remarks: String,
    @ColumnInfo(name = "remind_type")
    var remindType: String,
    @ColumnInfo(name = "remind_period")
    var remindPeriod: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
