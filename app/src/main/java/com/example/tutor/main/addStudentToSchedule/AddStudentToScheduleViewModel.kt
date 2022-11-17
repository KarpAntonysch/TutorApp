package com.example.tutor.main.addStudentToSchedule

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.bd.entities.StudentForSpinnerModel
import com.example.tutor.notifications.AlarmReceiver
import com.example.tutor.repository.ScheduleRepository
import com.example.tutor.toSpinnerModel
import kotlinx.coroutines.launch
import java.util.*

class AddStudentToScheduleViewModel(
    private val repository: ScheduleRepository,
    private val app: Application,
) : AndroidViewModel(app) {
    var studentID: Int? = null
    private var delay:Long = 600000
    val notificationCondition:MutableLiveData<String> = MutableLiveData("ten")//Условие для настройки боттомшит с оповещениями
    val periodCondition:MutableLiveData<String> = MutableLiveData("day")// Условие для настройки боттомшит с периодом
    val notificationDelay:MutableLiveData<Long> = MutableLiveData(600000)// параметр для заполнения свойства notificationDelay в scheduleTntity
    // получение из List<StudentForSchedule> List<StudentForSpinnerModel>
    fun getNewList(infoList: MutableList<StudentForSchedule>): List<StudentForSpinnerModel> {
        return infoList.map { item -> item.toSpinnerModel() }
    }

    fun searchID(list: List<StudentForSpinnerModel>): Int {
        return list.indices.find { list[it] == list.first { o -> o.id == studentID } }!!
    }
    fun insertWithPeriod(scheduleEntity: ScheduleEntity){
        if (periodCondition.value=="day"){
            insert(scheduleEntity)
        }
        if (periodCondition.value == "week"){
            periodInserting(scheduleEntity,1)
        }
        if (periodCondition.value == "month"){
            periodInserting(scheduleEntity,3)
        }
        if (periodCondition.value == "halfYear"){
            periodInserting(scheduleEntity,26)
        }
        if (periodCondition.value == "year"){
            periodInserting(scheduleEntity,51)
        }
    }
    private fun periodInserting(scheduleEntity: ScheduleEntity, number:Int){
        val week = 604800000L
        val numberList = (0..number).toList()
        val weekList = numberList.map { it * week }
        weekList.forEach {
            val scheduleEntity1 = ScheduleEntity(scheduleEntity.dateWithTime+it,scheduleEntity.studentId,it)// уточнить и проверить !!!!
            insert(scheduleEntity1)
        }
    }
    private fun insert(scheduleEntity: ScheduleEntity) {

        if (notificationCondition.value == "ten"){
            delay=600000
            setAlarm(scheduleEntity.dateWithTime-delay)// перенес сюда вместо отдельного метода в фрагменте
        }
        if (notificationCondition.value == "fifteen"){
            delay=900000
            setAlarm(scheduleEntity.dateWithTime-delay)// перенес сюда вместо отдельного метода в фрагменте
        }
        if (notificationCondition.value == "thirty"){
            delay=1800000
            setAlarm(scheduleEntity.dateWithTime-delay)// перенес сюда вместо отдельного метода в фрагменте
        }

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

    fun updateNotificationDelay(delay:Long,id:Int) = repository.updateNotificationDelay(delay,id)
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

