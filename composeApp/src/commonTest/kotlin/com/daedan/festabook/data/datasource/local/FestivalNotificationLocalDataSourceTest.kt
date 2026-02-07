package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FestivalNotificationLocalDataSourceTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var festivalNotificationLocalDataSource: FestivalNotificationLocalDataSource

    @AfterTest
    fun tearDown() {
        deleteTestDataStore()
    }

    @Test
    fun `festivalNotificationId를 읽고 쓸 수 있다`() =
        runTest {
            // given
            val expected = 1L
            dataStore = createTestDataStore(this)
            festivalNotificationLocalDataSource =
                FestivalNotificationLocalDataSourceImpl(
                    dataStore = dataStore,
                )

            // when
            festivalNotificationLocalDataSource.saveFestivalNotificationId(
                festivalId = 1L,
                festivalNotificationId = expected,
            )

            // then
            val result = festivalNotificationLocalDataSource.getFestivalNotificationId(1L).first()
            assertEquals(expected, result)
        }

    @Test
    fun `festivalNotificationId가 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            festivalNotificationLocalDataSource =
                FestivalNotificationLocalDataSourceImpl(
                    dataStore = dataStore,
                )

            // when
            val result = festivalNotificationLocalDataSource.getFestivalNotificationId(1L).first()

            // then
            assertNull(result)
        }

    @Test
    fun `festivalNotificationId를 삭제 할 수 있다`() =
        runTest {
            // given
            val festivalNotificationId = 1L
            val festivalId = 1L

            dataStore = createTestDataStore(this)
            festivalNotificationLocalDataSource = FestivalNotificationLocalDataSourceImpl(dataStore)

            festivalNotificationLocalDataSource.saveFestivalNotificationId(
                festivalId = festivalId,
                festivalNotificationId = festivalNotificationId,
            )

            // when
            festivalNotificationLocalDataSource.deleteFestivalNotificationId(festivalId)

            // then
            val result = festivalNotificationLocalDataSource.getFestivalNotificationId(1L).first()
            assertNull(result)
        }

    @Test
    fun `알림 허용 여부를 읽고 쓸 수 있다`() =
        runTest {
            // given
            val festivalId = 1L
            val expected = true
            dataStore = createTestDataStore(this)
            festivalNotificationLocalDataSource = FestivalNotificationLocalDataSourceImpl(dataStore)

            // when
            festivalNotificationLocalDataSource.saveFestivalNotificationIsAllowed(
                festivalId = festivalId,
                isAllowed = expected,
            )

            // then
            val result =
                festivalNotificationLocalDataSource
                    .getFestivalNotificationIsAllowed(festivalId)
                    .first()
            assertEquals(expected, result)
        }
}
