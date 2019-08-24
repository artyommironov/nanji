/*
 * Copyright 2019 Artyom Mironov
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
import android.view.View
import android.widget.RemoteViews
import com.kazufukurou.nanji.model.Prefs
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.dp
import com.kazufukurou.nanji.model.Language
import com.kazufukurou.nanji.model.Time
import com.kazufukurou.nanji.model.TimeCn
import com.kazufukurou.nanji.model.TimeEn
import com.kazufukurou.nanji.model.TimeJa
import com.kazufukurou.nanji.model.TimeKo
import com.kazufukurou.nanji.model.TimeRu
import com.kazufukurou.nanji.model.TimeSystem
import com.kazufukurou.nanji.toCodePoints
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class WidgetProvider : AppWidgetProvider() {
  override fun onUpdate(ctx: Context, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
    super.onUpdate(ctx, appWidgetManager, appWidgetIds)
    update(ctx)
  }

  override fun onEnabled(ctx: Context) {
    super.onEnabled(ctx)
    scheduleUpdate(ctx)
  }

  override fun onDisabled(ctx: Context) {
    super.onDisabled(ctx)
    getAlarmManager(ctx)?.cancel(createBroadcastPendingIntent(ctx, false))
  }

  override fun onReceive(ctx: Context, intent: Intent?) {
    super.onReceive(ctx, intent)
    intent ?: return
    val prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(ctx))
    if (intent.action == ACTION_CHANGE) prefs.showDigits = !prefs.showDigits
    update(ctx)
  }

  private fun getAlarmManager(ctx: Context) = ctx.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

  private fun createActivityPendingIntent(ctx: Context): PendingIntent {
    return PendingIntent.getActivity(ctx, 0, Intent(ctx, MainActivity::class.java), 0)
  }

  private fun createBroadcastPendingIntent(ctx: Context, change: Boolean): PendingIntent {
    val intent = Intent(ctx, WidgetProvider::class.java).setAction(if (change) ACTION_CHANGE else ACTION_TICK)
    return PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
  }

  private fun scheduleUpdate(ctx: Context) {
    val pendingIntent = createBroadcastPendingIntent(ctx, false)
    val cal = Calendar.getInstance().apply {
      set(Calendar.SECOND, 0)
      set(Calendar.MILLISECOND, 0)
      add(Calendar.MINUTE, 1)
    }
    if (Build.VERSION.SDK_INT >= 19) {
      getAlarmManager(ctx)?.setExact(AlarmManager.RTC, cal.timeInMillis, pendingIntent)
    } else {
      getAlarmManager(ctx)?.set(AlarmManager.RTC, cal.timeInMillis, pendingIntent)
    }
  }

  private fun getClockPendingIntent(ctx: Context) = sequenceOf(
    ComponentName("com.acer.deskclock", "com.acer.deskclock.DeskClock"),
    ComponentName("com.android.deskclock", "com.android.deskclock.DeskClock"),
    ComponentName("com.android.deskclock", "com.android.deskclock.AlarmClock"),
    ComponentName("com.android.alarmclock", "com.android.alarmclock.AlarmClock"),
    ComponentName("com.android.alarmclock", "com.meizu.flyme.alarmclock.DeskClock"),
    ComponentName("com.android.BBKClock", "com.android.BBKClock.Timer"),
    ComponentName("com.asus.deskclock", "com.asus.deskclock.DeskClock"),
    ComponentName("com.asus.alarmclock", "com.asus.alarmclock.AlarmClock"),
    ComponentName("com.coloros.alarmclock", "com.coloros.alarmclock.AlarmClock"),
    ComponentName("com.google.android.deskclock", "com.android.deskclock.DeskClock"),
    ComponentName("com.google.android.deskclock", "com.android.deskclock.AlarmClock"),
    ComponentName("com.google.android.alarmclock", "com.android.alarmclock.AlarmClock"),
    ComponentName("com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl"),
    ComponentName("com.lenovo.deskclock", "com.lenovo.clock.Clock"),
    ComponentName("com.lenovo.deskclock", "com.lenovo.deskclock.DeskClock"),
    ComponentName("com.lge.clock", "com.lge.clock.AlarmClockActivity"),
    ComponentName("com.lge.clock", "com.lge.clock.DefaultAlarmClockActivity"),
    ComponentName("com.lge.alarm.alarmclocknew", "com.lge.alarm.alarmclocknew.AlarmClockNew"),
    ComponentName("com.motorola.blur.alarmclock", "com.motorola.blur.alarmclock.AlarmClock"),
    ComponentName("com.motorola.alarmclock", "com.motorola.alarmclock.AlarmClock"),
    ComponentName("com.oneplus.deskclock", "com.oneplus.deskclock.DeskClock"),
    ComponentName("com.oppo.alarmclock", "com.oppo.alarmclock.AlarmClock"),
    ComponentName("com.pantech.app.clock", "com.pantech.app.clock.launcher.ClockManager"),
    ComponentName("com.sec.android.app.clockpackage", "com.sec.android.app.clockpackage.ClockPackage"),
    ComponentName("com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock"),
    ComponentName("com.sonyericsson.alarm", "com.sonyericsson.alarm.Alarm"),
    ComponentName("com.tct.timetool", "com.tct.timetool.DeskClock"),
    ComponentName("com.yulong.android.xtime", "yulong.xtime.ui.main.XTimeActivity"),
    ComponentName("com.zui.deskclock", "com.zui.deskclock.DeskClock"),
    ComponentName("net.oneplus.deskclock", "net.oneplus.deskclock.DeskClock"),
    ComponentName("zte.com.cn.alarmclock", "zte.com.cn.alarmclock.AlarmClock")
  )
    .map { Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER).setComponent(it) }
    .find { ctx.packageManager.queryIntentActivities(it, PackageManager.MATCH_DEFAULT_ONLY).isNotEmpty() }
    ?.let { PendingIntent.getActivity(ctx, 0, it, 0) }


  private fun getBatteryLevel(ctx: Context): Int {
    val batteryIntent = ctx.applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level == -1 || scale == -1 || scale == 0) 50 else (100f * level.toFloat() / scale.toFloat()).toInt()
  }

  private fun getGrammar(language: Language): Time = when (language) {
    Language.cn -> TimeCn()
    Language.ja -> TimeJa()
    Language.ko -> TimeKo()
    Language.en -> TimeEn()
    Language.ru -> TimeRu()
    Language.system -> TimeSystem(Locale.getDefault())
  }

  private fun convertDateAndTimeTexts(prefs: Prefs, dateText: String, timeText: String): Pair<String, String> {
    var resultTimeText = timeText
    var resultDateText = dateText
    "0０1１2２3３4４5５6６7７8８9９:："
      .takeIf {prefs.fullWidthDigits  }
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

  private fun update(ctx: Context) {
    val res = ctx.resources
    val prefs = Prefs(PreferenceManager.getDefaultSharedPreferences(ctx))
    val language = prefs.language
    val time = getGrammar(language)
    val batteryText = when (prefs.showBattery) {
      true -> "~" + time.getPercentText(getBatteryLevel(ctx), prefs.showDigits)
      else -> ""
    }
    val cal = Calendar.getInstance().apply {
      timeZone = if (prefs.timeZone.isBlank()) TimeZone.getDefault() else TimeZone.getTimeZone(prefs.timeZone)
    }
    val showDigits = prefs.showDigits
    val multiLineTimeText = !showDigits && (language == Language.ru || language == Language.en)
    val (dateText, timeText) = convertDateAndTimeTexts(
      prefs,
      dateText = time.getDateText(cal, showDigits, prefs.japaneseEra) + batteryText,
      timeText = time.getTimeText(cal, showDigits, prefs.twentyFour, multiLineTimeText)
    )
    val smallTextSize = prefs.smallTextSize || multiLineTimeText
    val externalClockWidget = if (prefs.openClock) getClockPendingIntent(ctx) else null
    val dateIntent = externalClockWidget ?: createActivityPendingIntent(ctx)
    val timeIntent = if (language == Language.system) dateIntent else createBroadcastPendingIntent(ctx, true)
    val timeTextId = if (smallTextSize) R.id.textTimeSmall else R.id.textTime
    val views = RemoteViews(ctx.packageName, R.layout.widget).apply {
      setTextViewText(R.id.textDate, dateText)
      setTextColor(R.id.textDate, prefs.textColor)
      setOnClickPendingIntent(R.id.textDate, dateIntent)
      setViewVisibility(R.id.textTime, if (!smallTextSize) View.VISIBLE else View.GONE)
      setViewVisibility(R.id.textTimeSmall, if (smallTextSize) View.VISIBLE else View.GONE)
      setTextViewText(timeTextId, timeText)
      setTextColor(timeTextId, prefs.textColor)
      setOnClickPendingIntent(timeTextId, timeIntent)
    }
    WidgetBg.draw(views, prefs.bgColor, res.dp(20), res.dp(prefs.cornerRadius))
    AppWidgetManager.getInstance(ctx).updateAppWidget(ComponentName(ctx, WidgetProvider::class.java), views)
    scheduleUpdate(ctx)
  }
}

private const val PREFIX = "com.kazufukurou.nanji"
private const val ACTION_CHANGE = "$PREFIX.change"
private const val ACTION_TICK = "$PREFIX.tick"
