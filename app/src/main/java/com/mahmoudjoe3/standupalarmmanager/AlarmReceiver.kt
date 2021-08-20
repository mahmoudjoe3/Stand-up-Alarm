package com.mahmoudjoe3.standupalarmmanager

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AlarmReceiver : BroadcastReceiver(){
    companion object {
        val ALARM_REC_CODE = 0
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent!!.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        val i = Intent(context, GoodJobActivity::class.java)
        val pindingIntent = PendingIntent.getActivity(context,ALARM_REC_CODE,i,0)

        val notification = NotificationCompat.Builder(context!!,App.CHANNEL_1_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("StandUp Alarm")
            .setContentText("اتفضل قوم اقف علشان ضهرك")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pindingIntent).build()

        val notficationManager = NotificationManagerCompat.from(context)
        notficationManager.notify(1,notification)

    }
}