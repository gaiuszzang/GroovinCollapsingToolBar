package com.acanel.groovinapp.repository.impl

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.acanel.groovinapp.di.SettingDataStore
import com.acanel.groovinapp.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreSettingRepository @Inject constructor(
    @SettingDataStore private val dataStore: DataStore<Preferences>
) : SettingRepository {
    private val is24HourDisplay by lazy { booleanPreferencesKey("is24HourDisplay") }
    private val isBurnInPrevention by lazy { booleanPreferencesKey("burnInPrevention") }
    private val clockFontSizeLevel by lazy { intPreferencesKey("clockFontSizeLevel") }


    private fun <T> getPreferenceFlow(key: Preferences.Key<T>, defaultValue: T): Flow<T> =
        dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }

    private suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    override fun is24HourDisplay(): Flow<Boolean> = getPreferenceFlow(is24HourDisplay, false)
    override suspend fun set24HourDisplay(on: Boolean) = setPreference(is24HourDisplay, on)

    override fun isBurnInPrevention(): Flow<Boolean> = getPreferenceFlow(isBurnInPrevention, true)
    override suspend fun setBurnInPrevention(on: Boolean) = setPreference(isBurnInPrevention, on)

    override fun getClockFontSizeLevel(): Flow<Int> = getPreferenceFlow(clockFontSizeLevel, 2)
    override suspend fun setClockFontSizeLevel(level: Int) = setPreference(clockFontSizeLevel, level)

}