package com.example.tutor.main.mainFragment

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.tutor.MainActivity
import com.example.tutor.R
import com.example.tutor.main.addStudentToSchedule.AddStudentToScheduleViewModel

private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0

    fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {

        val intent = Intent(applicationContext, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(applicationContext,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE)

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
            .setSound(RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        notify(NOTIFICATION_ID, builder.build())
    }



