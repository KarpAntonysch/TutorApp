package com.example.tutor.main.addStudentToSchedule

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.bd.entities.StudentForSpinnerModel
import com.example.tutor.main.mainFragment.AlarmReceiver
import com.example.tutor.repository.ScheduleRepository
import com.example.tutor.toSpinnerModel
import kotlinx.coroutines.launch

class AddStudentToScheduleViewModel(
    private val repository: ScheduleRepository,
    private val app: Application,
) : AndroidViewModel(app) {
    var studentID: Int? = null

    // получение из List<StudentForSchedule> List<StudentForSpinnerModel>
    fun getNewList(infoList: MutableList<StudentForSchedule>): List<StudentForSpinnerModel> {
        return infoList.map { item -> item.toSpinnerModel() }
    }

    fun searchID(list: List<StudentForSpinnerModel>): Int {
        return list.indices.find { list[it] == list.first { o -> o.id == studentID } }!!
    }

    fun insert(scheduleEntity: ScheduleEntity) = viewModelScope.launch {
        repository.insertSchedule(scheduleEntity)
    }
    /*fun getPush(){
        val notificationManager = ContextCompat.getSystemService(
            app,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(app.getString(R.string.push_message),app)
    }*/
    fun setAlarm(time:Long){
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(app,AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(app,(0..2000000).random(),intent,PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,time,AlarmManager.INTERVAL_DAY,pendingIntent
        )
    }

}

// эта конструкция необходима для того, что б проинициализировать VM в фрагменте с передачей в
// конструктор ссылки на репозиторий
class AddStudentToScheduleViewModelFactory(private val repository: ScheduleRepository,private val app: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStudentToScheduleViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return AddStudentToScheduleViewModel(repository,app) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
