package com.example.a75hard.components

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.a75hard.viewmodels.ViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WaterTrackerTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @MockK
    lateinit var mockViewModel: ViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        val mockWaterDrankState = MutableStateFlow(500)

        every { mockViewModel.waterDrank } returns mockWaterDrankState
    }

    @Test
    fun waterTracker_DisplaysCorrectUIElements() {
        composeTestRule.setContent {
            WaterTracker(dayNumber = "1", viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithTag("water_tracker_progress_bar").assertIsDisplayed()

        composeTestRule.onNodeWithTag("water_tracker_input_field").assertIsDisplayed()

        composeTestRule.onNodeWithText("Reset").assertIsDisplayed()
    }

    @Test
    fun waterTracker_ProgressUpdatesCorrectly() {
        composeTestRule.setContent {
            WaterTracker(dayNumber = "1", viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("25% of daily goal of 2000ml").assertIsDisplayed()
    }

    @Test
    fun waterTracker_AddsWaterCorrectly() {
        composeTestRule.setContent {
            WaterTracker(dayNumber = "1", viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithTag("water_tracker_input_field").performTextInput("300")

        composeTestRule.onNodeWithText("Add").performClick()

        verify { mockViewModel.addWater(800) }
    }

    @Test
    fun waterTracker_ResetButtonWorks() {
        composeTestRule.setContent {
            WaterTracker(dayNumber = "1", viewModel = mockViewModel)
        }

        composeTestRule.onNodeWithText("Reset").performClick()

        verify { mockViewModel.resetWater() }
    }
}