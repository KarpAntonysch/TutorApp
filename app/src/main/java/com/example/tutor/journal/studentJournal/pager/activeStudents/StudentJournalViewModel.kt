package com.example.tutor.journal.studentJournal.pager.activeStudents

import androidx.lifecycle.*
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.StudentForSchedule
import com.example.tutor.repository.StudentRepository

class StudentJournalViewModel(private val repository: StudentRepository) : ViewModel() {
    //Создаем переменную для получения списка из БД типа LD, которая инициализируется Flow из репозитория
    val allStudents: LiveData<List<StudentEntity>> = repository.allStudents.asLiveData()
    fun getInfo(): LiveData<MutableList<StudentForSchedule>>{
        return repository.infoForSchedule
    }


    fun changeStudentActive(studentID:Int) = repository.changeStudentActive(studentID)
}
class StudentJournalViewModelFactory(private val repository: StudentRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentJournalViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return StudentJournalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}
