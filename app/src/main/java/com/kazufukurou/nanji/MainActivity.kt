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

package com.kazufukurou.nanji

import android.app.Dialog
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.view.plusAssign
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.main.*
import java.util.Calendar
import java.util.TimeZone
import kotlin.reflect.KMutableProperty0

class MainActivity : AppCompatActivity() {
  private var currentDialog: Dialog? = null
    set(value) {
      field?.dismiss()
      field = value
      field?.show()
    }
  private val prefs by lazy { Prefs(PreferenceManager.getDefaultSharedPreferences(this)) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)
    val languages = Language.values().toList()
    val timeZones = listOf("") + TimeZone.getAvailableIDs()
    val timeZoneToString: (String) -> String = { if (it.isEmpty()) getString(R.string.languageSystem) else it }
    val messageReplaceDigits = String.format(TEXT_REPLACE_DIGITS_EXAMPLE, getString(R.string.prefsExamples))

    val items = listOf(
      Action(this, R.string.appearance, ::goAppearance),
      Selector(this, R.string.language, languages, prefs::language, { getString(it.title) }, ::showItemAlert),
      Switch(this, R.string.prefsTwentyFour, prefs::twentyFour),
      Switch(this, R.string.prefsShowBattery, prefs::showBattery),
      Switch(this, R.string.prefsOpenClock, prefs::openClock),
      Switch(this, R.string.japaneseEra, prefs::japaneseEra).apply { containerView?.tag = TAG_JA_ERA },
      Selector(this, R.string.prefsTimeZone, timeZones, prefs::timeZone, timeZoneToString, ::showItemAlert),
      Edit(this, R.string.prefsReplaceDigits, messageReplaceDigits, prefs::customSymbols, ::showItemAlert),
      Action(this, R.string.about, ::showAboutAlert)
    )
    items.forEach { viewList += it.containerView!! }
    render()
  }

  override fun onPause() {
    super.onPause()
    val ids = AppWidgetManager.getInstance(this).getAppWidgetIds(ComponentName(this, WidgetProvider::class.java))
    sendBroadcast(
      Intent(this, WidgetProvider::class.java)
        .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
    )
    currentDialog?.dismiss()
  }

  private fun render() {
    viewList.findViewWithTag<View>(TAG_JA_ERA)?.isVisible = prefs.language == Language.ja
  }

  private fun goAppearance() {
    startActivity(Intent(this, AppearanceActivity::class.java))
  }

  private fun goEmail(appName: String, appVersion: String) {
    val deviceInfo = "${Build.BRAND} ${Build.MODEL} (${Build.VERSION.SDK_INT})"
    Intent(Intent.ACTION_SEND)
      .setType("plain/text")
      .putExtra(Intent.EXTRA_EMAIL, arrayOf("artyommironov@gmail.com"))
      .putExtra(Intent.EXTRA_SUBJECT, "$appName $appVersion $deviceInfo")
      .takeIf { it.resolveActivity(packageManager) != null }
      ?.let(::startActivity)
  }

  private fun showItemAlert(item: LayoutContainer) {
    currentDialog = when (item) {
      is Edit -> item.showAlert()
      is Selector<*> -> item.showAlert(::render)
      else -> null
    }
  }

  private fun showAboutAlert() {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    val appName = getString(R.string.appName)
    val appVersion = packageInfo.versionName
    val year = Calendar.getInstance().year
    currentDialog = AlertDialog.Builder(this)
      .setTitle("$appName $appVersion")
      .setMessage(String.format(LICENSE, year))
      .setPositiveButton(android.R.string.ok, null)
      .setNegativeButton(R.string.prefsFeedback) { _, _ -> goEmail(appName, appVersion) }
      .create()
  }

  private class Action(
    context: Context,
    @StringRes title: Int,
    onClick: () -> Unit
  ) : LayoutContainer by Item(context) {
    init {
      containerView?.setOnClickListener { onClick() }
      switchValue.isVisible = false
      textSubTitle.isVisible = false
      textTitle.setText(title)
    }
  }

  private class Switch(
    context: Context,
    @StringRes title: Int,
    private val property: KMutableProperty0<Boolean>
  ) : LayoutContainer by Item(context) {
    init {
      containerView?.setOnClickListener {
        property.set(!property.get())
        render()
      }
      textSubTitle.isVisible = false
      textTitle.setText(title)
      switchValue.isClickable = false
      render()
    }

    private fun render() {
      switchValue.isChecked = property.get()
    }
  }

  private class Edit(
    private val context: Context,
    @StringRes private val title: Int,
    private val message: String,
    private val property: KMutableProperty0<String>,
    onClick: (Edit) -> Unit
  ) : LayoutContainer by Item(context) {
    init {
      containerView?.setOnClickListener { onClick(this) }
      switchValue.isVisible = false
      textTitle.setText(title)
      render()
    }

    private fun render() {
      textSubTitle.text = property.get()
      textSubTitle.isVisible = textSubTitle.text.toString().isNotEmpty()
    }

    fun showAlert(): Dialog {
      val edit = EditText(context).apply {
        setSingleLine()
        append(property.get())
      }
      return AlertDialog.Builder(context)
        .setTitle(title)
        .apply { if (message.isNotEmpty()) setMessage(message) }
        .setView(FrameLayout(context).apply {
          setPadding(resources.dp(16), 0, resources.dp(16), 0)
          addView(edit)
        })
        .setPositiveButton(android.R.string.ok) { _, _ ->
          property.set(edit.text.toString().trim())
          render()
        }
        .setNegativeButton(android.R.string.cancel, null)
        .create()
    }
  }

  private class Selector<T>(
    private val context: Context,
    @StringRes private val title: Int,
    private val items: List<T>,
    private val property: KMutableProperty0<T>,
    private val toString: (T) -> String,
    private val onClick: (Selector<T>) -> Unit
  ) : LayoutContainer by Item(context) {
    init {
      containerView?.setOnClickListener { onClick(this) }
      switchValue.isVisible = false
      textTitle.setText(title)
      render()
    }

    private fun render() {
      textSubTitle.text = toString(property.get())
      textSubTitle.isVisible = textSubTitle.text.toString().isNotEmpty()
    }

    fun showAlert(onChoose: () -> Unit): Dialog {
      return AlertDialog.Builder(context)
        .setTitle(title)
        .setSingleChoiceItems(
          items.map(toString).toTypedArray(),
          items.indexOf(property.get()).coerceAtLeast(0)
        ) { d, w ->
          property.set(items[w])
          d.cancel()
          render()
          onChoose()
        }
        .create()
    }
  }

  private class Item(context: Context) : LayoutContainer {
    override val containerView: View = View.inflate(context, R.layout.item, null)
  }
}

private const val TAG_JA_ERA = "era"
private const val TEXT_REPLACE_DIGITS_EXAMPLE =  "ABCDEF = A->B C->D E->F\n%s\n零〇~♥\n一壱二弐三参十拾"

private const val LICENSE = """Copyright %d Artyom Mironov

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
