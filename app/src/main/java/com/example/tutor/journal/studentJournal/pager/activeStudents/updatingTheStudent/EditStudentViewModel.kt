package com.example.tutor.journal.studentJournal.pager.activeStudents.updatingTheStudent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.repository.StudentRepository

class EditStudentViewModel(val repository: StudentRepository, val fbRepository: FireBaseRepository) : ViewModel() {
    fun updateStudent(studentEntity: StudentEntity,firstName:String,secondName:String,schoolClass:Int,price:Int){
        repository.updateStudent(studentEntity.id,firstName,secondName,schoolClass,price)
        fbRepository.changeStudentFieldsToFireBase(studentEntity,firstName,secondName,schoolClass,price)
    }

}
class EditStudentViewModelFactory(private val repository: StudentRepository,private val fbRepository: FireBaseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditStudentViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return EditStudentViewModel(repository, fbRepository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}