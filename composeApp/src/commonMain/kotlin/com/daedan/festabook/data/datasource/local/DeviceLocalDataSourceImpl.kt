package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.firstOrNull

@ContributesBinding(AppScope::class)
@Inject
class DeviceLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : DeviceLocalDataSource {
    override suspend fun saveUuid(uuid: String) {
        dataStore.edit { preferences ->
            preferences[KEY_UUID] = uuid
        }
    }

    override suspend fun getUuid(): String? = dataStore.data.firstOrNull()?.get(KEY_UUID)

    override suspend fun saveDeviceId(deviceId: Long) {
        dataStore.edit { preferences ->
            preferences[KEY_DEVICE_ID] = deviceId
        }
    }

    override suspend fun getDeviceId(): Long? = dataStore.data.firstOrNull()?.get(KEY_DEVICE_ID)

    companion object {
        private val KEY_DEVICE_ID = longPreferencesKey("server_device_id")
        private val KEY_UUID = stringPreferencesKey("device_uuid")
    }
}
