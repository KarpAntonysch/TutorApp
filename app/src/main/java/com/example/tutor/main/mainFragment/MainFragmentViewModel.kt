package com.example.tutor.main.mainFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tutor.bd.entities.ScheduleWithStudent
import com.example.tutor.repository.ScheduleRepository

class MainFragmentViewModel(private val repository: ScheduleRepository): ViewModel(){

    fun getScheduleOfDay(date:String) :List<ScheduleWithStudent> = repository.scheduleOfDay(date)
}
class MainFragmentViewModelFactory(private val repository: ScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return MainFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow VM")
    }
}