package com.example.tutor.journal.studentJournal.pager.inactiveStudents

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.repository.StudentRepository

class InactiveStudentsViewModel(val repository: StudentRepository,
                                private val fbRepository: FireBaseRepository
) : ViewModel() {
    // получение списка неактивных студентов из БД
    var allInactiveStudents: LiveData<List<StudentEntity>> =
        repository.allInactiveStudents.asLiveData()
    //Изменение статуса студента на "активный" в БД
    fun returnStudentToActive(studentEntity: StudentEntity){
        repository.changeStudentActiveToTrue(studentEntity.id)
        fbRepository.changeStudentActiveToFireBase(studentEntity,true)
    }

    fun deleteStudent(studentEntity: StudentEntity) {
        repository.changeDeleteStatus(studentEntity.id)
        fbRepository.changeDeleteStatusToFireBase(studentEntity)
    }
}
class InactiveStudentsViewModelFactory(private val repository: StudentRepository,
                                       private val fbRepository: FireBaseRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InactiveStudentsViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return InactiveStudentsViewModel(repository,fbRepository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}