package com.example.a75hard.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.a75hard.helpers.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application
) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)

    private val _completedDays = MutableStateFlow<Set<String>>(emptySet())
    val completedDays: StateFlow<Set<String>> = _completedDays

    private val _recentlyCompletedDay = MutableStateFlow<String?>(null)
    val recentlyCompletedDay: StateFlow<String?> = _recentlyCompletedDay

    init {
        viewModelScope.launch {
            dataStoreManager.completedDays.collect {
                _completedDays.value = it
            }
        }
    }

    fun markDayComplete(day: String) {
        val current = _completedDays.value
        val updated = current + day
        _recentlyCompletedDay.value = day

        _completedDays.value = updated
        viewModelScope.launch {
            dataStoreManager.saveCompletedDays(updated)
        }
    }

    fun markDayIncomplete(day: String) {
        val current = _completedDays.value

        if (day in current) {
            val updated = current - day
            _completedDays.value = updated
            viewModelScope.launch {
                dataStoreManager.saveCompletedDays(updated)
            }
        } else {
            Log.d("HomeViewModel", "Day $day not found in completed days.")
        }
    }

    fun clearRecentlyCompletedDay() {
        _recentlyCompletedDay.value = null
    }
}