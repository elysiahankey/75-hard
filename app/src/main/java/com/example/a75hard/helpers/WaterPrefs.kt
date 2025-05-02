package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Top-level property to initialize DataStore
val Context.dataStore by preferencesDataStore(name = "water_prefs")

object WaterPrefs {

    // Function to get the key for the specific day's progress
    private fun getWaterProgressKey(dayNumber: String): String {
        return "water_progress_$dayNumber"
    }

    // Save progress for a specific day (using day number)
    suspend fun saveWaterProgress(context: Context, progress: Float, dayNumber: String) {
        val WATER_KEY = floatPreferencesKey(getWaterProgressKey(dayNumber)) // Dynamic key based on day number
        context.dataStore.edit { prefs ->
            prefs[WATER_KEY] = progress
        }
    }

    // Get progress for a specific day (using day number)
    fun getWaterProgress(context: Context, dayNumber: String): Flow<Float> {
        val WATER_KEY = floatPreferencesKey(getWaterProgressKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            prefs[WATER_KEY] ?: 0f
        }
    }
}
