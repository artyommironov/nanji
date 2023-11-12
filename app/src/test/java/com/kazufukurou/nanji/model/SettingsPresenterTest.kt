package com.kazufukurou.nanji.model

import com.kazufukurou.nanji.ui.SettingsPresenter
import com.kazufukurou.nanji.R
import com.kazufukurou.nanji.ui.Item
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsPresenterTest {
  private val prefs: Prefs = mockk {
    every { showBattery } returns false
    every { language } returns Language.system
  }
  private val timeSystem: TimeSystem = mockk()
  private val presenter = SettingsPresenter(
    prefs = prefs,
    getString = Int::toString,
    render = {},
    goAppearance = {},
    showAboutAlert = {}
  )

  @Test
  fun getItems_DisplayModeDateTime_VerboseComponentsEmpty() {
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.DateTime
    every { timeSystem.verboseComponents } returns emptySet()
    assertEquals(
      listOf(
        R.string.appearance,
        R.string.language,
        R.string.tapAction,
        R.string.dateTimeDisplayMode,
        R.string.timeTwentyFourFormat,
        R.string.batteryShow,
        R.string.timeZone,
        R.string.about
      ),
      presenter.getItems(timeSystem).map(Item::title)
    )
  }

  @Test
  fun getItems_DisplayModeDateTime_VerboseComponentsTime() {
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.DateTime
    every { timeSystem.verboseComponents } returns setOf(DateTimeComponent.Time)
    assertEquals(
      listOf(
        R.string.appearance,
        R.string.language,
        R.string.tapAction,
        R.string.dateTimeDisplayMode,
        R.string.verboseDisplayMode,
        R.string.timeTwentyFourFormat,
        R.string.batteryShow,
        R.string.timeZone,
        R.string.about
      ),
      presenter.getItems(timeSystem).map(Item::title)
    )
  }

  @Test
  fun getItems_DisplayModeDateOnly_VerboseComponentsTime() {
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.DateOnly
    every { timeSystem.verboseComponents } returns setOf(DateTimeComponent.Time)
    assertEquals(
      listOf(
        R.string.appearance,
        R.string.language,
        R.string.tapAction,
        R.string.dateTimeDisplayMode,
        R.string.batteryShow,
        R.string.timeZone,
        R.string.about
      ),
      presenter.getItems(timeSystem).map(Item::title)
    )
  }

  @Test
  fun getItems_DisplayModeTimeOnly_VerboseComponentsTime() {
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.TimeOnly
    every { timeSystem.verboseComponents } returns setOf(DateTimeComponent.Time)
    assertEquals(
      listOf(
        R.string.appearance,
        R.string.language,
        R.string.tapAction,
        R.string.dateTimeDisplayMode,
        R.string.verboseDisplayMode,
        R.string.timeTwentyFourFormat,
        R.string.batteryShow,
        R.string.timeZone,
        R.string.about
      ),
      presenter.getItems(timeSystem).map(Item::title)
    )
  }
}