package com.example.tutor.repository

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.tutor.bd.dao.ScheduleDAO
import com.example.tutor.bd.entities.AmountByDays
import com.example.tutor.bd.entities.LessonsByDays
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
    fun getTotalMonthAmount():Flow<Int> = scheduleDAO.getTotalMonthAmount()
    fun getTotalPeriodAmount(period:String):Flow<Int> = scheduleDAO.getTotalPeriodAmount(period)
    fun getMapOfPrice(month:String):Map<Int,Int> = scheduleDAO.getMapOfPrice(month)



    fun getTotalWeekLessons():Flow<Int> = scheduleDAO.getTotalWeekLessons()
    fun getLessonsByDaysOfWeek():Flow<MutableList<LessonsByDays>> = scheduleDAO.getLessonsByDaysOfWeek()
    fun getAmountByDaysOfWeek():Flow<List<AmountByDays>> = scheduleDAO.getAmountByDaysOfWeek()


    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity){
        scheduleDAO.deleteSchedule(scheduleEntity)
    }
}