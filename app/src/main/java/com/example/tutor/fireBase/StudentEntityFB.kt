package com.example.tutor.fireBase

data class StudentEntityFB(
    val firstName: String ,
    val secondName: String ,
    val price: Int ,
    val schoolClass: Int ,
    val activeStatus: Boolean = true,
    val id: Int
)


