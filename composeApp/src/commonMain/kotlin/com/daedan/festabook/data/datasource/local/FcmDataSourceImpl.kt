package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

@ContributesBinding(AppScope::class)
@Inject
class FcmDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : FcmDataSource {
    override suspend fun saveFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_FCM_TOKEN] = token
        }
    }

    override fun getFcmToken(): Flow<String?> = dataStore.data.catch { emit(emptyPreferences()) }.map { it[KEY_FCM_TOKEN] }

    companion object {
        private val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
    }
}
