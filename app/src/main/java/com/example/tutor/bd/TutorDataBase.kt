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

@Database(entities = [StudentEntity::class, ScheduleEntity::class], version = 9)
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
                    .addMigrations(MIGRATION_8_9)
                    .allowMainThreadQueries()
                    .build()
                database as TutorDataBase
            } else {
                database as TutorDataBase
            }
        }
    }
}
// Оставил миграции, для собственного понимания изменений
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
// миграция для изменения типа переменной dateWithTime на стринг
val MIGRATION_6_7 = object : Migration(6, 7) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE scheduleTable11 (" +
                "dateWithTime TEXT NOT NULL, " +
                "studentId INTEGER NOT NULL," +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        database.execSQL("INSERT INTO scheduleTable11 (dateWithTime, studentId, id) SELECT dateWithTime, studentId, id FROM schedeulTable")
        // Delete the old table
        database.execSQL("DROP TABLE schedeulTable")
        // Rename the new table to the old table's name so that the rest of your code can recognize this table as the former one.
        database.execSQL("ALTER TABLE scheduleTable11 RENAME TO schedeulTable")
    }
}
val MIGRATION_7_8 = object : Migration(7, 8) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE scheduleTable11 (" +
                "dateWithTime INTEGER NOT NULL, " +
                "studentId INTEGER NOT NULL," +
                " id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)")
        database.execSQL("INSERT INTO scheduleTable11 (dateWithTime, studentId, id) SELECT dateWithTime, studentId, id FROM schedeulTable")
        // Delete the old table
        database.execSQL("DROP TABLE schedeulTable")
        // Rename the new table to the old table's name so that the rest of your code can recognize this table as the former one.
        database.execSQL("ALTER TABLE scheduleTable11 RENAME TO schedeulTable")
    }

}
val MIGRATION_8_9 = object : Migration(8, 9) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE studentTable ADD COLUMN activeStatus INTEGER NOT NULL DEFAULT 1")
    }
}





