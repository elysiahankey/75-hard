package com.example.a75hard.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse

class DayViewModelTest {
    fun DayViewModelTestInstance(): DayViewModel {
        val savedStateHandle = SavedStateHandle(mapOf("dayNumber" to "1"))
        val application = mockk<Application>(relaxed = true)
        return DayViewModel(savedStateHandle, application)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dayViewModel_AddWater_IncreasesWaterDrank() = runTest {
        val viewModel = DayViewModelTestInstance()

        viewModel.addWater(1000)

        advanceUntilIdle()

        assertEquals(1000, viewModel.waterDrank.value)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dayViewModel_ResetWater_SetsWaterDrankTo0() = runTest {
        val viewModel = DayViewModelTestInstance()

        viewModel.addWater(1500)
        advanceUntilIdle()

        viewModel.resetWater()
        advanceUntilIdle()

        val waterDrankValue = viewModel.waterDrank.value
        assertEquals(0, waterDrankValue)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun dayViewModel_ResetDay_ResetsAllValues() = runTest {
        val viewModel = DayViewModelTestInstance()

        viewModel.addWater(1500)
        viewModel.setChecked(true)
        viewModel.setPhotoUploaded(true)
        advanceUntilIdle()

        viewModel.resetDay()
        advanceUntilIdle()

        assertEquals(0, viewModel.waterDrank.value)
        assertFalse(viewModel.isDayComplete.value)
    }
}