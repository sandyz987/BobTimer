package com.sandyz.alltimers.schedule.model

import androidx.room.*
import com.sandyz.alltimers.common.legacy.ScheduleData

@Dao
interface ScheduleDao {
    //查询ScheduleData表中所有数据
    @get:Query("SELECT * FROM schedule_list")
    val all: List<ScheduleData>?

    @Query("SELECT * FROM schedule_list WHERE id IN (:c)")
    fun loadAllByIds(c: IntArray?): List<ScheduleData?>?

    @Query("SELECT * FROM schedule_list WHERE id =:id LIMIT 1")
    fun findScheduleData(id: Int): ScheduleData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(c: List<ScheduleData?>?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(c: ScheduleData?)

    @Delete
    fun delete(vararg c: ScheduleData?)

    @Query("DELETE FROM schedule_list")
    fun deleteAllScheduleData()

    @Query("DELETE FROM schedule_list WHERE id =:id")
    fun delete(id: Int)

    @Query("SELECT * FROM schedule_list LIMIT :limit")
    fun getAll(limit: Int): List<ScheduleData?>?

    @Update
    fun update(vararg scheduleData: ScheduleData?)
}
