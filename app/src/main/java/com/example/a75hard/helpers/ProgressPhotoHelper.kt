package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ProgressPhotoHelper {
    private fun getPhotoKey(dayNumber: String): String {
        return "photo_${dayNumber}"
    }

    suspend fun savePhotoState(
        context: Context,
        dayNumber: String,
        photoUri: String
    ) {
        val photoKey = stringPreferencesKey(getPhotoKey(dayNumber))
        context.dataStore.edit { prefs ->
            prefs[photoKey] = photoUri
        }
    }

    fun getPhotoState(context: Context, dayNumber: String): Flow<String> {
        val photoKey = stringPreferencesKey(getPhotoKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            prefs[photoKey] ?: "" // Default to false if not found
        }
    }
}