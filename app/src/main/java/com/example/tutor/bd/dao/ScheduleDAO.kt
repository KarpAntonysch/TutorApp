package com.example.tutor.bd.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tutor.bd.entities.AmountByDays
import com.example.tutor.bd.entities.LessonsByDays
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import kotlinx.coroutines.flow.Flow
import java.time.Month


@Dao
interface ScheduleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)

    // запрос для получения списка студентов(запрос к двум таблицам. Получения дня, времени, свойств студента) конкретный день
    @Query("SELECT * FROM schedeulTable WHERE strftime('%d-%m-%Y',dateWithTime/1000.0,'unixepoch') = :date")
    fun getScheduleForDay(date: String): LiveData<List<ScheduleWithStudent>>

    //запрос для получения заработанной суммы с понедельника по текущий день
    @Query("SELECT sum(a.price)FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')")
    fun getTotalWeekAmount(): Flow<Int>
    // запрос на получение заработанной суммы за текущий месяц ( с сегодняшнего дня по начало месяца)
    @Query("SELECT sum(a.price)FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','start of month')) AND strftime('%s','now')")
    fun getTotalMonthAmount(): Flow<Int>
    // запрос на получение заработанной суммы за период ( 6мес., год) от текущего дня. т.е. с текущей даты до начала месяца (как бы текущий месяц) - 5 мес для полугода или -11 мес для года
    @Query("SELECT sum(a.price)FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','start of month', :period)) AND strftime('%s','now')")
    fun getTotalPeriodAmount(period:String): Flow<Int>
    // Запрос с анотацией для получения MAP. Получет сумму за каждый месяц в определенный временной интервал для полугода ="-5 month", для года = "-11month"
    @MapInfo(keyColumn = "dateWithTime", valueColumn = "price")
    @Query("SELECT  sum(a.price) AS price ,strftime('%m',b.dateWithTime/1000,'unixepoch') AS dateWithTime FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','start of month', :month)) AND strftime('%s','now') GROUP BY strftime('%m',b.dateWithTime/1000,'unixepoch')")
    fun getMapOfPrice(month:String): Map<Long,Int>


    // запрос на получение заработанной суммы в каждый день недели с пн по текущий день
    @Query("SELECT sum(a.price) AS price ,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now') GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch');")
    fun getAmountByDaysOfWeek(): Flow<List<AmountByDays>>
    // запрос для получения количества занятий с понедельника по текущий день
    @Query("SELECT count(*) FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')")
    fun getTotalWeekLessons(): Flow<Int>

    // запрос для получения количества уроков на каждый день недели начиная с пн по текущий день
    @Query("SELECT count(*) as lessons,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch')")
    fun getLessonsByDaysOfWeek(): Flow<MutableList<LessonsByDays>>



    @Query("SELECT * from schedeulTable WHERE date('now','weekday 1','-7 days')")
    fun getStud(): LiveData<List<ScheduleWithStudent>>

    @Delete
    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}

// для полного месяца, а не с начала месяца до текущего дня
// SELECT count(*) as lessons,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','start of month')) AND strftime(date('now','start of month','+1 month','-1 day'))GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch')

// количество занятий за месяц (дата - месяц)
//SELECT count(*) as lessons,strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch') AS date FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','-1 months')) AND strftime(date('now'))GROUP BY strftime('%d-%m-%Y',b.dateWithTime/1000,'unixepoch')

