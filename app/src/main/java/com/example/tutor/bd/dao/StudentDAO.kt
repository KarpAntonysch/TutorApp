package com.example.tutor.bd.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.StudentForSchedule
import kotlinx.coroutines.flow.Flow


@Dao
interface StudentDAO {
    // возвращает Long это будет studentEntity.id так как это primaryKey
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(studentEntity: StudentEntity):Long
    @Delete
    suspend fun deleteStudent(studentEntity: StudentEntity)// не использую
    // добавляю функцию для получения списка всех активных студентов
    @Query("SELECT * FROM studentTable WHERE activeStatus = 1")
    fun getAllActiveStudents(): Flow<List<StudentEntity>>
    // для получения списка всех неактивных студентов
    @Query("SELECT * FROM studentTable WHERE activeStatus = 0")
    fun getAllInactiveStudents(): Flow<List<StudentEntity>>
    // функция для получения информация для спинера из второй таблицы БД (только активные ученики)
    @Query("SELECT id, firstName,secondName FROM studentTable WHERE activeStatus = 1 ")
    fun getInfoForSchedule(): LiveData<MutableList<StudentForSchedule>>
    // изменнение активности студента на false. Т.е. студент становится неактивным
    @Query("UPDATE studentTable SET activeStatus = 0 WHERE id = :studentID ")
    fun changeStudentActive(studentID:Int)
    @Query("UPDATE studentTable SET activeStatus = 1 WHERE id =:studentID")
    fun changeStudentActiveToTrue(studentID: Int)
    // изменение параметров студента
    @Query("UPDATE studentTable SET firstName = :firstName, secondName = :secondName, schoolClass = :schoolClass, price = :price WHERE id = :studentID")
    fun updateStudent(studentID:Int,firstName:String,secondName:String,schoolClass:Int,price:Int)

}
