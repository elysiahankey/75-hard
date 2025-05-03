package com.example.a75hard.viewmodels

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.example.a75hard.helpers.DataStoreManager
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    val application = mockk<Application>(relaxed = true)

    @Before
    fun setUp() {
        // Create the ViewModel with mocked dependencies
        viewModel = HomeViewModel(application)
    }

    @Test
    fun homeViewModel_MarkDayAsComplete_AddsDayToCompletedDays() = runTest {
        val day = "1"
        viewModel.markDayComplete(day)

        assertEquals(setOf(day), viewModel.completedDays.first())
    }

    @Test
    fun homeViewModel_MarkDayAsIncomplete_RemovesDayFromCompletedDays() = runTest {
        val day = "1"
        viewModel.markDayComplete(day)
        viewModel.markDayIncomplete(day)

        assertEquals(emptySet<String>(), viewModel.completedDays.first())
    }
}