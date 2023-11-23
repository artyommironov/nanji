package com.kazufukurou.nanji.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.getSystemService

class Environment(context: Context) {
  private val alarmManager: AlarmManager = requireNotNull(context.getSystemService())
  private val packageManager: PackageManager = context.packageManager

  fun canScheduleExactAlarms(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    alarmManager.canScheduleExactAlarms()
  } else {
    true
  }

  fun scheduleExactAlarm(timeMillis: Long, pendingIntent: PendingIntent) {
    alarmManager.setExact(AlarmManager.RTC, timeMillis, pendingIntent)
  }

  fun cancelAlarm(pendingIntent: PendingIntent) {
    alarmManager.cancel(pendingIntent)
  }

  fun getAlarmSettingsIntent(): Intent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
    Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
  } else {
    null
  }

  fun isActivityExists(intent: Intent): Boolean {
    val flags = PackageManager.MATCH_DEFAULT_ONLY
    val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(flags.toLong()))
    } else {
      @Suppress("DEPRECATION")
      packageManager.queryIntentActivities(intent, flags)
    }
    return resolveInfos.isNotEmpty()
  }
}