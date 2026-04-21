package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
@Inject
class WaitingLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : WaitingLocalDataSource {
    override suspend fun savePhoneNumber(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[KEY_PHONE_NUMBER] = phoneNumber
        }
    }

    override fun getPhoneNumber(): Flow<String?> =
        dataStore.data
            .catch {
                if (it is IOException) emit(emptyPreferences()) else throw it
            }.map { it[KEY_PHONE_NUMBER] }

    override suspend fun savePlaceId(placeId: Long) {
        dataStore.edit { it[KEY_PLACE_ID] = placeId }
    }

    override fun getPlaceId(): Flow<Long?> =
        dataStore.data
            .catch {
                if (it is IOException) emit(emptyPreferences()) else throw it
            }.map { it[KEY_PLACE_ID] }

    override suspend fun clearPlaceId() {
        dataStore.edit { it.remove(KEY_PLACE_ID) }
    }

    companion object {
        private val KEY_PHONE_NUMBER = stringPreferencesKey("waiting_phone_number")
        private val KEY_PLACE_ID = longPreferencesKey("waiting_place_id")
    }
}
