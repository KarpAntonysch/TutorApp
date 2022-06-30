package com.example.tutor.bd.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "schedeulTable")
data class ScheduleEntity(
    @ColumnInfo(name ="dateWithTime")
    val dateWithTime:Long,
    // указываем id студента,для дальнейшего связывания таблиц
    @ColumnInfo(name ="studentId")
    val studentId:Int
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
