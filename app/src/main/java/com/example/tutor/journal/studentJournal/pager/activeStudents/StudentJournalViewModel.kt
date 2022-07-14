package com.example.tutor.journal

import androidx.lifecycle.*
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.studentForSchedule
import com.example.tutor.repository.StudentRepository
import kotlinx.coroutines.launch

class StudentJournalViewModel(private val repository: StudentRepository) : ViewModel() {
    //Создаем переменную для получения списка из БД типа LD, которая инициализируется Flow из репозитория
    val allStudents: LiveData<List<StudentEntity>> = repository.allStudents.asLiveData()
    fun getInfo(): LiveData<MutableList<studentForSchedule>>{
        return repository.infoForSchedule
    }

    fun deleteStudent(studentEntity: StudentEntity)=viewModelScope.launch {
        repository.deleteStudent(studentEntity)
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
        throw IllegalArgumentException("Unknow VM")
    }
}