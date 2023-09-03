package com.kazufukurou.nanji.model

import android.graphics.Color
import com.kazufukurou.nanji.ui.WidgetPresenter
import io.mockk.every
import io.mockk.mockk
import java.util.Calendar
import java.util.Locale
import kotlin.test.Test
import kotlin.test.assertEquals

class WidgetPresenterTest {
  private val now = now(2023, Calendar.SEPTEMBER, 3, 13, 24)
  private val prefs: Prefs = mockk {
    every { cornerRadiusRange } returns 0 .. 100
    every { textSizeRange } returns 10..20
    every { timeZone } returns ""
    every { fullWidthCharacters } returns false
    every { batteryLevelPrefix } returns "~"
    every { textSize } returns 12
    every { cornerRadius } returns 34
    every { textColor } returns Color.RED
    every { bgColor } returns Color.BLUE
  }
  private val presenter = WidgetPresenter(prefs)

  @Test
  fun getState_DateTime_Battery() {
    val timeSystem = DefaultTimeSystem(Locale.US, twentyFourHours = false)
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.DateTime
    every { prefs.showBattery } returns true
    assertEquals(
      WidgetPresenter.State(
        header = "Sunday, September 3, 2023~42%",
        content = "1:24 pm",
        footer = "",
        headerFooterSizeDp = 10,
        contentSizeDp = 12,
        textColor = Color.RED,
        bgColor = Color.BLUE,
        bgCornerRadiusDp = 34,
        bgCornerSizeDp = 100,
      ),
      presenter.getState(timeSystem, now, 42)
    )
  }

  @Test
  fun getState_TimeDate_TwentyFourHours() {
    val timeSystem = DefaultTimeSystem(Locale.US, twentyFourHours = true)
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.TimeDate
    every { prefs.showBattery } returns false
    assertEquals(
      WidgetPresenter.State(
        header = "",
        content = "13:24",
        footer = "Sunday, September 3, 2023",
        headerFooterSizeDp = 10,
        contentSizeDp = 12,
        textColor = Color.RED,
        bgColor = Color.BLUE,
        bgCornerRadiusDp = 34,
        bgCornerSizeDp = 100,
      ),
      presenter.getState(timeSystem, now, 42)
    )
  }

  @Test
  fun getState_TimeOnly_Battery() {
    val timeSystem = DefaultTimeSystem(Locale.US, twentyFourHours = false)
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.TimeOnly
    every { prefs.showBattery } returns true
    assertEquals(
      WidgetPresenter.State(
        header = "",
        content = "1:24 pm~42%",
        footer = "",
        headerFooterSizeDp = 10,
        contentSizeDp = 12,
        textColor = Color.RED,
        bgColor = Color.BLUE,
        bgCornerRadiusDp = 34,
        bgCornerSizeDp = 100,
      ),
      presenter.getState(timeSystem, now, 42)
    )
  }

  @Test
  fun getState_DateOnly_TwentyFourHours() {
    val timeSystem = DefaultTimeSystem(Locale.US, twentyFourHours = true)
    every { prefs.dateTimeDisplayMode } returns DateTimeDisplayMode.DateOnly
    every { prefs.showBattery } returns false
    assertEquals(
      WidgetPresenter.State(
        header = "",
        content = "Sunday, September 3, 2023",
        footer = "",
        headerFooterSizeDp = 10,
        contentSizeDp = 12,
        textColor = Color.RED,
        bgColor = Color.BLUE,
        bgCornerRadiusDp = 34,
        bgCornerSizeDp = 100,
      ),
      presenter.getState(timeSystem, now, 42)
    )
  }
}
