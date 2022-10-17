package com.example.tutor.repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.tutor.bd.dao.StudentDAO
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.StudentForSchedule
import kotlinx.coroutines.flow.Flow


class StudentRepository(private val studentDAO: StudentDAO) {
    // Flow может возвращать несколько объектов по очереди или сразу, suspend же возвращает один
    // объект и завершается
    val allStudents: Flow<List<StudentEntity>> = studentDAO.getAllActiveStudents()
    val allInactiveStudents: Flow<List<StudentEntity>> = studentDAO.getAllInactiveStudents()
    val infoForSchedule: LiveData<MutableList<StudentForSchedule>>
    get()=studentDAO.getInfoForSchedule()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    // возвращает сгенерированный в БД  id для заполнения studentEntity при добавлении в FB
    suspend fun insertStudent(studentEntity: StudentEntity):Long {
        return  studentDAO.insertStudent(studentEntity)
    }
     fun insertAllStudent(studentList: List<StudentEntity>){
        studentDAO.insertAllStudent(studentList)
    }

    fun changeStudentActive(studentID:Int) = studentDAO.changeStudentActive(studentID)
    fun changeStudentActiveToTrue(studentID: Int) = studentDAO.changeStudentActiveToTrue(studentID)
    fun updateStudent(studentID:Int,firstName:String,secondName:String,schoolClass:Int,price:Int){
        studentDAO.updateStudent(studentID,firstName,secondName,schoolClass,price)
    }
}


