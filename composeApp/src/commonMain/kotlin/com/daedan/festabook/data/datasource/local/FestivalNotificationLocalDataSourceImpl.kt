package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.firstOrNull

@ContributesBinding(AppScope::class)
@Inject
class FestivalNotificationLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : FestivalNotificationLocalDataSource {
    override suspend fun saveFestivalNotificationId(
        festivalId: Long,
        festivalNotificationId: Long,
    ) {
        val key = longPreferencesKey("${KEY_FESTIVAL_NOTIFICATION_ID}_$festivalId")
        dataStore.edit { preferences ->
            preferences[key] = festivalNotificationId
        }
    }

    override suspend fun getFestivalNotificationId(festivalId: Long): Long {
        val key = longPreferencesKey("${KEY_FESTIVAL_NOTIFICATION_ID}_$festivalId")
        return dataStore.data.firstOrNull()?.get(key) ?: DEFAULT_FESTIVAL_NOTIFICATION_ID
    }

    override suspend fun deleteFestivalNotificationId(festivalId: Long) {
        val key = longPreferencesKey("${KEY_FESTIVAL_NOTIFICATION_ID}_$festivalId")
        dataStore.edit { preferences -> preferences.remove(key) }
    }

    override suspend fun clearAll() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    override suspend fun saveFestivalNotificationIsAllowed(
        festivalId: Long,
        isAllowed: Boolean,
    ) {
        val key = booleanPreferencesKey("${KEY_FESTIVAL_NOTIFICATION_ID}_$festivalId")
        dataStore.edit { preferences -> preferences[key] = isAllowed }
    }

    override suspend fun getFestivalNotificationIsAllowed(festivalId: Long): Boolean {
        val key = booleanPreferencesKey("${KEY_FESTIVAL_NOTIFICATION_IS_ALLOWED}_$festivalId")
        return dataStore.data.firstOrNull()?.get(key) ?: false
    }

    companion object {
        private const val KEY_FESTIVAL_NOTIFICATION_ID = "festival_notification_id"
        private const val DEFAULT_FESTIVAL_NOTIFICATION_ID = -1L
        private const val KEY_FESTIVAL_NOTIFICATION_IS_ALLOWED = "key_festival_notification_allowed"
    }
}
