package com.mahmoudjoe3.standupalarmmanager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.mahmoudjoe3.standupalarmmanager.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var picker:MaterialTimePicker
    private lateinit var calendar : Calendar
    private lateinit var calendarEnd : Calendar
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManagerEnd: AlarmManager
    private lateinit var pendingIntentEnd: PendingIntent
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initTime()

        binding.from.setOnClickListener {
            showTimePicker("from")
        }

        binding.to.setOnClickListener {
            showTimePicker("to")
        }

        binding.startBTN.setOnClickListener{
            setAlarm()
            setAlarmEnd()
        }
        binding.stopBTN.setOnClickListener{
            cancelAlarm()
        }

    }

    private fun initTime() {
        var hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val minute = Calendar.getInstance().get(Calendar.MINUTE)

        binding.from.text = getTime(hour, minute)
        calendar = setCalendar(hour, minute)

        binding.to.text = getTime(hour + 10, minute)
        calendarEnd = setCalendar(hour + 10, minute)
    }



    private fun showTimePicker(s:String) {
        picker =
            if(s == "to")
                buildMaterialTimePicker(s,10)
            else
                buildMaterialTimePicker(s,0)

        picker.show(supportFragmentManager,"mahmoudJoe")
        picker.addOnPositiveButtonClickListener{

            var time = getTime(picker.hour, picker.minute)
            when(s){
                "to" -> {
                    binding.to.text = time
                    calendarEnd = setCalendar(picker.hour,picker.minute)
                }
                "from" -> {
                    binding.from.text = time
                    calendar = setCalendar(picker.hour,picker.minute)
                }
            }
        }
    }

    private fun setAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent =Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,AlarmReceiver.ALARM_REC_CODE,intent,0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            60*60*1000, pendingIntent
        )

        enabelReciver()

        Toast.makeText(this, "Alarm set successfully!",Toast.LENGTH_LONG).show()
    }

    private fun setAlarmEnd() {
        alarmManagerEnd = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent =Intent(this, AlarmEndReceiver::class.java)
        pendingIntentEnd = PendingIntent.getBroadcast(this,AlarmEndReceiver.ALARM_END_REC_CODE,intent,0)

        alarmManagerEnd.setRepeating(
            AlarmManager.RTC_WAKEUP, calendarEnd.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntentEnd
        )

        Toast.makeText(this, "Alarm set successfully!",Toast.LENGTH_LONG).show()
    }

    private fun cancelAlarm() {
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent =Intent(this, AlarmReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this,AlarmReceiver.ALARM_REC_CODE,intent,0)
        alarmManager.cancel(pendingIntent)

        //END Alarm
        alarmManagerEnd = getSystemService(ALARM_SERVICE) as AlarmManager
        val i =Intent(this, AlarmEndReceiver::class.java)
        pendingIntentEnd = PendingIntent.getBroadcast(this,AlarmEndReceiver.ALARM_END_REC_CODE,i,0)
        alarmManagerEnd.cancel(pendingIntentEnd)

        disabeleReciver()

        Toast.makeText(this, "Alarm cancelled successfully!",Toast.LENGTH_LONG).show()
    }

    private fun setCalendar(hour:Int,minute:Int): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
    }

    private fun buildMaterialTimePicker(s: String,shifted_hours:Int) =
        MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+shifted_hours)
            .setMinute(Calendar.getInstance().get(Calendar.MINUTE))
            .setTitleText("Select Alarm $s ")
            .build()

    private fun getTime(hour:Int,minute:Int)=
        if (hour >= 24)
            String.format("%02d : %02d AM", hour - 24, minute)
        else if (hour > 12)
            String.format("%02d : %02d PM", hour - 12, minute)

        else
            String.format("%02d : %02d AM", hour, minute)


    private fun enabelReciver() {
        val receiver = ComponentName(this, AlarmReceiver::class.java)
        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }
    private fun disabeleReciver() {
        val receiver = ComponentName(this, AlarmReceiver::class.java)
        packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )
    }
}