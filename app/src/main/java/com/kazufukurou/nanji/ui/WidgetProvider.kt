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
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.BatteryManager
import android.os.Build
import android.provider.AlarmClock
import android.util.TypedValue
import android.view.View
import android.widget.RemoteViews
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.core.content.getSystemService
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.model.Module
import com.kazufukurou.nanji.model.TapAction
import java.util.Calendar

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
      val prefs = Module.getPrefs(context)
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
    alarmManager?.setExact(AlarmManager.RTC, cal.timeInMillis, pendingIntent)
  }

  private fun getAlarmPendingIntent(context: Context): PendingIntent? {
    val intent = Intent().addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      .setAction(AlarmClock.ACTION_SHOW_ALARMS)
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
      @Suppress("DEPRECATION")
      packageManager.queryIntentActivities(intent, flags)
    }.isNotEmpty()
  }

  private fun getBatteryLevel(context: Context): Int {
    val batteryIntent = context.applicationContext.registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    val level = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
    val scale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
    return if (level == -1 || scale == -1 || scale == 0) 50 else (100f * level.toFloat() / scale.toFloat()).toInt()
  }

  private fun update(context: Context) {
    val prefs = Module.getPrefs(context)
    val timeSystem = Module.getTimeSystem(prefs)
    val presenter = WidgetPresenter(timeSystem, prefs)
    val state = presenter.getState(batteryLevel = getBatteryLevel(context))
    val resources = context.resources
    val views = RemoteViews(context.packageName, R.layout.widget).apply {
      updateTextView(
        resources = resources,
        id = R.id.textHeader,
        color = state.textColor,
        sizeDp = state.headerFooterSizeDp,
        text = state.header,
        visible = state.header.isNotEmpty()
      )
      updateTextView(
        resources = resources,
        id = R.id.textContent,
        color = state.textColor,
        sizeDp = state.contentSizeDp,
        text = state.content,
        visible = state.content.isNotEmpty()
      )
      updateTextView(
        resources = resources,
        id = R.id.textFooter,
        color = state.textColor,
        sizeDp = state.headerFooterSizeDp,
        text = state.footer,
        visible = state.footer.isNotEmpty()
      )
      val pendingIntent = when (prefs.tapAction) {
        TapAction.ShowWords -> createBroadcastPendingIntent(context, true)
        TapAction.OpenClock -> getAlarmPendingIntent(context) ?: createActivityPendingIntent(context)
        TapAction.OpenSetting -> createActivityPendingIntent(context)
      }
      setOnClickPendingIntent(R.id.content, pendingIntent)
      drawBg(
        bgColor = state.bgColor,
        cornerSize = resources.dp(state.bgCornerSizeDp),
        cornerRadius = resources.dp(state.bgCornerRadiusDp)
      )
    }
    AppWidgetManager.getInstance(context).updateAppWidget(ComponentName(context, WidgetProvider::class.java), views)
    scheduleUpdate(context)
  }

  private fun RemoteViews.updateTextView(
    resources: Resources,
    @IdRes id: Int,
    @ColorInt color: Int,
    sizeDp: Int,
    text: String,
    visible: Boolean,
  ) {
    setTextViewTextSize(id, TypedValue.COMPLEX_UNIT_PX, resources.dp(sizeDp).toFloat())
    setTextViewText(id, text)
    setTextColor(id, color)
    setViewVisibility(id, if (visible) View.VISIBLE else View.GONE)
  }

  private fun RemoteViews.drawBg(bgColor: Int, cornerSize: Int, cornerRadius: Int) {
    val paint = Paint().apply {
      isAntiAlias = true
      style = Paint.Style.FILL
      color = bgColor
    }
    val radius = cornerRadius.toFloat()
    val size = cornerSize.toFloat()
    val config = Bitmap.Config.ARGB_8888
    listOf(
      R.id.imageBgRightBottom to RectF(-size, -size, size, size),
      R.id.imageBgLeftBottom to RectF(0f, -size, size * 2f, size),
      R.id.imageBgLeftTop to RectF(0f, 0f, size * 2f, size * 2f),
      R.id.imageBgRightTop to RectF(-size, 0f, size, size * 2f)
    ).forEach { (id, rect) ->
      val bitmap = Bitmap.createBitmap(cornerSize, cornerSize, config)
      Canvas(bitmap).drawRoundRect(rect, radius, radius, paint)
      setImageViewBitmap(id, bitmap)
    }
    val bitmap = Bitmap.createBitmap(2, 2, config)
    Canvas(bitmap).drawColor(bgColor)
    setImageViewBitmap(R.id.imageBgTop, bitmap)
    setImageViewBitmap(R.id.imageBgMiddle, bitmap)
    setImageViewBitmap(R.id.imageBgBottom, bitmap)
  }
}

private const val PREFIX = "com.kazufukurou.nanji"
private const val ACTION_CHANGE = "$PREFIX.change"
private const val ACTION_TICK = "$PREFIX.tick"
