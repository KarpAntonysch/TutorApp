package com.example.tutor.bd

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tutor.bd.dao.ScheduleDAO
import com.example.tutor.bd.dao.StudentDAO
import com.example.tutor.bd.entities.ScheduleEntity
import com.example.tutor.bd.entities.StudentEntity

@Database(entities = [StudentEntity::class, ScheduleEntity::class], version = 6)
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
                database = Room.databaseBuilder(context, TutorDataBase::class.java, "bd")
                    .addMigrations(MIGRATION_5_6)
                    .allowMainThreadQueries()
                    .build()
                database as TutorDataBase
            } else {
                database as TutorDataBase
            }
        }
    }
}

// здесь были миграции MIGRATION_1_4 (добавление таблицы scheduleTable) и MIGRATION_4_5 (добавление
// в таблицу scheduleTable столбец для времени). Колонка "время" была не нужна. Эта миграция ее удалила.
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE scheduleTable1 (" +
                "dateWithTime INTEGER NOT NULL, " +
                "studentId INTEGER NOT NULL," +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        // Delete the old table
        database.execSQL("DROP TABLE scheduleTable")
        // Rename the new table to the old table's name so that the rest of your code can recognize this table as the former one.
        database.execSQL("ALTER TABLE scheduleTable1 RENAME TO scheduleTable")
    }
}




