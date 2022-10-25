package com.example.tutor.bd.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "studentTable")
data class StudentEntity(
    @ColumnInfo(name = "firstName")
    var firstName: String="",
    @ColumnInfo(name = "secondName")
    var secondName: String="",
    @ColumnInfo(name = "price")
    var price: Int=0,
    @ColumnInfo(name = "schoolClass")
    var schoolClass: Int=0,
    @ColumnInfo(name = "activeStatus")
    var activeStatus:Boolean = true,
    @ColumnInfo(name = "deleteStatus")
    var deleteStatus:Boolean = true,
    @ColumnInfo(name = "phoneNumber")
    var phoneNumber:String? = null
) : Serializable, Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}
data class StudentForSchedule(
    @ColumnInfo
    val id:Int,
    @ColumnInfo(name = "firstName")
    val firstName: String,
    @ColumnInfo(name = "secondName")
    val secondName: String
    )

