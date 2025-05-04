package com.example.a75hard.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.a75hard.R
import com.example.a75hard.helpers.CheckboxHelper
import com.example.a75hard.helpers.NotesHelper
import com.example.a75hard.helpers.ProgressPhotoHelper
import com.example.a75hard.helpers.WaterHelper
import com.example.a75hard.helpers.WeightHelper
import com.example.a75hard.viewmodels.HomeViewModel
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
class DayViewModel @Inject constructor(
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

        const val WATER_GOAL_ML = 3000 // Example fixed goal
    }

    private val dayNumber: String = savedStateHandle["dayNumber"] ?: "1"
    private val _waterProgress = MutableStateFlow(0f)
    private val _checked = MutableStateFlow(false)
    private val _photoUploaded = MutableStateFlow(false)

    val waterDrank: StateFlow<Int> = _waterProgress
        .map { (it * WATER_GOAL_ML).toInt() }
        .stateIn(viewModelScope, SharingStarted.Companion.Eagerly, 0)

    val isDayComplete: StateFlow<Boolean> = combine(
        _checked, waterDrank, _photoUploaded
    ) { checked, water, photo ->
        checked && water >= WATER_GOAL_ML && photo
    }.stateIn(viewModelScope, SharingStarted.Companion.Eagerly, false)

    init {
        loadPhotoState()
    }

    private fun loadWaterProgress() {
        viewModelScope.launch {
            WaterHelper.getWaterProgress(getApplication(), dayNumber).collect { progress ->
                _waterProgress.value = progress
            }
        }
    }

    private fun loadPhotoState() {
        viewModelScope.launch {
            ProgressPhotoHelper.getPhotoState(getApplication(), dayNumber).collect { path ->
                _photoUploaded.value = path.isNotEmpty()
            }
        }
    }

    fun bindHomeViewModel(homeViewModel: HomeViewModel) {
        loadWaterProgress()
        loadPhotoState()

        viewModelScope.launch {
            isDayComplete.collect { complete ->
                if (complete) {
                    homeViewModel.markDayComplete(dayNumber)
                } else {
                    homeViewModel.markDayIncomplete(dayNumber)
                }
            }
        }
    }

    fun setChecked(value: Boolean) {
        _checked.value = value
    }

    fun addWater(amount: Int) {
        updateWaterProgress(amount)
    }

    private fun updateWaterProgress(amount: Int) {
        viewModelScope.launch {
            val currentDrank = (_waterProgress.value * WATER_GOAL_ML).toInt()
            val updated = (currentDrank + amount)
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

    fun setPhotoUploaded(uploaded: Boolean) {
        _photoUploaded.value = uploaded
    }

    fun resetDay() {
        resetWater()
        setChecked(false)
        setPhotoUploaded(false)
        viewModelScope.launch {
            ProgressPhotoHelper.deletePhoto(getApplication(), dayNumber)
            CheckboxHelper.resetAllCheckboxes(getApplication(), dayNumber, checkboxList)
            WeightHelper.resetWeight(getApplication(), dayNumber)
            NotesHelper.saveNotesState(getApplication(), dayNumber, "")
        }
    }
}