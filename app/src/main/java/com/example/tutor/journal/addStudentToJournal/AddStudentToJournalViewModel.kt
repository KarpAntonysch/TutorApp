package com.example.tutor.journal.addStudentToJournal

import androidx.lifecycle.*
import com.example.tutor.bd.StudentEntity
import com.example.tutor.journal.StudentJournalViewModel
import com.example.tutor.journal.studentJournal.StudentApplication
import com.example.tutor.repository.StudentRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class AddStudentToJournalViewModel(private val repository: StudentRepository):ViewModel() {



    // Создаем метод-оболочку insert, который вызывает insert из репозитория
    fun insert(studentEntity: StudentEntity)=viewModelScope.launch {
        repository.insert(studentEntity)
    }
}

class AddStudentToJournalViewModelFactory(private val repository: StudentRepository): ViewModelProvider.Factory{
    override fun <T:ViewModel> create(modelClass:Class<T>): T{
        if (modelClass.isAssignableFrom(AddStudentToJournalViewModel::class.java)){
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return AddStudentToJournalViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknow VM")
    }
}