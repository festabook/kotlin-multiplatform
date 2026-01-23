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
class FestivalLocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
) : FestivalLocalDataSource {
    override suspend fun saveFestivalId(festivalId: Long) {
        dataStore.edit { preferences ->
            preferences[KEY_FESTIVAL_ID] = festivalId
        }
    }

    override suspend fun getFestivalId(): Long? = dataStore.data.firstOrNull()?.get(KEY_FESTIVAL_ID)

    override suspend fun getIsFirstVisit(): Boolean {
        val festivalId = getFestivalId() ?: return true
        val key = booleanPreferencesKey("${KEY_IS_FIRST_VISIT}_$festivalId")
        val isFirstVisit = dataStore.data.firstOrNull()?.get(key) ?: true

        if (isFirstVisit) dataStore.edit { preferences -> preferences[key] = false }
        return isFirstVisit
    }

    companion object {
        private const val KEY_IS_FIRST_VISIT = "is_first_visit"
        private val KEY_FESTIVAL_ID = longPreferencesKey("festival_id")
    }
}
