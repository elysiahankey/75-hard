package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object NotesHelper {
    private fun getNotesKey(dayNumber: String): String {
        return "notes_${dayNumber}"
    }

    suspend fun saveNotesState(
        context: Context,
        dayNumber: String,
        notesText: String
    ) {
        val notesKey = stringPreferencesKey(getNotesKey(dayNumber))
        context.dataStore.edit { prefs ->
            prefs[notesKey] = notesText
        }
    }

    fun getNotesState(context: Context, dayNumber: String): Flow<String> {
        val notesKey = stringPreferencesKey(getNotesKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            prefs[notesKey] ?: "" // Default to false if not found
        }
    }
}