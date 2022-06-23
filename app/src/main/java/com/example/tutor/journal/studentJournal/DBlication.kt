package com.example.tutor.journal.studentJournal

import android.app.Application
import com.example.tutor.bd.TutorDataBase
import com.example.tutor.repository.ScheduleRepository
import com.example.tutor.repository.StudentRepository

// Для создания ЕДИНСТВЕННЫХ экземпляров БД и репозитория создаем их как членов Application.
//После этого они будут просто извлекаться из приложения, когда необходимо
class DBlication: Application() {
    val dataBase by lazy { TutorDataBase.getDataBase(this) }
    val studentRepository by lazy { StudentRepository(dataBase.getStudentDAO()) }
    val scheduleRepository by lazy { ScheduleRepository(dataBase.getScheduleDAO()) }
//делегирование свойств оператором by Lazy  позволяет создавать БД и Реп, только тогда,
// когда необходимо, а не при запуске приложения
}