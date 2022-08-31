package com.example.tutor.journal.studentJournal.pager.activeStudents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tutor.repository.StudentRepository

class EditStudentViewModel(val repository: StudentRepository) : ViewModel() {
    fun updateStudent(studentID:Int,firstName:String,secondName:String,schoolClass:Int,price:Int) =
        repository.updateStudent(studentID,firstName,secondName,schoolClass,price)
}
class EditStudentViewModelFactory(private val repository: StudentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditStudentViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return EditStudentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow VM")
    }
}