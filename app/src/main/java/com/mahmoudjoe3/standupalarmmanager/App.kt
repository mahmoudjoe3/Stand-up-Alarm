package com.mahmoudjoe3.standupalarmmanager

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build


class App : Application() {
    companion object{
        val CHANNEL_1_ID:String = "channel1"
        val CHANNEL_2_ID:String = "channel2"
    }
    override fun onCreate() {
        super.onCreate()

        createNotificationChannel();
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val channel1 = NotificationChannel(
                CHANNEL_1_ID,"Stand Up Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel1.enableLights(true)
            channel1.enableVibration(true)
            channel1.lightColor = Color.GREEN;
            channel1.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel1.description = "قوم اقف وانت بتكلمنى قوم اقف بصلى وفهمنى"

            val uri: Uri =
                Uri.parse("android.resource://" + this.packageName + "/raw/standup")
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            channel1.setSound(uri, att)

            val channel2 = NotificationChannel(
                CHANNEL_2_ID,"End Stand Up Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel2.enableLights(true)
            channel2.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel2.description = "خلاص نام بقا ومتقرفنيش"

            val uri2: Uri =
                Uri.parse("android.resource://" + this.packageName + "/raw/end_alarm")
            val att2 = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            channel2.setSound(uri2, att2)

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }
    }


}