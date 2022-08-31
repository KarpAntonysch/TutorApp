package com.example.tutor

import android.text.Editable
import java.text.SimpleDateFormat
import java.util.*

    fun Long.convertLongToTime(pattern:String): String{
        val date = Date(this)
        val format = SimpleDateFormat(pattern)
        return format.format(date)
    }
fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this )
fun Int.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this.toString())
