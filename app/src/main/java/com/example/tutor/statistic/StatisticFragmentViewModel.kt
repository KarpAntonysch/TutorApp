package com.example.tutor.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.tutor.bd.entities.Amount
import com.example.tutor.bd.entities.ScheduleWithStudent
import com.example.tutor.main.mainFragment.MainFragmentViewModel
import com.example.tutor.repository.ScheduleRepository

class StatisticFragmentViewModel(private val repository: ScheduleRepository) : ViewModel() {
    var totalWeekAmount : LiveData<Int> = repository.getTotalWeekAmount().asLiveData()
    var totalLessons : LiveData<Int> = repository.getTotalWeekLessons().asLiveData()
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