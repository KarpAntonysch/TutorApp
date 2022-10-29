package com.example.tutor

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.android.material.internal.ContextUtils.getActivity

class CounterNotificationService ( private  val context:Context){
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    fun showNotification(counter:Int){
        val activityIntent = Intent(context,MainActivity::class.java)
        val activityPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT) }/*PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) PendingIntent.FLAG_UPDATE_CURRENT else 0*/

        val notification = NotificationCompat.Builder(context, COUNTER_CHANNEL_ID)
            .setContentTitle("Напоминание")
            .setContentText("Скоро нчнется занятие $counter")
            .setSmallIcon(R.drawable.ic_push)
           /* .setStyle(
                NotificationCompat.BigPictureStyle().bigLargeIcon()
            )*/
            .setContentIntent(activityPendingIntent)


            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
        notificationManager.notify(1,notification)
    }
    companion object{
        const val COUNTER_CHANNEL_ID = "counter_channel"
    }
}