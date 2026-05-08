package com.daedan.festabook.news

import com.daedan.festabook.domain.model.Lost
import com.daedan.festabook.domain.repository.FAQRepository
import com.daedan.festabook.domain.repository.LostItemRepository
import com.daedan.festabook.domain.repository.NoticeRepository
import com.daedan.festabook.presentation.news.NewsViewModel
import com.daedan.festabook.presentation.news.faq.FAQUiState
import com.daedan.festabook.presentation.news.faq.model.toUiModel
import com.daedan.festabook.presentation.news.lost.LostUiState
import com.daedan.festabook.presentation.news.lost.model.LostUiModel
import com.daedan.festabook.presentation.news.lost.model.toLostGuideItemUiModel
import com.daedan.festabook.presentation.news.lost.model.toLostItemUiModel
import com.daedan.festabook.presentation.news.notice.NoticeUiState
import com.daedan.festabook.presentation.news.notice.NoticeUiState.Companion.DEFAULT_POSITION
import com.daedan.festabook.presentation.news.notice.model.NoticeUiModel
import com.daedan.festabook.presentation.news.notice.model.toUiModel
import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verifySuspend
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class NewsViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var noticeRepository: NoticeRepository
    private lateinit var faqRepository: FAQRepository
    private lateinit var lostItemRepository: LostItemRepository
    private lateinit var newsViewModel: NewsViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        noticeRepository = mock()
        faqRepository = mock()
        lostItemRepository = mock()
        everySuspend { noticeRepository.fetchNotices() } returns Result.success(FAKE_NOTICES)
        everySuspend { faqRepository.getAllFAQ() } returns Result.success(FAKE_FAQS)
        everySuspend { lostItemRepository.getLost() } returns Result.success(FAKE_LOST_ITEM)

        newsViewModel =
            NewsViewModel(
                noticeRepository,
                faqRepository,
                lostItemRepository,
            )
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `공지사항을 불러올 수 있다`() =
        runTest {
            // given
            everySuspend { noticeRepository.fetchNotices() } returns Result.success(FAKE_NOTICES)

            // when
            newsViewModel.loadAllNotices(NoticeUiState(content = NoticeUiState.Content.InitialLoading))

            // then
            val expected = FAKE_NOTICES.map { it.toUiModel() }
            val actual = newsViewModel.noticeUiState.value
            verifySuspend { noticeRepository.fetchNotices() }
            assertEquals(
                NoticeUiState(content = NoticeUiState.Content.Success(expected, DEFAULT_POSITION)),
                actual,
            )
        }

    @Test
    fun `분실물을 불러올 수 있다`() =
        runTest {
            // given
            val expected =
                LostUiState(
                    content =
                        LostUiState.Content.Success(
                            listOf(
                                (FAKE_LOST_ITEM[0] as Lost.Guide).toLostGuideItemUiModel(),
                                (FAKE_LOST_ITEM[1] as Lost.Item).toLostItemUiModel(),
                            ),
                        ),
                )

            // when
            newsViewModel.loadAllLostItems(LostUiState(content = LostUiState.Content.InitialLoading))

            // then
            val actual = newsViewModel.lostUiState.value
            verifySuspend { lostItemRepository.getLost() }
            assertEquals(expected, actual)
        }

    @Test
    fun `공지사항 로드에 실패하면 에러 상태를 표시한다`() =
        runTest {
            // given
            val exception = Throwable("테스트")
            everySuspend { noticeRepository.fetchNotices() } returns Result.failure(exception)

            // when
            newsViewModel.loadAllNotices(NoticeUiState(content = NoticeUiState.Content.InitialLoading))

            // then
            val expected = NoticeUiState(content = NoticeUiState.Content.Error(exception))
            val actual = newsViewModel.noticeUiState.value
            verifySuspend { noticeRepository.fetchNotices() }
            assertEquals(expected, actual)
        }

    @Test
    fun `뷰모델을 생성하면 FAQ를 불러올 수 있다`() =
        runTest {
            // given
            everySuspend { faqRepository.getAllFAQ() } returns Result.success(FAKE_FAQS)

            // when
            newsViewModel = NewsViewModel(noticeRepository, faqRepository, lostItemRepository)

            // then
            val expected = FAKE_FAQS.map { it.toUiModel() }
            val actual = newsViewModel.faqUiState.value
            verifySuspend { faqRepository.getAllFAQ() }
            assertEquals(FAQUiState.Success(expected), actual)
        }

    @Test
    fun `FAQ 로드에 실패하면 에러 상태를 표시한다`() =
        runTest {
            // given
            val exception = Throwable("테스트")
            everySuspend { faqRepository.getAllFAQ() } returns Result.failure(exception)

            // when
            newsViewModel = NewsViewModel(noticeRepository, faqRepository, lostItemRepository)

            // then
            val expected = FAQUiState.Error(exception)
            val actual = newsViewModel.faqUiState.value
            verifySuspend { faqRepository.getAllFAQ() }
            assertEquals(expected, actual)
        }

    @Test
    fun `공지사항 요소를 펼치게 할 수 있다`() =
        runTest {
            // given
            val notice = FAKE_NOTICES.first().toUiModel()

            // when
            newsViewModel.toggleNotice(notice)

            // then
            val notices =
                listOf(
                    notice.copy(isExpanded = true),
                    NoticeUiModel(
                        id = 2,
                        title = "테스트 2",
                        content = "테스트 2",
                        isPinned = true,
                        createdAt = kotlinx.datetime.LocalDateTime(2025, 1, 1, 0, 0, 0),
                    ),
                )
            val actual = newsViewModel.noticeUiState.value
            val expected =
                NoticeUiState(content = NoticeUiState.Content.Success(notices, DEFAULT_POSITION))
            assertEquals(expected, actual)
        }

    @Test
    fun `FAQ 요소를 펼치게 할 수 있다`() =
        runTest {
            // given
            val faq = FAKE_FAQS.first().toUiModel()

            // when
            newsViewModel.toggleFAQ(faq)

            // then
            val expected = listOf(faq.copy(isExpanded = true))
            val actual = newsViewModel.faqUiState.value
            assertEquals(FAQUiState.Success(expected), actual)
        }

    @Test
    fun `처음 로드했을 때 펼처질 공지사항을 지정할 수 있다`() =
        runTest {
            // given
            everySuspend { noticeRepository.fetchNotices() } returns Result.success(FAKE_NOTICES)
            val notices =
                listOf(
                    FAKE_NOTICES.first().toUiModel(),
                    FAKE_NOTICES[1].toUiModel().copy(isExpanded = true),
                )

            // when
            newsViewModel.expandNotice(2)

            // then
            val actual = newsViewModel.noticeUiState.value
            val expected = NoticeUiState(content = NoticeUiState.Content.Success(notices, 1))
            verifySuspend { noticeRepository.fetchNotices() }
            assertEquals(expected, actual)
        }

    @Test
    fun `분실물 가이드라인을 펼칠 수 있다`() =
        runTest {
            // given
            val expected =
                LostUiState(
                    content =
                        LostUiState.Content.Success(
                            FAKE_LOST_ITEM_UI_MODEL.map {
                                when (it) {
                                    is LostUiModel.Guide -> it.copy(isExpanded = true)
                                    else -> it
                                }
                            },
                        ),
                )

            // when
            newsViewModel.toggleLostGuide()

            // then
            val actual = newsViewModel.lostUiState.value
            assertEquals(expected, actual)
        }
}
