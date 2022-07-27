package com.example.tutor.bd.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tutor.bd.entities.Amount
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import kotlinx.coroutines.flow.Flow


@Dao
interface ScheduleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)
    // запрос для получения списка студентов(запрос к двум таблицам. Получения дня, времени, свойств студента) конкретный день
    @Query("SELECT * FROM schedeulTable WHERE strftime('%d-%m-%Y',dateWithTime/1000.0,'unixepoch') = :date")
    fun getScheduleForDay(date:String): LiveData<List<ScheduleWithStudent>>
    //запрос для получения заработанной суммы с понедельника по текущий день
    @Query("SELECT sum(a.price)FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')")
    fun getTotalWeekAmount() : Flow<Int>
    // запрос для получения количества занятий с понедельника по текущий день
    @Query("SELECT count(*) FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')")
    fun getTotalWeekLessons(): Flow<Int>
    @Query("SELECT * from schedeulTable WHERE date('now','weekday 1','-7 days')")
    fun getStud():LiveData<List<ScheduleWithStudent>>
    @Delete
    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}

//    select a.price,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') as date from schedeulTable b left join studentTable a on b.studentId=a.id where cast(b.dateWithTime/1000 as integer) between
//    strftime('%s',date('now','weekday 1','-7 days')) and strftime('%s','now');

//select sum(a.price),strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') as date from schedeulTable b left join studentTable a on a.id=b.studentId where cast(b.dateWithTime/1000 as integer) between
// strftime('%s',date('now','weekday 1','-7 days')) and strftime('%s','now')group by strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch');

//select count(*),strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') as date from schedeulTable b where cast(b.dateWithTime/1000 as integer)
// between strftime('%s',date('now','weekday 1','-7 days')) and strftime('%s','now')

//select count(*),strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') as date from schedeulTable b where cast(b.dateWithTime/1000 as integer)
// between strftime('%s',date('now','weekday 1','-7 days')) and strftime('%s','now') group by strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch');
