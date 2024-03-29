package com.example.tutor.bd.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.tutor.bd.entities.StudentEntity
import com.example.tutor.bd.entities.StudentForSchedule
import kotlinx.coroutines.flow.Flow


@Dao
interface StudentDAO {
    // возвращает тип Long. Это будет studentEntity.id так как это primaryKey
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(studentEntity: StudentEntity):Long
    // для добавления целого списка студентов из FB
    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAllStudent(studentList: List<StudentEntity> )
    // добавляю функцию для получения списка всех  студентов
    @Query("SELECT * FROM studentTable")
    fun getAllStudents(): Flow<List<StudentEntity>>
    // добавляю функцию для получения списка всех активных студентов
    @Query("SELECT * FROM studentTable WHERE activeStatus = 1")
    fun getAllActiveStudents(): Flow<List<StudentEntity>>
    // для получения списка всех неактивных студентов
    @Query("SELECT * FROM studentTable WHERE activeStatus = 0 AND deleteStatus = 1")
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
    @Query("UPDATE studentTable SET firstName = :firstName, secondName = :secondName, schoolClass = :schoolClass, price = :price, phoneNumber = :phoneNumber  WHERE id = :studentID")
    fun updateStudent(studentID:Int,firstName:String,secondName:String,schoolClass:Int,price:Int, phoneNumber:String)
    // "удаление" студента. Перестает быть виден в списке выпускников
    @Query("UPDATE studentTable SET deleteStatus = 0 WHERE id =:studentID")
    fun changeDeleteStatus(studentID: Int)
}
