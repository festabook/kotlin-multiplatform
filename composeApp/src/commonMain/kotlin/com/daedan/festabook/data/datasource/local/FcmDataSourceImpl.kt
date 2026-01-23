package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.firstOrNull

@ContributesBinding(AppScope::class)
@Inject
class FcmDataSourceImpl constructor(
    private val dataStore: DataStore<Preferences>,
) : FcmDataSource {
    override suspend fun saveFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[KEY_FCM_TOKEN] = token
        }
    }

    override suspend fun getFcmToken(): String? = dataStore.data.firstOrNull()?.get(KEY_FCM_TOKEN)

    companion object {
        private val KEY_FCM_TOKEN = stringPreferencesKey("fcm_token")
    }
}
