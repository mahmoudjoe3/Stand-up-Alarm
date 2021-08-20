package com.mahmoudjoe3.standupalarmmanager

import android.app.AlarmManager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class AlarmEndReceiver : BroadcastReceiver(){
    companion object {
        val ALARM_END_REC_CODE = 1
    }
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    override fun onReceive(context: Context?, intent: Intent?) {
        //cancel first alarm
        alarmManager = context!!.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent =Intent(context, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(context,AlarmReceiver.ALARM_REC_CODE,intent,0)
        alarmManager.cancel(pendingIntent)

        //alarm end notification
        val i = Intent(context, GoodJobActivity::class.java)
        val pindingIntent = PendingIntent.getActivity(context, ALARM_END_REC_CODE,i,0)

        val notification = NotificationCompat.Builder(context!!,App.CHANNEL_2_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("StandUp Alarm")
            .setContentText("دى اخر مره هقولك قوم اقف")
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pindingIntent).build()

        val notficationManager = NotificationManagerCompat.from(context)
        notficationManager.notify(2,notification)
    }
}