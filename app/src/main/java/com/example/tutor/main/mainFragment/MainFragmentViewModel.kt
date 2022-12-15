package com.example.tutor.main.mainFragment

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.*
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.ScheduleWithStudent
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.notifications.AlarmReceiver
import com.example.tutor.repository.ScheduleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val repository: ScheduleRepository,private val app: Application,
                            private val fbRepository: FireBaseRepository
): AndroidViewModel(app){

    fun getScheduleOfDay(date:String) :LiveData<List<ScheduleWithStudent>> = repository.scheduleOfDay(date)
    fun deleteSchedule(scheduleEntity: ScheduleEntity) =
        viewModelScope.launch {
            repository.deleteSchedule(scheduleEntity)
        }
    fun cancelNotification(requestCode:Int){
        val alarmManager = app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(app, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(app,requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        alarmManager.cancel(pendingIntent)
        Log.v("eee","cancel $requestCode")

    }
    fun deleteScheduleFromFB(scheduleEntity: ScheduleEntity){
        fbRepository.deleteScheduleFromFB(scheduleEntity)
    }

   // private val allLessons: LiveData<List<ScheduleEntity>> = repository.getAllLessons().asLiveData()

   /* fun fillingScheduleDataBase(){
        CoroutineScope(Dispatchers.IO).launch {
            val fbLessonList = fbRepository.fbLessonList()
            if (allLessons.value.isNullOrEmpty() && !fbLessonList.isNullOrEmpty()){
                repository.insertAllLessons(fbLessonList)
            }
        }
    }*/

}


class MainFragmentViewModelFactory(private val repository: ScheduleRepository, private val app: Application,
                                   private val fbRepository: FireBaseRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return MainFragmentViewModel(repository,app,fbRepository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}