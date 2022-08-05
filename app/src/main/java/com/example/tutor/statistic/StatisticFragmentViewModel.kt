package com.example.tutor.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.tutor.bd.entities.AmountByDays
import com.example.tutor.bd.entities.LessonsByDays
import com.example.tutor.repository.ScheduleRepository

class StatisticFragmentViewModel(private val repository: ScheduleRepository) : ViewModel() {
    var totalWeekAmount : LiveData<Int> = repository.getTotalWeekAmount().asLiveData()
    var totalMonthAmount : LiveData<Int> = repository.getTotalMonthAmount().asLiveData()
    fun totalPeriodAmount (period: String) : LiveData<Int> = repository.getTotalPeriodAmount(period).asLiveData()
    fun getMapOfWeek(): Map<Int,Int> = repository.getMapOfWeek()
    fun getMapOfMonth():Map<String,Int> = repository.getMapOfMonth()
    fun getMapOfYear(month:String) : Map<Int,Int> = repository.getMapOfYear(month)


    var totalLessons : LiveData<Int> = repository.getTotalWeekLessons().asLiveData()
    var lessonsByDaysOfWeek:LiveData<MutableList<LessonsByDays>> = repository.getLessonsByDaysOfWeek().asLiveData()
    var amountByDaysOfWeek:LiveData<List<AmountByDays>> =repository.getAmountByDaysOfWeek().asLiveData()
}
class StatisticFragmentViewModelFactory(private val repository: ScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StatisticFragmentViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return StatisticFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow VM")
    }
}