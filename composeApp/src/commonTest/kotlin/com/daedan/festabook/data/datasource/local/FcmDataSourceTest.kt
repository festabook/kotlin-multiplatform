package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FcmDataSourceTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var fcmDataSource: FcmDataSource

    @AfterTest
    fun tearDown() {
        deleteTestDataStore()
    }

    @Test
    fun `DataStore에 FcmToken을 읽고 쓸 수 있다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            fcmDataSource = FcmDataSourceImpl(dataStore)
            val token = "제이"

            // when
            fcmDataSource.saveFcmToken(token)

            // then
            val result = fcmDataSource.getFcmToken()
            assertEquals(token, result.first())
        }

    @Test
    fun `FcmToken이 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            fcmDataSource = FcmDataSourceImpl(dataStore)

            // when
            val result = fcmDataSource.getFcmToken()

            // then
            assertNull(result.first())
        }
}
