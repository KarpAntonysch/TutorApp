package com.example.tutor.bd.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "schedeulTable")
data class ScheduleEntity(
    @ColumnInfo(name ="dateWithTime")
    val dateWithTime:Long =0,
    // указываем id студента,для дальнейшего связывания таблиц
    @ColumnInfo(name ="studentId")
    val studentId:Int = 0,
    @ColumnInfo(name ="notificationDelay")
    val notificationDelay:Long = 0
) : Serializable, Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}
