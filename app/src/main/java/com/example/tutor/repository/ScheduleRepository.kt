package com.example.tutor.repository

import androidx.annotation.WorkerThread
import com.example.tutor.bd.dao.ScheduleDAO
import com.example.tutor.bd.entities.ScheduleEntity

class ScheduleRepository(private val scheduleDAO: ScheduleDAO){

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity) {
        scheduleDAO.insertSchedule(scheduleEntity)
    }

}