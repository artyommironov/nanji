package com.kazufukurou.nanji.model

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private fun <T> SharedPreferences.delegate(
  default: T,
  key: String?,
  commit: Boolean,
  getter: SharedPreferences.(String, T) -> T,
  setter: Editor.(String, T) -> Editor
) = object : ReadWriteProperty<Any?, T> {

  override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
    return runCatching { getter(key ?: property.name, default) }.getOrNull() ?: default
  }

  @SuppressLint("ApplySharedPref", "CommitPrefEdits")
  override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
    edit().setter(key ?: property.name, value ?: default).apply {
      if (commit) commit() else apply()
    }
  }
}

fun SharedPreferences.property(
  default: Boolean,
  key: String? = null,
  commit: Boolean = false
): ReadWriteProperty<Any?, Boolean> = delegate(default, key, commit, SharedPreferences::getBoolean, Editor::putBoolean)

fun SharedPreferences.property(
  default: Long,
  key: String? = null,
  commit: Boolean = false
): ReadWriteProperty<Any?, Long> = delegate(default, key, commit, SharedPreferences::getLong, Editor::putLong)

fun SharedPreferences.property(
  default: Int,
  key: String? = null,
  commit: Boolean = false
): ReadWriteProperty<Any?, Int> = delegate(default, key, commit, SharedPreferences::getInt, Editor::putInt)

fun SharedPreferences.property(
  default: Float,
  key: String? = null,
  commit: Boolean = false
): ReadWriteProperty<Any?, Float> = delegate(default, key, commit, SharedPreferences::getFloat, Editor::putFloat)

fun SharedPreferences.property(
  default: String,
  key: String? = null,
  commit: Boolean = false
): ReadWriteProperty<Any?, String> = delegate(
  default = default,
  key = key,
  commit = commit,
  getter = { k, d -> requireNotNull(getString(k, d)) },
  setter = Editor::putString
)

fun SharedPreferences.property(
  default: Set<String>,
  key: String? = null,
  commit: Boolean = false
): ReadWriteProperty<Any?, Set<String>> = delegate(
  default = default,
  key = key,
  commit = commit,
  getter = { k, d -> requireNotNull(getStringSet(k, d)) },
  setter = Editor::putStringSet
)

fun <T : Enum<*>> SharedPreferences.property(
  default: T,
  key: String? = null,
  commit: Boolean = false
): ReadWriteProperty<Any?, T> = delegate(
  default = default,
  key = key,
  commit = commit,
  getter = { k, d -> getString(k, d.name)?.let { s -> d::class.java.enumConstants?.find { it.name == s } } ?: d },
  setter = { k, v -> putString(k, v.name) }
)
