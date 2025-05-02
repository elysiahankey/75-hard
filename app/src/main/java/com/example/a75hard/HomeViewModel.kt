package com.example.a75hard

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

    init {
        viewModelScope.launch {
            dataStoreManager.completedDays.collect {
                _completedDays.value = it
            }
        }
    }

    fun markDayComplete(day: String) {
        val current = _completedDays.value
        Log.d("HomeViewModel", "Marking $day as COMPLETE. Current set: $current")

        val updated = current + day
        _completedDays.value = updated
        viewModelScope.launch {
            dataStoreManager.saveCompletedDays(updated)
            Log.d("HomeViewModel", "Day $day marked complete. Updated set: $updated")
        }
    }

    fun markDayIncomplete(day: String) {
        // Log the actual set of completed days
        val current = _completedDays.value
        Log.d("HomeViewModel", "Marking $day as INCOMPLETE. Current set: $current")

        if (day in current) {
            val updated = current - day
            _completedDays.value = updated
            viewModelScope.launch {
                dataStoreManager.saveCompletedDays(updated)
                Log.d("HomeViewModel", "Day $day marked incomplete. Updated set: $updated")
            }
        } else {
            Log.d("HomeViewModel", "Day $day not found in completed days.")
        }
    }

}