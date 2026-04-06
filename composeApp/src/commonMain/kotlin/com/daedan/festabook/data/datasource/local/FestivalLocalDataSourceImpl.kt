package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.daedan.festabook.data.model.entity.FestivalSearchItemEntity
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

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

    override suspend fun saveRecentFestivalSearch(festivalSearchItemEntity: FestivalSearchItemEntity) {
        dataStore.edit { preferences ->
            val currentJson = preferences[FESTIVAL_SEARCH_ITEM] ?: ""

            val currentList =
                if (currentJson.isEmpty()) {
                    emptyList<FestivalSearchItemEntity>()
                } else {
                    Json.decodeFromString(currentJson)
                }
            val updatedList =
                (
                    listOf(festivalSearchItemEntity) +
                        currentList
                            .filter { it.festivalId != festivalSearchItemEntity.festivalId }
                            .take(MAX_RECENT_SEARCH_COUNT)
                )

            preferences[FESTIVAL_SEARCH_ITEM] = Json.encodeToString(updatedList)
        }
    }

    override fun getRecentFestivalSearches(): Flow<List<FestivalSearchItemEntity>> =
        dataStore.data
            .catch {
                if (it is IOException) emit(emptyPreferences()) else throw it
            }.map {
                val json = it[FESTIVAL_SEARCH_ITEM] ?: ""
                if (json.isEmpty()) emptyList() else Json.decodeFromString(json)
            }

    override suspend fun clearRecentFestivalSearches() {
        dataStore.edit { preferences ->
            preferences[FESTIVAL_SEARCH_ITEM] =
                Json.encodeToString(emptyList<FestivalSearchItemEntity>())
        }
    }

    override fun getIsFirstVisit(): Flow<Boolean> =
        getFestivalId().map { festivalId ->
            val key = booleanPreferencesKey("${KEY_IS_FIRST_VISIT}_$festivalId")
            var isFirstVisit = true
            dataStore.edit { preferences ->
                isFirstVisit = preferences[key] ?: true
                if (isFirstVisit) preferences[key] = false
            }

            isFirstVisit
        }

    companion object {
        private const val KEY_IS_FIRST_VISIT = "is_first_visit"
        private const val MAX_RECENT_SEARCH_COUNT = 5
        private val FESTIVAL_SEARCH_ITEM = stringPreferencesKey("festival_search_item")
        private val KEY_FESTIVAL_ID = longPreferencesKey("festival_id")
    }
}
