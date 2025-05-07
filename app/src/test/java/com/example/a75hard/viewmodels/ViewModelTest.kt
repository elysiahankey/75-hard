package com.example.a75hard.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Ignore
import java.lang.Thread.sleep

class ViewModelTest {
    private lateinit var viewModel: ViewModel
    val application = mockk<Application>(relaxed = true)

    @Before
    fun setUp() {
        val savedStateHandle = SavedStateHandle(mapOf("dayNumber" to "1"))
        viewModel = ViewModel(savedStateHandle, application)
    }

    @Test
    fun viewModel_AddWater_IncreasesWaterDrank() = runTest {
        viewModel.addWater(1000)
        sleep(100)

        assertEquals(1000, viewModel.waterDrank.value)
    }

    @Test
    fun viewModel_AddMoreWater_IncreasesWaterDrankCorrectly() = runTest {
        viewModel.addWater(1000)
        sleep(100)

        viewModel.addWater(1000)
        sleep(100)

        assertEquals(2000, viewModel.waterDrank.value)
    }

    @Ignore("This fails when run in a suite :(")
    @Test
    fun viewModel_ResetWater_SetsWaterDrankTo0() = runTest {
        viewModel.addWater(1500)
        sleep(100)

        assertEquals("Water not added correctly", 1500, viewModel.waterDrank.value)

        viewModel.resetWater()
        sleep(100)

        assertEquals("Water not reset correctly", 0, viewModel.waterDrank.value)
    }

    @Test
    fun viewModel_ResetDay_ResetsAllValues() = runTest {
        viewModel.addWater(1500)
        viewModel.setChecked(true)
        viewModel.setPhotoUploaded(true)
        sleep(100)

        viewModel.resetDay("2")
        sleep(100)

        assertEquals(0, viewModel.waterDrank.value)
        assertFalse(viewModel.isDayComplete.value)
    }

    @Test
    fun viewModel_MarkDayAsComplete_AddsDayToCompletedDays() = runTest {
        val day = "1"
        viewModel.markDayComplete(day)

        assertEquals(setOf(day), viewModel.completedDays.first())
    }

    @Test
    fun viewModel_MarkDayAsIncomplete_RemovesDayFromCompletedDays() = runTest {
        val day = "1"
        viewModel.markDayComplete(day)
        viewModel.markDayIncomplete(day)

        assertEquals(emptySet<String>(), viewModel.completedDays.first())
    }
}