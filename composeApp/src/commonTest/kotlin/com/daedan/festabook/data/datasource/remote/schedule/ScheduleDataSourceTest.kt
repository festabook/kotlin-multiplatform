package com.daedan.festabook.data.datasource.remote.schedule

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.service.ScheduleService
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class ScheduleDataSourceTest {
    private lateinit var scheduleService: ScheduleService
    private lateinit var scheduleDataSource: ScheduleDataSource

    @BeforeTest
    fun setUp() {
        scheduleService = mock()
        scheduleDataSource = ScheduleDataSourceImpl(scheduleService)
    }

    @Test
    fun `해당 날짜 id에 맞는 일정 데이터를 가져올 수 있다`() =
        runTest {
            // given
            val id = 1L
            everySuspend { scheduleService.fetchScheduleEventsById(id) } returns FAKE_SCHEDULE_EVENTS_RESPONSES

            // when
            val expected = ApiResult.toApiResult { FAKE_SCHEDULE_EVENTS_RESPONSES }
            val result = scheduleDataSource.fetchScheduleEventsById(id)

            // then
            verifySuspend { scheduleService.fetchScheduleEventsById(id) }
            assertEquals(expected, result)
        }

    @Test
    fun `모든 날짜 데이터를 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { scheduleService.fetchScheduleDates() } returns FAKE_SCHEDULE_DATES_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_SCHEDULE_DATES_RESPONSE }
            val result = scheduleDataSource.fetchScheduleDates()

            // then
            verifySuspend { scheduleService.fetchScheduleDates() }
            assertEquals(expected, result)
        }
}
