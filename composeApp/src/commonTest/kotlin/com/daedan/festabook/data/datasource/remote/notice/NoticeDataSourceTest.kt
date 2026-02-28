package com.daedan.festabook.data.datasource.remote.notice

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.schedule.FAKE_HTTP_RESPONSE
import com.daedan.festabook.data.model.response.notice.NoticeListResponse
import com.daedan.festabook.data.service.NoticeService
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val FAKE_NOTICE_LIST_RESPONSE =
    NoticeListResponse

@Suppress("UNCHECKED_CAST")
private val FAKE_NOTICE_HTTP_RESPONSE: Response<NoticeListResponse> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_NOTICE_LIST_RESPONSE,
    ) as Response<NoticeListResponse>

class NoticeDataSourceTest {
    private lateinit var noticeService: NoticeService
    private lateinit var noticeDataSource: NoticeDataSource

    @BeforeTest
    fun setUp() {
        noticeService = mock()
        noticeDataSource = NoticeDataSourceImpl(noticeService)
    }

    @Test
    fun `공지사항 목록을 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { noticeService.getNotices() } returns FAKE_NOTICE_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_NOTICE_HTTP_RESPONSE }
            val result = noticeDataSource.fetchNotices()

            // then
            verifySuspend { noticeService.getNotices() }
            assertEquals(expected, result)
        }
}
