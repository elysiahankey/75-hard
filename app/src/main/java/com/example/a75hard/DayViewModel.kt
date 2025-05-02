package com.example.a75hard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.a75hard.helpers.DataStoreManager
import com.example.a75hard.helpers.ProgressPhotoHelper
import com.example.a75hard.helpers.WaterHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {

    private val dayNumber: String = savedStateHandle["dayNumber"] ?: "1"

    private lateinit var homeViewModel: HomeViewModel

    private val _checked = MutableStateFlow(false)
    private val _waterDrank = MutableStateFlow(0)
    private val _photoUploaded = MutableStateFlow(false)

    val waterDrank: StateFlow<Int> = _waterDrank

    val isDayComplete: StateFlow<Boolean> = combine(
        _checked, _waterDrank, _photoUploaded
    ) { checked, water, photo ->
        checked && water >= 4500 && photo
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    fun bindHomeViewModel(homeViewModel: HomeViewModel) {
        this.homeViewModel = homeViewModel

        viewModelScope.launch {
            // Collect the isDayComplete value
            isDayComplete.collect { complete ->
                Log.d("DayViewModel", "isDayComplete changed to: $complete")

                if (complete) {
                    homeViewModel.markDayComplete(dayNumber)
                } else {
                    homeViewModel.markDayIncomplete(dayNumber)
                }
            }
        }
    }

    // Your setters
    fun setChecked(value: Boolean) {
        _checked.value = value
    }

    fun addWater(amount: Int) {
        val updated = (_waterDrank.value + amount).coerceAtMost(4500)
        _waterDrank.value = updated
        viewModelScope.launch {
            WaterHelper.saveWaterProgress(getApplication(), updated / 4500f, dayNumber)
        }
    }

    fun resetWater() {
        _waterDrank.value = 0
        viewModelScope.launch {
            WaterHelper.saveWaterProgress(getApplication(), 0f, dayNumber)
        }
    }

    fun setPhotoUploaded(uploaded: Boolean) {
        _photoUploaded.value = uploaded
    }

    init {
        loadWaterProgress()
        loadPhotoState()
    }

    private fun loadWaterProgress() {
        viewModelScope.launch {
            WaterHelper.getWaterProgress(getApplication(), dayNumber).collect { progress ->
                _waterDrank.value = (progress * 4500).toInt()
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
}