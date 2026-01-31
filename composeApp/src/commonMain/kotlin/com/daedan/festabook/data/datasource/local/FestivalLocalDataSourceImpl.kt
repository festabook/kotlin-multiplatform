package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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

    override fun getFestivalId(): Flow<Long?> =
        dataStore.data
            .catch {
                if (it is IOException) emit(emptyPreferences()) else throw it
            }.map { it[KEY_FESTIVAL_ID] }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getIsFirstVisit(): Flow<Boolean> =
        getFestivalId().flatMapLatest { festivalId ->
            val key = booleanPreferencesKey("${KEY_IS_FIRST_VISIT}_$festivalId")
            var isFirstVisit = true
            dataStore.edit { preferences ->
                isFirstVisit = preferences[key] ?: true
                if (isFirstVisit) preferences[key] = false
            }

            flowOf(isFirstVisit)
        }

    companion object {
        private const val KEY_IS_FIRST_VISIT = "is_first_visit"
        private val KEY_FESTIVAL_ID = longPreferencesKey("festival_id")
    }
}
