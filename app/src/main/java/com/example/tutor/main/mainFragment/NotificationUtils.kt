package com.example.tutor.main.mainFragment

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.tutor.MainActivity
import com.example.tutor.R

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
        val intent = Intent(applicationContext, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(applicationContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.push_notification_channel_id)
        )
            .setSmallIcon(R.drawable.ic_push)
            .setContentTitle(applicationContext
                .getString(R.string.push_tittle))
            .setContentText(messageBody)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        notify(NOTIFICATION_ID, builder.build())
    }



