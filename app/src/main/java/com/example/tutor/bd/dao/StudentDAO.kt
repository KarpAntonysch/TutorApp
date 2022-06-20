package com.example.tutor.bd.dao

import androidx.room.*
import com.example.tutor.bd.StudentEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface StudentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(studentEntity: StudentEntity)
    @Delete
    suspend fun deleteStudent(studentEntity: StudentEntity)
    // добавляю функцию для получения списка всех студентов
    @Query("SELECT * FROM studentTable")
    fun getAllStudents(): Flow<List<StudentEntity>>
}