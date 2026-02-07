package com.daedan.festabook.data.datasource.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class DeviceLocalDataSourceTest {
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var deviceLocalDataSource: DeviceLocalDataSource

    lateinit var testScope: TestScope

    @BeforeTest
    fun setUp() {
        testScope = TestScope(UnconfinedTestDispatcher())
        dataStore = createTestDataStore(testScope)
        deviceLocalDataSource = DeviceLocalDataSourceImpl(dataStore)
    }

    @AfterTest
    fun tearDown() {
        deleteTestDataStore()
    }

    @Test
    fun `Uuid를 읽고 쓸 수 있다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(dataStore)

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
            dataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(dataStore)
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
            dataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(dataStore)

            // when
            val result = deviceLocalDataSource.getUuid()

            // then
            assertNull(result.first())
        }

    @Test
    fun `DeviceId가 저장되지 않았다면 null을 반환한다`() =
        runTest {
            // given
            dataStore = createTestDataStore(this)
            deviceLocalDataSource = DeviceLocalDataSourceImpl(dataStore)

            // when
            val result = deviceLocalDataSource.getDeviceId()

            // then
            assertNull(result.first())
        }
}
