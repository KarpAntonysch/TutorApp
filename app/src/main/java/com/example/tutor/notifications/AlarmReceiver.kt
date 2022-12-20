package com.example.tutor.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.tutor.R
import com.example.tutor.main.mainFragment.sendNotification

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
           val message= intent.getStringExtra("notificationMessage") // прием сообщения из VM при вызове ресивера
            notificationManager.sendNotification(
               message!!,
                context
            )
    }
}

