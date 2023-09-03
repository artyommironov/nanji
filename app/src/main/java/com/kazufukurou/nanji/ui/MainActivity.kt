package com.kazufukurou.nanji.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.artyommironov.anyadapter.AnyAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.kazufukurou.nanji.BuildConfig
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.databinding.ItemBinding
import com.kazufukurou.nanji.model.DateTimeComponent
import com.kazufukurou.nanji.model.DateTimeDisplayMode
import com.kazufukurou.nanji.model.Language
import com.kazufukurou.nanji.model.Module
import com.kazufukurou.nanji.model.TapAction
import java.util.Calendar
import java.util.TimeZone

class MainActivity : AppCompatActivity() {
  private val prefs by lazy { Module.getPrefs(this) }
  private val diffUtilItemCallback = object : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
      return oldItem is Item && newItem is Item && oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = false
  }
  private val dialogHolder = DialogHolder()
  private val myAdapter = AnyAdapter(diffUtilItemCallback)
    .map { ActionHolder(getItemBinding(it)) }
    .map { SwitchHolder(getItemBinding(it)) }
    .map { EditHolder(getItemBinding(it), dialogHolder) }
    .map { SelectorHolder(getItemBinding(it), dialogHolder) }
  private var dateTimeDisplayModeIndex: Int
    get() = DateTimeDisplayMode.values().indexOf(prefs.dateTimeDisplayMode)
    set(value) {
      prefs.dateTimeDisplayMode = DateTimeDisplayMode.values()[value]
      render()
    }
  private var tapActionIndex: Int
    get() = TapAction.values().indexOf(prefs.tapAction)
    set(value) {
      prefs.tapAction = TapAction.values()[value]
    }
  private var languageIndex: Int
    get() = Language.values().indexOf(prefs.language)
    set(value) {
      prefs.language = Language.values()[value]
      render()
    }
  private var showBattery: Boolean
    get() = prefs.showBattery
    set(value) {
      prefs.showBattery = value
      render()
    }
  private val timeZones: List<String> = listOf("") + TimeZone.getAvailableIDs()
  private var timeZoneIndex: Int
    get() = timeZones.indexOf(prefs.timeZone)
    set(value) {
      prefs.timeZone = timeZones[value]
    }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(
      RecyclerView(this).apply {
        setHasFixedSize(true)
        layoutManager = LinearLayoutManager(context)
        adapter = myAdapter
      }
    )
    render()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menu.addItem(this, R.string.reset, R.drawable.ic_reset, ::showResetAlert)
    return true
  }

  override fun onPause() {
    super.onPause()
    val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, WidgetProvider::class.java))
    sendBroadcast(
      Intent(this, WidgetProvider::class.java)
        .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
    )
    dialogHolder.dismiss()
  }

  private fun render() {
    val languageStrings = Language.values().map { getString(it.title) }
    val tapActionStrings = TapAction.values().map { getString(it.title) }
    val dateTimeDisplayModeStrings = DateTimeDisplayMode.values().map { getString(it.title) }
    val timeZoneStrings = timeZones.map { it.ifEmpty { getString(R.string.languageSystem) } }
    val dateTimeDisplayMode = prefs.dateTimeDisplayMode
    val timeSystem = Module.getTimeSystem(prefs)
    val canBeVerbose = dateTimeDisplayMode.components.intersect(timeSystem.verboseComponents).isNotEmpty()
    val hasTime = DateTimeComponent.Time in dateTimeDisplayMode.components
    myAdapter.submitList(
      listOfNotNull(
        ActionItem(R.string.appearance, ::goAppearance),
        SelectorItem(R.string.language, languageStrings, ::languageIndex),
        SelectorItem(R.string.prefsTapAction, tapActionStrings, ::tapActionIndex),
        SelectorItem(R.string.prefsDateTimeDisplayMode, dateTimeDisplayModeStrings, ::dateTimeDisplayModeIndex),
        SwitchItem(R.string.prefsVerboseDisplayMode, prefs::showWords).takeIf { canBeVerbose },
        SwitchItem(R.string.prefsTwentyFour, prefs::twentyFour).takeIf { hasTime },
        SwitchItem(R.string.prefsBatteryShow, ::showBattery),
        EditItem(R.string.prefsBatteryLevelPrefix, "", prefs::batteryLevelPrefix) .takeIf { showBattery },
        SwitchItem(R.string.japaneseEra, prefs::japaneseEra).takeIf { prefs.language == Language.ja },
        SelectorItem(R.string.prefsTimeZone, timeZoneStrings, ::timeZoneIndex),
        ActionItem(R.string.about, ::showAboutAlert)
      )
    )
  }

  private fun getItemBinding(parent: ViewGroup): ItemBinding {
    return ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  }

  private fun goAppearance() {
    startActivity(Intent(this, AppearanceActivity::class.java))
  }

  private fun showResetAlert() {
    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.reset)
      .setMessage(R.string.reset_settings)
      .setPositiveButton(R.string.reset) { _, _ ->
        prefs.clear()
        render()
      }
      .setNegativeButton(android.R.string.cancel, null)
      .create()
      .let(dialogHolder::show)
  }

  private fun showAboutAlert() {
    val appName = getString(R.string.appName)
    val appVersion = BuildConfig.VERSION_NAME
    val year = Calendar.getInstance().get(Calendar.YEAR)
    MaterialAlertDialogBuilder(this)
      .setTitle("$appName $appVersion")
      .setMessage(String.format(LICENSE, year))
      .setPositiveButton(android.R.string.ok, null)
      .create()
      .let(dialogHolder::show)
  }
}

private const val LICENSE = """Copyright 2012-%d Artyom Mironov

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
"""
