package com.example.tutor.bd.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "studentTable")
data class StudentEntity(
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "secondName")
    val secondName: String,
    @ColumnInfo(name = "price")
    val price: Int,
    @ColumnInfo(name = "schoolClass")
    val schoolClass: Int,
) : Serializable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
data class studentForSchedule(
    @ColumnInfo
    val id:Int,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "secondName")
    val secondName: String
    )

