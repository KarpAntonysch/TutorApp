package com.example.tutor.statistic.lessons

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.tutor.repository.ScheduleRepository

class LessonsStatisticFragmentViewModel(private val repository: ScheduleRepository) : ViewModel() {
    var totalWeekLessons: LiveData<Int> = repository.getTotalWeekLessons().asLiveData()
    var totalMonthLessons: LiveData<Int> = repository.getTotalMonthLessons().asLiveData()
    fun totalPeriodLessons(period:String): LiveData<Int> =
        repository.getTotalPeriodLessons(period).asLiveData()

    fun getMapOfWeekLessons() : Map<Int,Int> = repository.getMapOfWeekLessons()
    fun getMapOfMonthLessons() : Map<String,Int> = repository.getMapOfMonthLessons()
    fun getMapOfPeriodLessons(month: String) : Map<Int,Int> = repository.getMapOfPeriodLessons(month)

}

class LessonsStatisticFragmentViewModelFactory(private val repository: ScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LessonsStatisticFragmentViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return LessonsStatisticFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}