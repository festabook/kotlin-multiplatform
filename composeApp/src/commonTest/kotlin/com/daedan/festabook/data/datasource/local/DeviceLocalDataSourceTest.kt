package com.daedan.festabook.data.datasource.local

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceLocalDataSourceTest {
    private lateinit var testDataStore: TestDataStore
    private lateinit var deviceLocalDataSource: DeviceLocalDataSource

    @AfterTest
    fun tearDown() {
        deleteTestDataStore(testDataStore.path)
    }

    @Test
    fun `Uuid를 읽고 쓸 수 있다`() =
        runTest {
            // given
            testDataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(testDataStore.store)

            val uuid = "제이"

            // when
            deviceLocalDataSource.saveUuid(uuid)

            // then
            val result = deviceLocalDataSource.getUuid().first()
            assertEquals(uuid, result)
        }

    @Test
    fun `DeviceId를 읽고 쓸 수 있다`() =
        runTest {
            // given
            testDataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(testDataStore.store)
            val deviceId = 1L

            // when
            deviceLocalDataSource.saveDeviceId(deviceId)

            // then
            val result = deviceLocalDataSource.getDeviceId().first()
            assertEquals(deviceId, result)
        }

    @Test
    fun `Uuid가 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            testDataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(testDataStore.store)

            // when
            val result = deviceLocalDataSource.getUuid()

            // then
            assertNull(result.first())
        }

    @Test
    fun `DeviceId가 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            testDataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(testDataStore.store)

            // when
            val result = deviceLocalDataSource.getDeviceId()

            // then
            assertNull(result.first())
        }
}
