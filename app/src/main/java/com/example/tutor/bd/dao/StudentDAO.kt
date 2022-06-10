package com.example.tutor.bd.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tutor.bd.StudentEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface StudentDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(studentEntity: StudentEntity)
    // добавляю функцию для получения списка всех студентов
    @Query("SELECT * FROM studentTable")
    fun getAllStudents(): Flow<List<StudentEntity>>
}