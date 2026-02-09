package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.test.TestScope
import okio.FileSystem
import okio.SYSTEM
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

private const val TEST_DATASTORE_FILE_NAME = "test_prefs.preferences_pb"

@OptIn(ExperimentalTime::class)
private fun getTestDataStoreFile(): String = "test_prefs_${Clock.System.now()}.preferences_pb"

fun createTestDataStore(
    scope: TestScope,
    fileName: String = getTestDataStoreFile(),
): DataStore<Preferences> {
    val testFilePath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / fileName

    return PreferenceDataStoreFactory.createWithPath(
        scope = scope,
        produceFile = { testFilePath },
    )
}

fun deleteTestDataStore(fileName: String = TEST_DATASTORE_FILE_NAME) {
    val testFilePath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / fileName
    FileSystem.SYSTEM.delete(testFilePath)
}
