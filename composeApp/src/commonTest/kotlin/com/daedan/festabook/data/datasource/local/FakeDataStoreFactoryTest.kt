package com.daedan.festabook.data.datasource.local

import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okio.FileSystem
import okio.SYSTEM
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class FakeDataStoreFactoryTest {
    @Test
    fun `createTestDataStore는 파일을 생성하고 deleteTestDataStore는 파일을 삭제한다`() =
        runTest {
            // given
            val fileName = "test.preferences_pb"
            val filePath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY / fileName

            // when
            val dataStore =
                createTestDataStore(
                    scope = this,
                    fileName = fileName,
                )

            val testKey = stringPreferencesKey("제이")
            dataStore.edit { prefs ->
                prefs[testKey] = "제이 천재"
            }

            // then
            assertTrue(FileSystem.SYSTEM.exists(filePath))

            // when
            deleteTestDataStore(fileName)

            // then
            assertFalse(FileSystem.SYSTEM.exists(filePath))
        }
}
