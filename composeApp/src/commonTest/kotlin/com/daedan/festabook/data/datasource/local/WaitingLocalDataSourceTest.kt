package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WaitingLocalDataSourceTest {
    private lateinit var testDataStore: TestDataStore
    private lateinit var waitingLocalDataSource: WaitingLocalDataSource

    @AfterTest
    fun tearDown() {
        deleteTestDataStore(testDataStore.path)
    }

    @Test
    fun `전화번호를 읽고 쓸 수 있다`() =
        runTest {
            // given
            val expected = "010-1234-5678"
            testDataStore = createTestDataStore(this)
            waitingLocalDataSource = WaitingLocalDataSourceImpl(testDataStore.store)

            // when
            waitingLocalDataSource.savePhoneNumber(expected)
            val result = waitingLocalDataSource.getPhoneNumber().first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `전화번호가 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            testDataStore = createTestDataStore(this)
            waitingLocalDataSource = WaitingLocalDataSourceImpl(testDataStore.store)

            // when
            val result = waitingLocalDataSource.getPhoneNumber().first()

            // then
            assertNull(result)
        }

    @Test
    fun `전화번호를 덮어쓸 수 있다`() =
        runTest {
            // given
            val expected = "010-9999-8888"
            testDataStore = createTestDataStore(this)
            waitingLocalDataSource = WaitingLocalDataSourceImpl(testDataStore.store)
            waitingLocalDataSource.savePhoneNumber("010-1234-5678")

            // when
            waitingLocalDataSource.savePhoneNumber(expected)
            val result = waitingLocalDataSource.getPhoneNumber().first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `placeId를 저장하고 읽을 수 있다`() =
        runTest {
            // given
            val expected = 123L
            testDataStore = createTestDataStore(this)
            waitingLocalDataSource = WaitingLocalDataSourceImpl(testDataStore.store)

            // when
            waitingLocalDataSource.savePlaceId(expected)
            val result = waitingLocalDataSource.getPlaceId().first()

            // then
            assertEquals(expected, result)
        }

    @Test
    fun `placeId가 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            testDataStore = createTestDataStore(this)
            waitingLocalDataSource = WaitingLocalDataSourceImpl(testDataStore.store)

            // when
            val result = waitingLocalDataSource.getPlaceId().first()

            // then
            assertNull(result)
        }

    @Test
    fun `placeId를 삭제하면 null을 반환한다`() =
        runTest {
            // given
            testDataStore = createTestDataStore(this)
            waitingLocalDataSource = WaitingLocalDataSourceImpl(testDataStore.store)
            waitingLocalDataSource.savePlaceId(999L)

            // when
            waitingLocalDataSource.clearPlaceId()
            val result = waitingLocalDataSource.getPlaceId().first()

            // then
            assertNull(result)
        }
}
