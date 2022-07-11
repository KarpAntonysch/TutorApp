package com.example.tutor.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.tutor.bd.dao.StudentDAO
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.studentForSchedule
import kotlinx.coroutines.flow.Flow


class StudentRepository(private val studentDAO: StudentDAO) {
    // Flow может возвращать несколько объектов по очереди или сразу, suspend же возвращает один
    // объект и завершается
    val allStudents: Flow<List<StudentEntity>> = studentDAO.getAllActiveStudents()
    val infoForSchedule: LiveData<MutableList<studentForSchedule>>
    get()=studentDAO.getInfoForSchedule()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertStudent(studentEntity: StudentEntity) {
        studentDAO.insertStudent(studentEntity)
    }
    suspend fun deleteStudent(studentEntity: StudentEntity){
        studentDAO.deleteStudent(studentEntity)
    }
}


