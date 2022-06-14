package com.example.tutor.journal

import androidx.lifecycle.*
import com.example.tutor.bd.StudentEntity
import com.example.tutor.repository.StudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class StudentJournalViewModel(private val repository: StudentRepository) : ViewModel() {
    //Создаем переменную для получения списка из БД типа LD, которая инициализируется Flow из репозитория
    val allStudents: LiveData<List<StudentEntity>> = repository.allStudents.asLiveData()
}