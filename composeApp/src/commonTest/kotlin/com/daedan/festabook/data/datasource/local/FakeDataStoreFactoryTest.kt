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
    private lateinit var testDataStore: TestDataStore

    @Test
    fun `createTestDataStore는 파일을 생성하고 deleteTestDataStore는 파일을 삭제한다`() =
        runTest {
            // given

            // when
            testDataStore = createTestDataStore(this)

            val testKey = stringPreferencesKey("제이")
            testDataStore.store.edit { prefs ->
                prefs[testKey] = "제이 천재"
            }

            // then
            assertTrue(FileSystem.SYSTEM.exists(testDataStore.path))

            // when
            deleteTestDataStore(testDataStore.path)

            // then
            assertFalse(FileSystem.SYSTEM.exists(testDataStore.path))
        }
}
