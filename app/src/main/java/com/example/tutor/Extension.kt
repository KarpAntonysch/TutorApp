package com.example.tutor

import java.text.SimpleDateFormat
import java.util.*

    fun Long.convertLongToTime(pattern:String): String{
        val date = Date(this)
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }
