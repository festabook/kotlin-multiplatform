package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FestivalLocalDataSourceTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var festivalLocalDataSource: FestivalLocalDataSource

    @AfterTest
    fun tearDown() {
        deleteTestDataStore()
    }

    @Test
    fun `festivalId를 읽고 쓸 수 있다`() =
        runTest {
            // given
            val expected = 1L
            dataStore = createTestDataStore(this)
            festivalLocalDataSource = FestivalLocalDataSourceImpl(dataStore)

            // when
            festivalLocalDataSource.saveFestivalId(expected)
            val result = festivalLocalDataSource.getFestivalId().first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `festivalId가 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            festivalLocalDataSource = FestivalLocalDataSourceImpl(dataStore)

            // when
            val result = festivalLocalDataSource.getFestivalId().first()

            // then
            assertNull(result)
        }

    @Test
    fun `첫 방문이라면 true를 반환한다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            festivalLocalDataSource = FestivalLocalDataSourceImpl(dataStore)

            // when
            val result = festivalLocalDataSource.getIsFirstVisit().first()

            // then
            assertTrue(result)
        }

    @Test
    fun `첫 방문이 아니라면 false를 반환한다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            festivalLocalDataSource = FestivalLocalDataSourceImpl(dataStore)
            festivalLocalDataSource.getIsFirstVisit().first()

            // when
            val result = festivalLocalDataSource.getIsFirstVisit().first()

            // then
            assertFalse(result)
        }
}
