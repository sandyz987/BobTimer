package com.sandyz.alltimers.schedule.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "schedule_list")
data class ScheduleData(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "icon")
    val icon: String,
    @ColumnInfo(name = "target_date")
    val targetDate: Long,
    @ColumnInfo(name = "topping")
    val topping: Boolean,
    @ColumnInfo(name = "remarks")
    val remarks: String,
    @ColumnInfo(name = "remind_type")
    val remindType: String,
    @ColumnInfo(name = "remind_period")
    val remindPeriod: String
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
