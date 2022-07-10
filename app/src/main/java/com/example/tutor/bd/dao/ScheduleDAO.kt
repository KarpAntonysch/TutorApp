package com.example.tutor.bd.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent


@Dao
interface ScheduleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)
    // запрос для получения списка студентов(запрос к двум таблицам. Получения дня, времени, свойств студента) конкретный день
    @Query("select * from schedeulTable where strftime('%d-%m-%Y',dateWithTime/1000.0,'unixepoch') = :date")
    fun getScheduleForDay(date:String): LiveData<List<ScheduleWithStudent>>
}

