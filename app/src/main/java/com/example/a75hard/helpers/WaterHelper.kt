package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object WaterHelper {

    private fun getWaterProgressKey(dayNumber: String): String {
        return "water_progress_$dayNumber"
    }

    suspend fun saveWaterProgress(context: Context, progress: Float, dayNumber: String) {
        val waterKey = floatPreferencesKey(getWaterProgressKey(dayNumber)) // Dynamic key based on day number
        context.dataStore.edit { prefs ->
            prefs[waterKey] = progress
        }
    }

    fun getWaterProgress(context: Context, dayNumber: String): Flow<Float> {
        val waterKey = floatPreferencesKey(getWaterProgressKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            prefs[waterKey] ?: 0f
        }
    }

    suspend fun getAllWater(context: Context): List<Pair<Int, Float>> {
        val prefs = context.dataStore.data.first()
        val waterEntries = mutableListOf<Pair<Int, Float>>()

        for (day in 1..75) {
            val dayNumber = day.toString()
            val waterKey = floatPreferencesKey("water_progress_$dayNumber")
            val value = prefs[waterKey] ?: 0f
            val result = value * 3000f
            if (value > 0f) {
                waterEntries.add(day to result)
            }
        }
        return waterEntries
    }
}
