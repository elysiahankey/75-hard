package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import kotlin.text.isNullOrBlank

object WaterHelper {

    private fun getWaterProgressKey(dayNumber: String): String {
        return "water_progress_$dayNumber"
    }

    suspend fun saveWaterProgress(context: Context, progress: Float, dayNumber: String) {
        val WATER_KEY = floatPreferencesKey(getWaterProgressKey(dayNumber)) // Dynamic key based on day number
        context.dataStore.edit { prefs ->
            prefs[WATER_KEY] = progress
        }
    }

    fun getWaterProgress(context: Context, dayNumber: String): Flow<Float> {
        val WATER_KEY = floatPreferencesKey(getWaterProgressKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            prefs[WATER_KEY] ?: 0f
        }
    }

    suspend fun getAllWater(context: Context): List<Pair<Int, Float>> {
        val prefs = context.dataStore.data.first()
        val waterEntries = mutableListOf<Pair<Int, Float>>()

        for (day in 1..75) {
            val dayNumber = day.toString()
            val key = floatPreferencesKey("water_progress_$dayNumber")
            val value = prefs[key] ?: 0f
            val result = value * 3000f
            if (value > 0f) {
                waterEntries.add(day to result)
            }
        }

        return waterEntries
    }
}
