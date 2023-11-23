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
import com.kazufukurou.nanji.model.Module
import java.util.Calendar

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
  private val presenter: SettingsPresenter by lazy {
    SettingsPresenter(
      prefs = prefs,
      getString = { getString(it) },
      render = ::render,
      goAppearance = { startActivity(Intent(this, AppearanceActivity::class.java)) },
      showAboutAlert = ::showAboutAlert
    )
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
    val timeSystem = Module.getTimeSystem(prefs)
    myAdapter.submitList(presenter.getItems(timeSystem))
  }

  private fun getItemBinding(parent: ViewGroup): ItemBinding {
    return ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  }

  private fun showResetAlert() {
    MaterialAlertDialogBuilder(this)
      .setTitle(R.string.reset)
      .setMessage(R.string.resetSettings)
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
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE."""
