package com.example.tutor.journal

import androidx.lifecycle.*
import com.example.tutor.bd.StudentEntity
import com.example.tutor.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.text.FieldPosition

class StudentJournalViewModel(private val repository: StudentRepository) : ViewModel() {
    //Создаем переменную для получения списка из БД типа LD, которая инициализируется Flow из репозитория
    val allStudents: LiveData<List<StudentEntity>> = repository.allStudents.asLiveData()

    fun deleteStudent(studentEntity: StudentEntity)=viewModelScope.launch {
        repository.deleteStudent(studentEntity)
    }

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