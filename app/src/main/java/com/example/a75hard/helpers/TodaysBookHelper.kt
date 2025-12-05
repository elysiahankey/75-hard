package com.example.a75hard.helpers

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object TodaysBookHelper {
    private fun getBookKey(dayNumber: String): String {
        return "book_${dayNumber}"
    }

    fun getBookFlow(context: Context, dayNumber: String): Flow<String> {
        val dayKey = stringPreferencesKey(getBookKey(dayNumber))
        return context.dataStore.data.map { prefs ->
            val dayValue = prefs[dayKey]
            dayValue ?: ""
        }
    }

    suspend fun getOrInitBookState(context: Context, dayNumber: String): String {
        val dayKey = stringPreferencesKey(getBookKey(dayNumber))

        val prefs = context.dataStore.data.first()
        val dayValue = prefs[dayKey]

        return dayValue ?: ""
    }


    suspend fun getYesterdaysBook(context: Context, dayNumber: String): String {
        val yesterday = dayNumber.toInt() - 1
        val yesterdayString = yesterday.toString()

        return getOrInitBookState(context, yesterdayString)
    }

    suspend fun saveBookState(context: Context, bookText: String, dayNumber: String) {
        val dayKey = stringPreferencesKey(getBookKey(dayNumber))

        context.dataStore.edit {
            it[dayKey] = bookText
        }
    }


}