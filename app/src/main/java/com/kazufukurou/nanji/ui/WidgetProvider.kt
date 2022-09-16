/*
 * Copyright 2021 Artyom Mironov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kazufukurou.nanji.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.BatteryManager
import android.os.Build
import android.preference.PreferenceManager
import android.provider.AlarmClock
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.core.content.getSystemService
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.model.Prefs
import com.kazufukurou.nanji.model.Language
import com.kazufukurou.nanji.model.Time
import com.kazufukurou.nanji.model.TimeZh
import com.kazufukurou.nanji.model.TimeEn
import com.kazufukurou.nanji.model.TimeJa
import com.kazufukurou.nanji.model.TimeKo
import com.kazufukurou.nanji.model.TimeRu
import com.kazufukurou.nanji.model.TimeSystem
import com.kazufukurou.nanji.model.toCodePoints
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class WidgetProvider : AppWidgetProvider() {
  override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
    super.onUpdate(context, appWidgetManager, appWidgetIds)
    update(context)
  }

  override fun onEnabled(context: Context) {
    super.onEnabled(context)
    scheduleUpdate(context)
  }

  override fun onDisabled(context: Context) {
    super.onDisabled(context)
    context.getSystemService<AlarmManager>()?.cancel(createBroadcastPendingIntent(context, false))
  }

  override fun onReceive(context: Context, intent: Intent?) {
    super.onReceive(context, intent)
    intent ?: return
    if (intent.action == ACTION_CHANGE) {
      val prefs = getPrefs(context)
      prefs.showWords = !prefs.showWords
    }
    update(context)
  }

  private fun createActivityPendingIntent(context: Context): PendingIntent {
    return PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), fixPendingIntentFlags(0))
  }

  private fun createBroadcastPendingIntent(context: Context, change: Boolean): PendingIntent {
    val intent = Intent(context, WidgetProvider::class.java).setAction(if (change) ACTION_CHANGE else ACTION_TICK)
    return PendingIntent.getBroadcast(context, 0, intent, fixPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT))
  }

  private fun scheduleUpdate(context: Context) {
    val pendingIntent = createBroadcastPendingIntent(context, false)
    val cal = Calendar.getInstance().apply {
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
      add(Calendar.MINUTE, 1)
    }
    val alarmManager = context.getSystemService<AlarmManager>()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      alarmManager?.setExact(AlarmManager.RTC, cal.timeInMillis, pendingIntent)
    } else {
      alarmManager?.set(AlarmManager.RTC, cal.timeInMillis, pendingIntent)
    }
  }

  private fun getAlarmPendingIntent(context: Context): PendingIntent? {
    val action = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      AlarmClock.ACTION_SHOW_ALARMS
    } else {
      AlarmClock.ACTION_SET_ALARM
    }
    val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setAction(action)
    return if (isActivityExists(context.packageManager, intent)) {
      PendingIntent.getActivity(context, 0, intent, fixPendingIntentFlags(PendingIntent.FLAG_UPDATE_CURRENT))
    } else {
      null
    }
  }

  private fun fixPendingIntentFlags(flags: Int): Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
    flags or PendingIntent.FLAG_IMMUTABLE
  } else {
    flags
  }

  private fun isActivityExists(packageManager: PackageManager, intent: Intent): Boolean {
    val flags = PackageManager.MATCH_DEFAULT_ONLY
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      packageManager.queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(flags.toLong()))
    } else {
      packageManager.queryIntentActivities(intent, flags)
    }.isNotEmpty()
  }

  private fun getPrefs(context: Context): Prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(context))

  private fun getBatteryLevel(context: Context): Int {
    val batteryIntent = context.applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level == -1 || scale == -1 || scale == 0) 50 else (100f * level.toFloat() / scale.toFloat()).toInt()
  }

  private fun getTime(prefs: Prefs): Time {
    val useWords = prefs.showWords
    val useTwentyFourHours = prefs.twentyFour
    return when (prefs.language) {
      Language.zhCN -> TimeZh(simplified = true, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.zhTW -> TimeZh(simplified = false, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ja -> TimeJa(useEra = prefs.japaneseEra, useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ko -> TimeKo(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.en -> TimeEn(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.ru -> TimeRu(useWords = useWords, useTwentyFourHours = useTwentyFourHours)
      Language.system -> TimeSystem(Locale.getDefault(), useTwentyFourHours = useTwentyFourHours)
    }
  }

  private fun convertDateAndTimeTexts(prefs: Prefs, dateText: String, timeText: String): Pair<String, String> {
    var resultTimeText = timeText
    var resultDateText = dateText
    "0０1１2２3３4４5５6６7７8８9９:："
      .takeIf { prefs.fullWidthDigits }
      .orEmpty()
      .plus(prefs.customSymbols)
      .toCodePoints()
      .windowed(2, 2, partialWindows = false, transform = { it[0] to it[1] })
      .forEach { (oldString, newString) ->
        resultDateText = resultDateText.replace(oldString, newString)
        resultTimeText = resultTimeText.replace(oldString, newString)
      }
    return resultDateText to resultTimeText
  }

  private fun update(context: Context) {
    val prefs = getPrefs(context)
    val hideTime = prefs.hideTime
    val time = getTime(prefs)
    val batteryText = if (prefs.showBattery) "~" + time.getPercentText(getBatteryLevel(context)) else ""
    val cal = Calendar.getInstance().apply {
      timeZone = if (prefs.timeZone.isBlank()) TimeZone.getDefault() else TimeZone.getTimeZone(prefs.timeZone)
    }
    val (dateText, timeText) = convertDateAndTimeTexts(
      prefs = prefs,
      dateText = time.getDateText(cal) + batteryText,
      timeText = time.getTimeText(cal)
    )
    val intent = when (prefs.tapAction) {
      TapAction.ShowWords -> createBroadcastPendingIntent(context, true)
      TapAction.OpenClock -> getAlarmPendingIntent(context) ?: createActivityPendingIntent(context)
      TapAction.OpenSetting -> createActivityPendingIntent(context)
    }
    val textSizeHeader =  context.resources.dp(prefs.textSizeRange.first).toFloat()
    val textSizeContent = context.resources.dp(prefs.textSize).toFloat()
    val views = RemoteViews(context.packageName, R.layout.widget).apply {
      setTextViewTextSize(R.id.textHeader, TypedValue.COMPLEX_UNIT_PX, textSizeHeader)
      setTextViewTextSize(R.id.textContent, TypedValue.COMPLEX_UNIT_PX, textSizeContent)
      setViewVisibility(R.id.textHeader, if (hideTime) View.GONE else View.VISIBLE)
      setTextViewText(R.id.textHeader, dateText)
      setTextViewText(R.id.textContent, if (hideTime) dateText else timeText)
      setTextColor(R.id.textHeader, prefs.textColor)
      setTextColor(R.id.textContent, prefs.textColor)
      setOnClickPendingIntent(R.id.content, intent)
    }
    views.drawBg(prefs.bgColor, context.resources.dp(20), context.resources.dp(prefs.cornerRadius))
    AppWidgetManager.getInstance(context).updateAppWidget(ComponentName(context, WidgetProvider::class.java), views)
    scheduleUpdate(context)
  }
}

private const val PREFIX = "com.kazufukurou.nanji"
private const val ACTION_CHANGE = "$PREFIX.change"
private const val ACTION_TICK = "$PREFIX.tick"
