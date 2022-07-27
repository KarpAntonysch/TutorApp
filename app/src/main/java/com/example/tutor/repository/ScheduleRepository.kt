package com.example.tutor.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.tutor.bd.dao.ScheduleDAO
import com.example.tutor.bd.entities.Amount
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val scheduleDAO: ScheduleDAO){

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity) {
        scheduleDAO.insertSchedule(scheduleEntity)
    }
    fun scheduleOfDay(date:String): LiveData<List<ScheduleWithStudent>> = scheduleDAO.getScheduleForDay(date)
    fun getTotalWeekAmount():Flow<Int> = scheduleDAO.getTotalWeekAmount()
    fun getTotalWeekLessons():Flow<Int> = scheduleDAO.getTotalWeekLessons()

    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity){
        scheduleDAO.deleteSchedule(scheduleEntity)
    }
}