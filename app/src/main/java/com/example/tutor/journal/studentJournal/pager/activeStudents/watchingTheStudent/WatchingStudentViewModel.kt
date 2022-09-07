package com.example.tutor.journal.studentJournal.pager.activeStudents.watchingTheStudent

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.tutor.repository.ScheduleRepository

class WatchingStudentViewModel(val repository: ScheduleRepository) : ViewModel() {
    fun getStudentLessons(id:Int): LiveData<List<Long>> = repository.getStudentLessons(id).asLiveData()
}
class WatchingStudentViewModelFactory(private val repository: ScheduleRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WatchingStudentViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return WatchingStudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}