package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.test.TestScope
import okio.FileSystem
import okio.Path
import okio.SYSTEM
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
data class TestDataStore(
    val store: DataStore<Preferences>,
    val fileName: String,
    val path: Path,
)

@OptIn(ExperimentalTime::class)
fun createTestDataStore(scope: TestScope): TestDataStore {
    val fileName = "test_prefs_${Clock.System.now()}.preferences_pb"
    val testFilePath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / fileName
    val dataStore =
        PreferenceDataStoreFactory.createWithPath(
            scope = scope,
            produceFile = { testFilePath },
        )

    return TestDataStore(store = dataStore, fileName = fileName, path = testFilePath)
}

fun deleteTestDataStore(path: Path) {
    if (FileSystem.SYSTEM.exists(path)) FileSystem.SYSTEM.delete(path)
}
