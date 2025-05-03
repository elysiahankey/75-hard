package com.example.a75hard.helpers

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.a75hard.viewmodels.DayViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

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

    suspend fun deletePhoto(context: Context, dayNumber: String) {
        withContext(Dispatchers.IO) {
            val path = getPhotoState(context, dayNumber).first() // Collect the flow
            if (path.isNotEmpty()) {
                val file = File(path)
                if (file.exists()) file.delete()
            }
            savePhotoState(context, dayNumber, "")
        }
    }
}