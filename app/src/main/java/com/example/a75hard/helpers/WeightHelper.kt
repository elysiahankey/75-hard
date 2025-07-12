package com.example.a75hard.helpers

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object WeightHelper {
    private fun getWeightKey(dayNumber: String): String {
        return "weight_${dayNumber}"
    }

    suspend fun saveWeightState(context: Context, dayNumber: String, weight: String) {
        val weightKey = stringPreferencesKey(getWeightKey(dayNumber))
        context.dataStore.edit { prefs ->
            prefs[weightKey] = weight
        }
    }

    fun getWeightState(context: Context, dayNumber: String): Flow<String> {
        val weightKey = stringPreferencesKey(getWeightKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            prefs[weightKey] ?: ""
        }
    }

    suspend fun getAllWeights(context: Context): List<Pair<Int, String>> {
        val prefs = context.dataStore.data.first()
        val weightEntries = mutableListOf<Pair<Int, String>>()

        for (day in 1..75) {
            var dayNumber = day.toString()
            val key = stringPreferencesKey("weight_${dayNumber}")
            val weight = prefs[key]
            if (!weight.isNullOrBlank()) {
                weightEntries.add(day to weight)
            }
        }

        return weightEntries
    }

    suspend fun getWeightChange(context: Context): Double {
        val weights = getAllWeights(context = context)
        val startWeight = weights.firstOrNull()?.second.toString().toDoubleOrNull()
        val latestWeight = weights.lastOrNull()?.second.toString().toDoubleOrNull()

        if (startWeight == null || latestWeight == null) {
            return 0.0
        }

        Log.d("Weight", startWeight.toString())
        Log.d("Weight", latestWeight.toString())

        return startWeight - latestWeight
    }

    suspend fun resetWeight(context: Context, dayNumber: String) {
        context.dataStore.edit { prefs ->
                val key = stringPreferencesKey(getWeightKey(dayNumber))
                prefs[key] = ""
        }
    }
}