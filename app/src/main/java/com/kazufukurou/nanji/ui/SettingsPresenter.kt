package com.kazufukurou.nanji.ui

import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.model.DateTimeComponent
import com.kazufukurou.nanji.model.DateTimeDisplayMode
import com.kazufukurou.nanji.model.Language
import com.kazufukurou.nanji.model.Prefs
import com.kazufukurou.nanji.model.TapAction
import com.kazufukurou.nanji.model.TimeSystem
import java.util.TimeZone

class SettingsPresenter(
  private val prefs: Prefs,
  private val getString: (Int) -> String,
  private val render: () -> Unit,
  private val goAppearance: () -> Unit,
  private val showAboutAlert: () -> Unit,
) {
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

  fun getItems(timeSystem: TimeSystem): List<Item> {
    val languageStrings = Language.values().map { getString(it.title) }
    val tapActionStrings = TapAction.values().map { getString(it.title) }
    val dateTimeDisplayModeStrings = DateTimeDisplayMode.values().map { getString(it.title) }
    val timeZoneStrings = timeZones.map { it.ifEmpty { getString(R.string.languageSystem) } }
    val dateTimeDisplayMode = prefs.dateTimeDisplayMode
    val canBeVerbose = dateTimeDisplayMode.components.intersect(timeSystem.verboseComponents).isNotEmpty()
    val hasTime = DateTimeComponent.Time in dateTimeDisplayMode.components
    return listOfNotNull(
      ActionItem(R.string.appearance, goAppearance),
      SelectorItem(R.string.language, languageStrings, ::languageIndex),
      SelectorItem(R.string.tapAction, tapActionStrings, ::tapActionIndex),
      SelectorItem(R.string.dateTimeDisplayMode, dateTimeDisplayModeStrings, ::dateTimeDisplayModeIndex),
      SwitchItem(R.string.verboseDisplayMode, prefs::showWords).takeIf { canBeVerbose },
      SwitchItem(R.string.twentyFour, prefs::twentyFour).takeIf { hasTime },
      SwitchItem(R.string.batteryShow, ::showBattery),
      EditItem(R.string.batteryLevelPrefix, "", prefs::batteryLevelPrefix).takeIf { showBattery },
      SwitchItem(R.string.japaneseEra, prefs::japaneseEra).takeIf { prefs.language == Language.ja },
      SelectorItem(R.string.timeZone, timeZoneStrings, ::timeZoneIndex),
      ActionItem(R.string.about, showAboutAlert)
    )
  }
}