package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.text.get

object TodaysBookHelper {
    private fun getBookKey(dayNumber: String): String {
        return "book_${dayNumber}"
    }

    fun getBookFlow(context: Context, dayNumber: String): Flow<String> {
        val dayKey = stringPreferencesKey(getBookKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            val dayValue = prefs[dayKey]
            if (dayValue == "<cleared>") "" else dayValue ?: ""
        }
    }

    suspend fun getOrInitBookState(context: Context, dayNumber: String): String {
        val dayKey = stringPreferencesKey(getBookKey(dayNumber))
        val latestKey = stringPreferencesKey("latestBookState")

        val prefs = context.dataStore.data.first()
        val dayValue = prefs[dayKey]

        return when {
            dayValue == "<cleared>" -> ""
            dayValue != null -> dayValue
            else -> {
                val fallback = prefs[latestKey] ?: ""
                context.dataStore.edit { it[dayKey] = fallback }
                fallback
            }
        }
    }

    suspend fun saveBookState(context: Context, bookText: String, dayNumber: String) {
        val dayKey = stringPreferencesKey(getBookKey(dayNumber))
        val latestKey = stringPreferencesKey("latestBookState")

        context.dataStore.edit {
            it[dayKey] = if (bookText.isBlank()) "<cleared>" else bookText
            if (bookText.isNotBlank()) {
                it[latestKey] = bookText
            } else {
                it.remove(latestKey)
            }
        }
    }


}