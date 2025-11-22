package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object CheckboxHelper {
    private fun getCheckboxKey(dayNumber: String, item: Int): String {
        return "checkbox_${dayNumber}_$item"
    }

    suspend fun saveCheckboxState(context: Context, dayNumber: String, item: Int, isChecked: Boolean) {
        val checkboxKey = booleanPreferencesKey(getCheckboxKey(dayNumber, item))
        context.dataStore.edit { prefs ->
            prefs[checkboxKey] = isChecked
        }
    }

    fun getCheckboxState(context: Context, dayNumber: String, item: Int): Flow<Boolean> {
        val checkboxKey = booleanPreferencesKey(getCheckboxKey(dayNumber, item))
        return context.dataStore.data.map { prefs ->
            prefs[checkboxKey] == true
        }
    }

    suspend fun resetAllCheckboxes(context: Context, dayNumber: String, items: List<Int>) {
        context.dataStore.edit { prefs ->
            for (item in items) {
                val key = booleanPreferencesKey(getCheckboxKey(dayNumber, item))
                prefs[key] = false
            }
        }
    }
}

