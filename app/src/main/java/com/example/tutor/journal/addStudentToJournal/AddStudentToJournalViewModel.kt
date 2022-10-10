package com.example.tutor.journal.addStudentToJournal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.fireBase.FireBaseRepository
import com.example.tutor.repository.StudentRepository
import kotlinx.coroutines.launch

class AddStudentToJournalViewModel(
    private val repository: StudentRepository,
    private val fbRepository: FireBaseRepository
) : ViewModel() {
    // Создаем метод-оболочку insert, который вызывает insert из репозитория
    fun insert(studentEntity: StudentEntity) = viewModelScope.launch {
        val id = repository.insertStudent(studentEntity)// добавление ученика в БД
        studentEntity.id = id.toInt()// сет сгенерированного базой данных id
        fbRepository.addStudentToFBCloud(studentEntity)// добавление ученика в FB
    }
}

// эта конструкция необходима для того, что б проинициализировать VM в фрагменте с передачей в
// конструктор ссылки на репозиторий
class AddStudentToJournalViewModelFactory(
    private val repository: StudentRepository,
    private val fbRepository: FireBaseRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddStudentToJournalViewModel::class.java)) {
            //тестовая аннотация для обнаружения ошибок. Означает, что тестовый метод не будет включен в набор тестов
            @Suppress("UNCHECKED_CAST")
            return AddStudentToJournalViewModel(repository, fbRepository) as T
        }
        throw IllegalArgumentException("Unknown VM")
    }
}