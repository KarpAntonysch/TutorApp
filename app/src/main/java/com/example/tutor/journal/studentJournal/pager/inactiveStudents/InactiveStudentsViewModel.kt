package com.example.tutor.journal.studentJournal.pager.inactiveStudents

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.repository.StudentRepository

class InactiveStudentsViewModel(val repository: StudentRepository) : ViewModel() {
    val allInactiveStudents: LiveData<List<StudentEntity>> =
        repository.allInactiveStudents.asLiveData()
    fun returnStudentToActive(studentID: Int) = repository.changeStudentActiveToTrue(studentID)
}
class InactiveStudentsViewModelFactory(private val repository: StudentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InactiveStudentsViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return InactiveStudentsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow VM")
    }
}