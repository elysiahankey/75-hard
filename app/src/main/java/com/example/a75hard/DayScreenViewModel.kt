package com.example.a75hard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.a75hard.helpers.ProgressPhotoHelper
import com.example.a75hard.helpers.WaterHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {

    private val dayNumber: String = savedStateHandle["dayNumber"] ?: "1"

    private val _checked = MutableStateFlow(false)
    val checked: StateFlow<Boolean> = _checked

    private val _waterDrank = MutableStateFlow(0)
    val waterDrank: StateFlow<Int> = _waterDrank

    private val _photoUploaded = MutableStateFlow(false)
    val photoUploaded: StateFlow<Boolean> = _photoUploaded

    val isDayComplete: StateFlow<Boolean> = combine(
        _checked, _waterDrank, _photoUploaded
    ) { checked, water, photo ->
        checked && water >= 4500 && photo
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        loadWaterProgress()
        loadPhotoState()
    }

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

    private fun loadWaterProgress() {
        viewModelScope.launch {
            WaterHelper.getWaterProgress(getApplication(), dayNumber).collect { progress ->
                _waterDrank.value = (progress * 4500).toInt()
            }
        }
    }

    fun setPhotoUploaded(uploaded: Boolean) {
        _photoUploaded.value = uploaded
    }

    private fun loadPhotoState() {
        viewModelScope.launch {
            ProgressPhotoHelper.getPhotoState(getApplication(), dayNumber).collect { path ->
                _photoUploaded.value = path.isNotEmpty()
            }
        }
    }
}


