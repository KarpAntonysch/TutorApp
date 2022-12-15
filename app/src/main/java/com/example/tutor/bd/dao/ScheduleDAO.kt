package com.example.tutor.bd.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import com.example.tutor.bd.entities.StudentEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface ScheduleDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity):Long

    // запрос на получение id занятия из времени и id студента
    @Query("SELECT id FROM schedeulTable WHERE studentId = :studentId AND dateWithTime =:dateWithTime ")
    fun getScheduleId(studentId:Int,dateWithTime:Long):Int

    // запрос на получение списка всех занятий из БД
    @Query("SELECT * FROM schedeulTable ")
    fun getAllLessons():Flow<List<ScheduleEntity>>

    // запрос на добавление всех занятий из FB в ЛБД
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllLessons(scheduleList: List<ScheduleEntity> )

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
    fun getTotalPeriodAmount(period: String): Flow<Int>


    // запрос для получения количества занятий с понедельника по текущий день
    @Query("SELECT count(*) FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now')")
    fun getTotalWeekLessons(): Flow<Int>

    // запрос для получения количества занятий за текущий месяц ( с сегодняшнего дня по начало месяца)
    @Query("SELECT count(*) FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','start of month')) AND strftime('%s','now')")
    fun getTotalMonthLessons(): Flow<Int>

    @Query("SELECT count(*)FROM schedeulTable b WHERE CAST(b.dateWithTime/1000 AS integer) BETWEEN strftime('%s',date('now','start of month', :period)) AND strftime('%s','now')")
    fun getTotalPeriodLessons(period: String): Flow<Int>


    // Запрос с анотацией для получения MAP. Сумма за день от текущего дня до начала недели
    @MapInfo(keyColumn = "dateWithTime", valueColumn = "price")
    @Query("SELECT sum(a.price) AS price ,CAST(strftime('%w',b.dateWithTime/1000,'unixepoch') as integer) AS dateWithTime FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now') GROUP BY strftime('%w',b.dateWithTime/1000,'unixepoch') ")
    fun getMapOfWeek(): Map<Int, Int>

    // Запрос с анотацией для получения MAP. Получет сумму за каждый месяц в определенный временной интервал для полугода ="-5 month", для года = "-11month"
    @MapInfo(keyColumn = "dateWithTime", valueColumn = "price")
    @Query("SELECT  sum(a.price) AS price ,CAST(strftime('%m',b.dateWithTime/1000,'unixepoch')as integer) AS dateWithTime FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','start of month', :month)) AND strftime('%s','now') GROUP BY strftime('%m',b.dateWithTime/1000,'unixepoch')")
    fun getMapOfYear(month: String): Map<Int, Int>

    // все дни месяца, когда проводились занятия
    @MapInfo(keyColumn = "dateWithTime", valueColumn = "price")
    @Query("SELECT  sum(a.price) AS price ,strftime('%d.%m',b.dateWithTime/1000,'unixepoch') AS dateWithTime FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','start of month')) AND strftime('%s','now') GROUP BY strftime('%d.%m',b.dateWithTime/1000,'unixepoch')")
    fun getMapOfMonth():Map<String,Int>



    // Количкство занятий с начала недели до текущего дня
    @MapInfo(keyColumn = "dateWithTime", valueColumn = "lessons")
    @Query("SELECT count(*)AS lessons ,CAST(strftime('%w',b.dateWithTime/1000,'unixepoch') as integer) AS dateWithTime FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','weekday 1','-7 days')) AND strftime('%s','now') GROUP BY strftime('%w',b.dateWithTime/1000,'unixepoch')")
    fun getMapOfWeekLessons(): Map<Int,Int>

    // аналогично getMapOfYear, только для количества занятий
    @MapInfo(keyColumn = "dateWithTime", valueColumn = "lessons")
    @Query("SELECT  count(*) AS lessons ,CAST(strftime('%m',b.dateWithTime/1000,'unixepoch')as integer) AS dateWithTime FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','start of month', :month)) AND strftime('%s','now') GROUP BY strftime('%m',b.dateWithTime/1000,'unixepoch')")
    fun getMapOfPeriodLessons(month: String): Map<Int, Int>

    // количество занятий за каждый день месяца, когда проводились занятия
    @MapInfo(keyColumn = "dateWithTime", valueColumn = "lessons")
    @Query("SELECT  count(*) AS lessons ,strftime('%d.%m',b.dateWithTime/1000,'unixepoch') AS dateWithTime FROM schedeulTable b LEFT JOIN studentTable a ON a.id=b.studentId WHERE CAST(b.dateWithTime/1000 as integer) BETWEEN strftime('%s',date('now','start of month')) AND strftime('%s','now') GROUP BY strftime('%d.%m',b.dateWithTime/1000,'unixepoch')")
    fun getMapOfMonthLessons():Map<String,Int>


    @Query("SELECT dateWithTime FROM schedeulTable where studentId = :id ")
    fun getStudentLessons(id:Int): Flow<List<Long>>

    @Delete
    suspend fun deleteSchedule(scheduleEntity: ScheduleEntity)
}

