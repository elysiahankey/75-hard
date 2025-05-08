package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object WaterHelper {

    private fun getWaterProgressKey(dayNumber: String): String {
        return "water_progress_int_$dayNumber"
    }

    suspend fun saveWaterProgress(context: Context, progress: Int, dayNumber: String) {
        val waterKey = intPreferencesKey(getWaterProgressKey(dayNumber)) // Dynamic key based on day number
        context.dataStore.edit { prefs ->
            prefs[waterKey] = progress
        }
    }

    fun getWaterProgress(context: Context, dayNumber: String): Flow<Int> {
        val waterKey = intPreferencesKey(getWaterProgressKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            prefs[waterKey] ?: 0
        }
    }

    suspend fun getAllWater(context: Context): List<Pair<Int, Int>> {
        val prefs = context.dataStore.data.first()
        val waterEntries = mutableListOf<Pair<Int, Int>>()

        for (day in 1..75) {
            val dayNumber = day.toString()
            val waterKey = intPreferencesKey("water_progress_int_$dayNumber")
            val value = prefs[waterKey] ?: 0
            if (value > 0) {
                waterEntries.add(day to value)
            }
        }
        return waterEntries
    }
}
