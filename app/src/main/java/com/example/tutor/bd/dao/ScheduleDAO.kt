package com.example.tutor.bd.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tutor.bd.entities.AmountByDays
import com.example.tutor.bd.entities.LessonsByDays
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
    // запрос на получение заработанной суммы за последний месяц (текущая дата - месяц)
    @Query("SELECT sum(a.price)FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','-1 month')) AND strftime('%s','now')")
    fun getTotalMonthAmount() : Flow<Int>
    @Query("SELECT sum(a.price)FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','-6 month')) AND strftime('%s','now')")
    fun getTotal6MonthAmount() : Flow<Int>
    @Query("SELECT sum(a.price)FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','-1 year')) AND strftime('%s','now')")
    fun getTotalYearAmount() : Flow<Int>

    // запрос для получения количества занятий с понедельника по текущий день
    @Query("SELECT count(*) FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')")
    fun getTotalWeekLessons(): Flow<Int>
    // запрос для получения количества уроков на каждый день недели начиная с пн по текущий день
    @Query("SELECT count(*) as lessons,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch')")
    fun getLessonsByDaysOfWeek(): Flow<MutableList<LessonsByDays>>
    // запрос на получение заработанной суммы в каждый день недели с пн по текущий день
    @Query("SELECT sum(a.price) AS price ,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch');")
    fun getAmountByDaysOfWeek(): Flow<List<AmountByDays>>
    @Query("SELECT * from schedeulTable WHERE date('now','weekday 1','-7 days')")

    fun getStud():LiveData<List<ScheduleWithStudent>>
    @Delete
    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}

// для полного месяца, а не с начала месяца до текущего дня
// SELECT count(*) as lessons,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','start of month')) AND strftime(date('now','start of month','+1 month','-1 day'))GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch')

// количество занятий за месяц (дата - месяц)
//SELECT count(*) as lessons,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','-1 months')) AND strftime(date('now'))GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch')

