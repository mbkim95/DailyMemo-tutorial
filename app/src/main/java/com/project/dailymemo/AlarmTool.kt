package com.project.dailymemo

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.project.dailymemo.data.MemoDao
import io.realm.Realm
import java.util.*

class AlarmTool : BroadcastReceiver() {

    companion object {
        private const val ACTION_RUN_ALARM = "RUN_ALARM"

        private fun createAlarmIntent(context: Context, id: String): PendingIntent {
            val intent = Intent(context, AlarmTool::class.java)
            intent.data = Uri.parse("id:" + id)
            intent.putExtra("MEMO_ID", id)
            intent.action = ACTION_RUN_ALARM

            return PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        fun addAlarm(context: Context, id: String, alarmTime: Date) {
            val alarmIntent = createAlarmIntent(context, id)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.time, alarmIntent)
        }

        fun deleteAlarm(context: Context, id: String) {
            val alarmIntent = createAlarmIntent(context, id)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(alarmIntent)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AlarmTool.ACTION_RUN_ALARM -> {
                val memoId = intent.getStringExtra("MEMO_ID")
                val realm = Realm.getDefaultInstance()
                val memoData = MemoDao(realm).selectMemo(memoId)

                val notificationIntent = Intent(context, DetailActivity::class.java)
                notificationIntent.putExtra("MEMO_ID", memoId)

                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Notification.Builder(context, "alarm")
                        .setContentTitle(memoData.title)
                        .setContentText(memoData.content)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                } else {
                    TODO("VERSION.SDK_INT < O")
                }

                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                    val channel =
                        NotificationChannel("alarm", "알림 메시지", NotificationManager.IMPORTANCE_HIGH)

                    notificationManager.createNotificationChannel(channel)
                } else {
                    builder.setSmallIcon(R.mipmap.ic_launcher)
                }

                notificationManager.notify(1, builder.build())
            }

            Intent.ACTION_BOOT_COMPLETED -> {
                val realm = Realm.getDefaultInstance()
                val activeAlarms = MemoDao(realm).getActiveAlarms()

                for (memoData in activeAlarms) {
                    addAlarm(context, memoData.id, memoData.alarmTime)
                }
            }
        }
    }

}