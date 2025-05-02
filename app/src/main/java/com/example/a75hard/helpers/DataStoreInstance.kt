package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_prefs")

object DataStoreKeys {
    val COMPLETED_DAYS = stringSetPreferencesKey("completed_days")
}

class DataStoreManager(private val context: Context) {

    val completedDays: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[DataStoreKeys.COMPLETED_DAYS] ?: emptySet()
        }

    suspend fun saveCompletedDays(days: Set<String>) {
        context.dataStore.edit { preferences ->
            preferences[DataStoreKeys.COMPLETED_DAYS] = days
        }
    }
}