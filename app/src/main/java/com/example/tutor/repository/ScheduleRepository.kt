package com.example.tutor.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.tutor.bd.dao.ScheduleDAO
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import com.example.tutor.bd.entities.StudentEntity
import kotlinx.coroutines.flow.Flow

class ScheduleRepository(private val scheduleDAO: ScheduleDAO){

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity):Long {
        return scheduleDAO.insertSchedule(scheduleEntity)
    }
    fun getAllLessons():Flow<List<ScheduleEntity>> =scheduleDAO.getAllLessons()// полуить список всех занятий
    fun insertAllLessons(scheduleList:List<ScheduleEntity>){
        scheduleDAO.insertAllLessons(scheduleList)
    }
    fun getScheduleId(studentId:Int,dateWithTime:Long)=scheduleDAO.getScheduleId(studentId,dateWithTime)


    fun scheduleOfDay(date:String): LiveData<List<ScheduleWithStudent>> = scheduleDAO.getScheduleForDay(date)

    fun getTotalWeekAmount():Flow<Int> = scheduleDAO.getTotalWeekAmount()
    fun getTotalMonthAmount():Flow<Int> = scheduleDAO.getTotalMonthAmount()
    fun getTotalPeriodAmount(period:String):Flow<Int> = scheduleDAO.getTotalPeriodAmount(period)

    fun getMapOfWeek():Map<Int,Int> = scheduleDAO.getMapOfWeek()
    fun getMapOfMonth(): Map<String,Int> = scheduleDAO.getMapOfMonth()
    fun getMapOfYear(month:String):Map<Int,Int> = scheduleDAO.getMapOfYear(month)


    fun getTotalWeekLessons():Flow<Int> = scheduleDAO.getTotalWeekLessons()
    fun getTotalMonthLessons():Flow<Int> = scheduleDAO.getTotalMonthLessons()
    fun getTotalPeriodLessons(period: String):Flow<Int> = scheduleDAO.getTotalPeriodLessons(period)

    fun getMapOfWeekLessons(): Map<Int,Int> = scheduleDAO.getMapOfWeekLessons()
    fun getMapOfMonthLessons(): Map<String,Int> = scheduleDAO.getMapOfMonthLessons()
    fun getMapOfPeriodLessons(month: String): Map<Int,Int> = scheduleDAO.getMapOfPeriodLessons(month)

    fun getStudentLessons(id:Int): Flow<List<Long>> = scheduleDAO.getStudentLessons(id)



    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity){
        scheduleDAO.deleteSchedule(scheduleEntity)
    }
}