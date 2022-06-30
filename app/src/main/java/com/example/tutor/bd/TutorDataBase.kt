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
// (добавление таблицы scheduleTable)
val MIGRATION_1_4 = object : Migration(1, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE scheduleTable" +
                " (dateWithTime INTEGER NOT NULL," +
                " studentId INTEGER NOT NULL," +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
    }
}
//(добавление в таблицу scheduleTable столбец для времени)
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE scheduleTable ADD COLUMN time INTEGER ")
    }
}
//  Колонка "время" была не нужна. Эта миграция ее удалила.
val MIGRATION_5_6 = object : Migration(5, 6) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // создал новую таблицу под замену. С необходимыми полями
        database.execSQL("CREATE TABLE scheduleTable1 (" +
                "dateWithTime INTEGER NOT NULL, " +
                "studentId INTEGER NOT NULL," +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        // Удалил старую таблицу, из которой удаляю колонку. В файле с таблицей удаляем колонку т.е
        // приводим ее к необходимому нам виду
        database.execSQL("DROP TABLE scheduleTable")
        // Переименовал новую таблицу старым названием
        database.execSQL("ALTER TABLE scheduleTable1 RENAME TO scheduleTable")
    }
}




