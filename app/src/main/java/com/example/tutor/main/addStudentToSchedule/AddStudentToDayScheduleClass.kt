package com.example.tutor.main.addStudentToSchedule

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.convertLongToTime
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class AddStudentToDayScheduleClass {

    @RequiresApi(Build.VERSION_CODES.O)
    fun formattedCurrentDate(getCurrentDate: Long, timeFromPicker: Long): Long {
        val jointDate = getCurrentDate.convertLongToTime("dd.MM.yyyy") +
                timeFromPicker.convertLongToTime(" HH:mm")
        //Перевожу String в LocalDateTime с помощью DateTimeFormatter
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
        val formatDate = LocalDateTime.parse(jointDate, formatter)
        // Преобразование LocalDateTime в миллисекнды(Long), исспользуется ZoneID т.к.
        // LocalDateTime изначально не имеет часового пояса
        return formatDate.atZone(ZoneId.of("Europe/Moscow"))
            .toInstant().toEpochMilli()
    }

    @SuppressLint("NewApi")
     fun getScheduleValues(formattedCurrentDate:Long,studentID:Int): ScheduleEntity {
        val dateWithTime: Long = formattedCurrentDate
        val studentId: Int = studentID
        return ScheduleEntity(dateWithTime, studentId)
    }
}