package com.sandyz.alltimers.api_schedule

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "schedule_list")
data class ScheduleData(
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "sort")
    var sort: String = "生活",
    @ColumnInfo(name = "modify_date")
    var modifyDate: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(name = "target_start_date")
    var targetStartDate: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(name = "period")
    var period: String = "无", // [无/年/月/周/间【数字】]
    @ColumnInfo(name = "remind")
    var remind: String = "0", // 提前n天
    @ColumnInfo(name = "topping")
    var topping: Boolean = false,
    @ColumnInfo(name = "remarks")
    var remarks: String = "",
    @ColumnInfo(name = "background_image_uri")
    var backgroundImageUri: String = "",
    @ColumnInfo(name = "show_progress")
    var showProgress: Boolean = false,
    @ColumnInfo(name = "style")
    var style: Int = 0
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
