package com.example.a75hard.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Ignore
import java.lang.Thread.sleep

class DayViewModelTest {
    fun dayViewModelTestInstance(): DayViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("dayNumber" to "1"))
        val application = mockk<Application>(relaxed = true)
        return DayViewModel(savedStateHandle, application)
    }

    private val viewModel = dayViewModelTestInstance()

    @Test
    fun dayViewModel_AddWater_IncreasesWaterDrank() = runTest {
        viewModel.addWater(1000)
        sleep(100)

        assertEquals(1000, viewModel.waterDrank.value)
    }

    @Test
    fun dayViewModel_AddMoreWater_IncreasesWaterDrankCorrectly() = runTest {
        viewModel.addWater(1000)
        sleep(100)

        viewModel.addWater(1000)
        sleep(100)

        assertEquals(2000, viewModel.waterDrank.value)
    }

    @Ignore("This fails when run in a suite :(")
    @Test
    fun dayViewModel_ResetWater_SetsWaterDrankTo0() = runTest {
        viewModel.addWater(1500)
        sleep(100)

        assertEquals("Water not added correctly", 1500, viewModel.waterDrank.value)

        viewModel.resetWater()
        sleep(100)

        assertEquals("Water not reset correctly", 0, viewModel.waterDrank.value)
    }

    @Test
    fun dayViewModel_ResetDay_ResetsAllValues() = runTest {
        viewModel.addWater(1500)
        viewModel.setChecked(true)
        viewModel.setPhotoUploaded(true)
        sleep(100)

        viewModel.resetDay()
        sleep(100)

        assertEquals(0, viewModel.waterDrank.value)
        assertFalse(viewModel.isDayComplete.value)
    }
}