package com.example.tutor.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tutor.bd.dao.ScheduleDAO
import com.example.tutor.bd.dao.StudentDAO
import com.example.tutor.bd.entities.StudentEntity

@Database(entities = [StudentEntity::class], version = 1)
abstract class TutorDataBase : RoomDatabase() {
    // для каждой DAO определяем метод для свзязи с этой DAO
    abstract fun getStudentDAO(): StudentDAO
    abstract fun getScheduleDAO(): ScheduleDAO

    companion object {
        @Volatile// делает объект видимым для всех потоков
        private var database: TutorDataBase? = null// определяем базу данных

        @Synchronized//защита от одновременного выполнения несколькими потоками
        fun getDataBase(context: Context): TutorDataBase { //  инициализируем БД
            return if (database == null) {
                database = Room.databaseBuilder(context, TutorDataBase::class.java, "bd").build()
                database as TutorDataBase
            } else {
                database as TutorDataBase
            }
        }
    }
}