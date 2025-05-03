package com.example.a75hard.viewmodels

import android.app.Application
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Before

class HomeViewModelTest {
    private lateinit var viewModel: HomeViewModel
    val application = mockk<Application>(relaxed = true)

    @Before
    fun setUp() {
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