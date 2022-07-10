package com.example.tutor.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.tutor.bd.dao.ScheduleDAO
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent

class ScheduleRepository(private val scheduleDAO: ScheduleDAO){

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity) {
        scheduleDAO.insertSchedule(scheduleEntity)
    }
    fun scheduleOfDay(date:String): LiveData<List<ScheduleWithStudent>> = scheduleDAO.getScheduleForDay(date)

    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity){
        scheduleDAO.deleteSchedulet(scheduleEntity)
    }
}