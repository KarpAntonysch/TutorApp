package com.example.tutor.main.addStudentToSchedule

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.example.tutor.R
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.bd.entities.StudentForSpinnerModel
import com.example.tutor.notifications.AlarmReceiver
import com.example.tutor.repository.ScheduleRepository
import com.example.tutor.toSpinnerModel
import kotlinx.coroutines.Delay
import kotlinx.coroutines.launch
import java.util.*

class AddStudentToScheduleViewModel(
    private val repository: ScheduleRepository,
    private val app: Application,
) : AndroidViewModel(app) {
    var studentID: Int? = null
    var delay:Long = 600000
    val sss:MutableLiveData<Int> = MutableLiveData(0)
    // получение из List<StudentForSchedule> List<StudentForSpinnerModel>
    fun getNewList(infoList: MutableList<StudentForSchedule>): List<StudentForSpinnerModel> {
        return infoList.map { item -> item.toSpinnerModel() }
    }

    fun searchID(list: List<StudentForSpinnerModel>): Int {
        return list.indices.find { list[it] == list.first { o -> o.id == studentID } }!!
    }

    fun insert(scheduleEntity: ScheduleEntity) {

        if (sss.value == 1 || sss.value == 0){
            delay=600000
            setAlarm(scheduleEntity.dateWithTime-delay)// перенес сюда вместо отдельного метода в фрагменте
        }
        if (sss.value==2){
            delay=900000
            setAlarm(scheduleEntity.dateWithTime-delay)// перенес сюда вместо отдельного метода в фрагменте
        }
        if (sss.value==3){
            delay=1800000
            setAlarm(scheduleEntity.dateWithTime-delay)// перенес сюда вместо отдельного метода в фрагменте
        }
        if (sss.value==4)

        viewModelScope.launch {
            repository.insertSchedule(scheduleEntity)
        }
    }

    private fun setAlarm(time: Long) {
        val cal: Calendar = Calendar.getInstance()
        if (cal.timeInMillis < time) {
            val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(app, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(app, time.toInt(), intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent)
        }
    }
}

// эта конструкция необходима для того, что б проинициализировать VM в фрагменте с передачей в
// конструктор ссылки на репозиторий
class AddStudentToScheduleViewModelFactory(
    private val repository: ScheduleRepository,
    private val app: Application,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStudentToScheduleViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return AddStudentToScheduleViewModel(repository, app) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}

