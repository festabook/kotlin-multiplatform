package com.daedan.festabook.data.datasource.remote.faq

import com.daedan.festabook.data.datasource.remote.ApiResult
import com.daedan.festabook.data.datasource.remote.schedule.FAKE_HTTP_RESPONSE
import com.daedan.festabook.data.model.response.faq.FAQResponse
import com.daedan.festabook.data.service.FAQService
import de.jensklingenberg.ktorfit.Response
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val FAKE_FAQS: List<FAQResponse> =
    listOf(
        FAQResponse(
            questionId = 1,
            question = "Q1",
            answer = "A1",
            sequence = 1,
        ),
        FAQResponse(
            questionId = 2,
            question = "Q3",
            answer = "A2",
            sequence = 1,
        ),
    )

@Suppress("UNCHECKED_CAST")
private val FAKE_FAQ_HTTP_RESPONSE: Response<List<FAQResponse>> =
    Response.success(
        rawResponse = FAKE_HTTP_RESPONSE,
        body = FAKE_FAQS,
    ) as Response<List<FAQResponse>>

class FAQDataSourceTest {
    private lateinit var faqService: FAQService
    private lateinit var faqDataSource: FAQDataSource

    @BeforeTest
    fun setUp() {
        faqService = mock()
        faqDataSource = FAQDataSourceImpl(faqService)
    }

    @Test
    fun `FAQ 전체 목록을 가져올 수 있다`() =
        runTest {
            // given
            everySuspend { faqService.fetchAllFAQs() } returns FAKE_FAQ_HTTP_RESPONSE

            // when
            val expected = ApiResult.toApiResult { FAKE_FAQ_HTTP_RESPONSE }
            val result = faqDataSource.fetchAllFAQs()

            // then
            verifySuspend { faqService.fetchAllFAQs() }
            assertEquals(expected, result)
        }
}
