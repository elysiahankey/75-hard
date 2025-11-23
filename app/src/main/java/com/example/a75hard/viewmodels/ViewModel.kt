package com.example.a75hard.viewmodels

import android.app.Application
import android.util.Log
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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    application: Application,
) : AndroidViewModel(application) {

    sealed interface NavigationEvent {
        data object NavigateToChallengeComplete : NavigationEvent
    }

    private val _navigationChannel = Channel<NavigationEvent>()
    val navigationEvents = _navigationChannel.receiveAsFlow()

    companion object {
        val checkboxList = listOf(
            R.string.day_screen_diet_header,
            R.string.day_screen_outside_workout,
            R.string.day_screen_second_workout,
            R.string.day_screen_pages_read
        )
        const val WATER_GOAL_ML = 3000
        // TODO("Make water goal configurable from settings")
    }

    private val dayNumber: String = savedStateHandle["dayNumber"] ?: "1"

    private val dataStoreManager = DataStoreManager(application)

    private val _bookState = MutableStateFlow("")
    private val _allBooks = MutableStateFlow<List<String>>(emptyList())
    private val _waterProgress = MutableStateFlow(0)
    private val _checked = MutableStateFlow(false)
    private val _photoUploaded = MutableStateFlow(false)
    private val _hasLoadedWater = MutableStateFlow(false)

    private val _hasLoadedPhoto = MutableStateFlow(false)
    private val _hasLoadedCheckboxes = MutableStateFlow(false)
    private val _completedDays = MutableStateFlow<Set<String>>(emptySet())

    val completedDays: StateFlow<Set<String>> = _completedDays.asStateFlow()
    private val _recentlyCompletedDay = MutableStateFlow<String?>(null)

    val recentlyCompletedDay: StateFlow<String?> = _recentlyCompletedDay
    val waterDrank: StateFlow<Int> = _waterProgress
    val allBooks = _allBooks.asStateFlow()

    val isDayComplete: StateFlow<Boolean> = combine(
        _checked, waterDrank, _photoUploaded
    ) { checked, water, photo ->
        checked && water >= WATER_GOAL_ML && photo
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        viewModelScope.launch {
            dataStoreManager.completedDays.collect {
                Log.d("ViewModel", "Collected completedDays from DataStore: $it")
                _completedDays.value = it
            }
        }

        loadWaterProgress()
        loadPhotoState()
        loadCheckboxState()
        loadBookState()

        viewModelScope.launch {
            combine(
                isDayComplete,
                _hasLoadedWater,
                _hasLoadedPhoto
            ) { complete, waterLoaded, photoLoaded ->
                Triple(complete, waterLoaded, photoLoaded)
            }.collect { (complete, waterLoaded, photoLoaded) ->
                if (!waterLoaded || !photoLoaded) return@collect

                Log.d("ViewModel", "isDayComplete = $complete for day $dayNumber")
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
            val updated = _waterProgress.value + amount
            _waterProgress.value = updated
            saveWaterProgress(updated)
        }
    }

    fun resetWater() {
        _waterProgress.value = 0
        saveWaterProgress(0)
    }

    private fun saveWaterProgress(progress: Int) {
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

    private fun loadBookState() {
        viewModelScope.launch {
            TodaysBookHelper.getBookFlow(getApplication(), dayNumber).collect { book ->
                _bookState.value = book
            }
        }
    }

    fun getAllBooksRead(): Flow<List<String>> = flow {
        val bookTasks = (1..75).map { day ->
            val dayString = day.toString()
            viewModelScope.async {
                val bookTitle = TodaysBookHelper.getBookFlow(getApplication(), dayString).first()
                bookTitle
            }
        }

        val allEntries = bookTasks.awaitAll()

        val uniqueBooks = allEntries
            .filter { it.isNotBlank() }
            .map { it.trim() }
            .toSet()
            .toList()

        emit(uniqueBooks)
    }

    fun loadAllBooks() {
        viewModelScope.launch {
            getAllBooksRead().collect { books ->
                _allBooks.value = books
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
        viewModelScope.launch {
            val current = dataStoreManager.completedDays.first()
            val updated = current + day

            Log.d("ViewModel", "Marking complete: $day, new set: $updated")

            _completedDays.value = updated
            _recentlyCompletedDay.value = day

            dataStoreManager.saveCompletedDays(updated)
        }
    }

    fun markDayIncomplete(day: String) {
        viewModelScope.launch {
            val current = dataStoreManager.completedDays.first()

            if (day in current) {
                val updated = current - day
                Log.d("ViewModel", "Marking incomplete: $day, new set: $updated")
                _completedDays.value = updated
                dataStoreManager.saveCompletedDays(updated)
            } else {
                Log.d("ViewModel", "Day $day not found in completed days.")
            }
        }
    }

    fun clearRecentlyCompletedDay() {
        _recentlyCompletedDay.value = null
    }

    fun resetDay(dayNumber: String) {
        setChecked(false)
        setPhotoUploaded(false)

        if (dayNumber == this.dayNumber) {
            resetWater()
            setChecked(false)
            setPhotoUploaded(false)
            _bookState.value = ""
        }

        viewModelScope.launch {
            WaterHelper.saveWaterProgress(getApplication(), 0, dayNumber)
            ProgressPhotoHelper.deletePhoto(getApplication(), dayNumber)
            CheckboxHelper.resetAllCheckboxes(getApplication(), dayNumber, checkboxList)
            WeightHelper.resetWeight(getApplication(), dayNumber)
            NotesHelper.saveNotesState(getApplication(), dayNumber, "")
            TodaysBookHelper.saveBookState(getApplication(), "", dayNumber)
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
