package com.example.tutor.main.addStudentToSchedule

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.*
import com.example.tutor.R
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.bd.entities.StudentForSpinnerModel
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.notifications.AlarmReceiver
import com.example.tutor.repository.ScheduleRepository
import com.example.tutor.toSpinnerModel
import kotlinx.coroutines.launch
import java.util.*

class AddStudentToScheduleViewModel(
    private val repository: ScheduleRepository,
    private val app: Application,
    private val fbRepository: FireBaseRepository,
) : AndroidViewModel(app) {
    var studentID: Int? = null
    private var delay: Long = 600000
    val notificationCondition: MutableLiveData<String> =
        MutableLiveData("10 мин.")//Условие для настройки боттомшит с оповещениями
    val periodCondition: MutableLiveData<String> =
        MutableLiveData("day")// Условие для настройки боттомшит с периодом
    val notificationDelay: MutableLiveData<Long> =
        MutableLiveData(600000)// параметр для заполнения свойства notificationDelay в scheduleEntity


    // получение из List<StudentForSchedule> List<StudentForSpinnerModel>
    fun getNewList(infoList: MutableList<StudentForSchedule>): List<StudentForSpinnerModel> {
        return infoList.map { item -> item.toSpinnerModel() }
    }

    fun searchID(list: List<StudentForSpinnerModel>): Int {
        return list.indices.find { list[it] == list.first { o -> o.id == studentID } }!!
    }

    fun insertWithPeriod(scheduleEntity: ScheduleEntity) {
        if (periodCondition.value == "day") {
            insert(scheduleEntity)
            getScheduleId(scheduleEntity) // обновление id в FB
        }
        if (periodCondition.value == "week") {
            periodInserting(scheduleEntity, 1)
        }
        if (periodCondition.value == "month") {
            periodInserting(scheduleEntity, 3)
        }
        if (periodCondition.value == "halfYear") {
            periodInserting(scheduleEntity, 26)
        }
        if (periodCondition.value == "year") {
            periodInserting(scheduleEntity, 51)
        }
    }

    private fun periodInserting(scheduleEntity: ScheduleEntity, number: Int) {
        val week = 604800000L
        val numberList = (0..number).toList()
        val weekList = numberList.map { it * week }
        weekList.forEach {
            val scheduleEntity1 = ScheduleEntity(scheduleEntity.dateWithTime + it,
                scheduleEntity.studentId,
                scheduleEntity.notificationDelay)
            insert(scheduleEntity1)
            getScheduleId(scheduleEntity1) // обновление id в FB
        }
    }

    private fun getScheduleId(scheduleEntity: ScheduleEntity) = fbRepository
        .changeScheduleIDToFireBase(scheduleEntity, repository
            .getScheduleId(scheduleEntity.studentId, scheduleEntity.dateWithTime))

    private fun insert(scheduleEntity: ScheduleEntity) {

        if (notificationCondition.value == "10 мин.") {
            delay = 600000
            setAlarm(scheduleEntity.dateWithTime - delay, app.getString(R.string.push_message10))// перенес сюда вместо отдельного метода в фрагменте
        }
        if (notificationCondition.value == "15 мин.") {
            delay = 900000
            setAlarm(scheduleEntity.dateWithTime - delay,app.getString(R.string.push_message15))// перенес сюда вместо отдельного метода в фрагменте
        }
        if (notificationCondition.value == "30 мин.") {
            delay = 1800000
            setAlarm(scheduleEntity.dateWithTime - delay,app.getString(R.string.push_message30))// перенес сюда вместо отдельного метода в фрагменте
        }

        viewModelScope.launch {
            repository.insertSchedule(scheduleEntity)
        }
        viewModelScope.launch {
            fbRepository.addLessonsToFBCloud(scheduleEntity)//добавление занятия в FB
        }
    }

    private fun setAlarm(time: Long, message:String) {
        val cal: Calendar = Calendar.getInstance()
        if (cal.timeInMillis < time) {
            val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(app, AlarmReceiver::class.java)
            intent.putExtra("notificationMessage",message)// передача сообщения для разных оповещений в BroadcastReceiver, при вызове этого ресивера
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
    private val fbRepository: FireBaseRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStudentToScheduleViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return AddStudentToScheduleViewModel(repository, app, fbRepository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}

