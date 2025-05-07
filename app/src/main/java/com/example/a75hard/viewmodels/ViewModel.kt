package com.example.a75hard.viewmodels

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.a75hard.R
import com.example.a75hard.helpers.CheckboxHelper
import com.example.a75hard.helpers.DataStoreManager
import com.example.a75hard.helpers.NotesHelper
import com.example.a75hard.helpers.ProgressPhotoHelper
import com.example.a75hard.helpers.TodaysBookHelper
import com.example.a75hard.helpers.WaterHelper
import com.example.a75hard.helpers.WeightHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {

    companion object {
        val checkboxList = listOf(
            R.string.day_screen_diet_header,
            R.string.day_screen_outside_workout,
            R.string.day_screen_second_workout,
            R.string.day_screen_pages_read
        )
        const val WATER_GOAL_ML = 3000
    }

    private val dayNumber: String = savedStateHandle["dayNumber"] ?: "1"
    var currentDay = mutableStateOf("0")
        private set

    private val dataStoreManager = DataStoreManager(application)

    private val _waterProgress = MutableStateFlow(0f)
    private val _checked = MutableStateFlow(false)
    private val _photoUploaded = MutableStateFlow(false)

    private val _hasLoadedWater = MutableStateFlow(false)
    private val _hasLoadedPhoto = MutableStateFlow(false)
    private val _hasLoadedCheckboxes = MutableStateFlow(false)

    private val _completedDays = MutableStateFlow<Set<String>>(emptySet())
    val completedDays: StateFlow<Set<String>> = _completedDays

    private val _recentlyCompletedDay = MutableStateFlow<String?>(null)
    val recentlyCompletedDay: StateFlow<String?> = _recentlyCompletedDay

    val waterDrank: StateFlow<Int> = _waterProgress
        .map { (it * WATER_GOAL_ML).toInt() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val isDayComplete: StateFlow<Boolean> = combine(
        _checked, waterDrank, _photoUploaded
    ) { checked, water, photo ->
        checked && water >= WATER_GOAL_ML && photo
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        loadWaterProgress()
        loadPhotoState()
        loadCheckboxState()

        viewModelScope.launch {
            dataStoreManager.completedDays.collect {
                _completedDays.value = it
            }
        }

        viewModelScope.launch {
            combine(
                isDayComplete,
                _hasLoadedWater,
                _hasLoadedPhoto
            ) { complete, waterLoaded, photoLoaded ->
                Triple(complete, waterLoaded, photoLoaded)
            }.collect { (complete, waterLoaded, photoLoaded) ->
                if (!waterLoaded || !photoLoaded) return@collect

                Log.d("ViewModel", "isDayComplete changed to $complete for day $dayNumber")
                if (complete) {
                    markDayComplete(dayNumber)
                } else {
                    markDayIncomplete(dayNumber)
                }
            }
        }
    }

    fun setChecked(value: Boolean) {
        _checked.value = value
    }

    fun addWater(amount: Int) {
        viewModelScope.launch {
            val currentDrank = (_waterProgress.value * WATER_GOAL_ML).toInt()
            val updated = currentDrank + amount
            val newProgress = updated / WATER_GOAL_ML.toFloat()
            _waterProgress.value = newProgress
            saveWaterProgress(newProgress)
        }
    }

    fun resetWater() {
        _waterProgress.value = 0f
        saveWaterProgress(0f)
    }

    private fun saveWaterProgress(progress: Float) {
        viewModelScope.launch {
            WaterHelper.saveWaterProgress(getApplication(), progress, dayNumber)
        }
    }

    private fun loadWaterProgress() {
        viewModelScope.launch {
            WaterHelper.getWaterProgress(getApplication(), dayNumber).collect { progress ->
                _waterProgress.value = progress
                _hasLoadedWater.value = true
            }
        }
    }

    private fun loadPhotoState() {
        viewModelScope.launch {
            ProgressPhotoHelper.getPhotoState(getApplication(), dayNumber).collect { path ->
                _photoUploaded.value = path.isNotEmpty()
                _hasLoadedPhoto.value = true
            }
        }
    }

    private fun loadCheckboxState() {
        viewModelScope.launch {
            val checkboxFlows = checkboxList.map { item ->
                CheckboxHelper.getCheckboxState(getApplication(), dayNumber, item)
            }

            val combinedCheckboxState = combine(checkboxFlows) { checkboxStates ->
                checkboxStates.all { it }
            }

            combinedCheckboxState.collect { allChecked ->
                _checked.value = allChecked
                _hasLoadedCheckboxes.value = true
            }
        }
    }

    fun setPhotoUploaded(uploaded: Boolean) {
        _photoUploaded.value = uploaded
    }

    fun markDayComplete(day: String) {
        val current = _completedDays.value
        val updated = current + day
        _recentlyCompletedDay.value = day

        Log.d("ViewModel", "markDayComplete: current=$current, updated=$updated")
        _completedDays.value = updated

        viewModelScope.launch {
            dataStoreManager.saveCompletedDays(updated)
        }
    }

    fun markDayIncomplete(day: String) {
        val current = _completedDays.value

        Log.d("ViewModel", "markDayIncomplete: current=$current")

        if (day in current) {
            val updated = current - day
            _completedDays.value = updated
            viewModelScope.launch {
                dataStoreManager.saveCompletedDays(updated)
            }
        } else {
            Log.d("ViewModel", "Day $day not found in completed days.")
        }
    }

    fun clearRecentlyCompletedDay() {
        _recentlyCompletedDay.value = null
    }

    fun resetDay(dayNumber: String) {
        resetWater()
        setChecked(false)
        setPhotoUploaded(false)

        viewModelScope.launch {
            ProgressPhotoHelper.deletePhoto(getApplication(), dayNumber)
            CheckboxHelper.resetAllCheckboxes(getApplication(), dayNumber, checkboxList)
            WeightHelper.resetWeight(getApplication(), dayNumber)
            NotesHelper.saveNotesState(getApplication(), dayNumber, "")
            TodaysBookHelper.saveBookState(getApplication(), dayNumber, "")
        }
    }

    fun resetAllDays() {
        viewModelScope.launch {
            for (day in 1..75) {
                resetDay(day.toString())
            }
            dataStoreManager.clearCompletedDays()
        }
    }
}