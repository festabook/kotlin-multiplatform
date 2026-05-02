package com.daedan.festabook.presentation.news.faq.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.daedan.festabook.logging.ScreenViewLogger
import com.daedan.festabook.presentation.common.component.EmptyStateScreen
import com.daedan.festabook.presentation.common.component.ErrorStateScreen
import com.daedan.festabook.presentation.common.component.LoadingStateScreen
import com.daedan.festabook.presentation.news.component.NewsItem
import com.daedan.festabook.presentation.news.faq.FAQUiState
import com.daedan.festabook.presentation.news.faq.model.FAQItemUiModel
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.tab_faq_question
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FAQScreen(
    uiState: FAQUiState,
    onFaqClick: (FAQItemUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScreenViewLogger("FAQScreen")

    when (uiState) {
        is FAQUiState.Error -> {
//            Timber.w(uiState.throwable.stackTraceToString())
            ErrorStateScreen(modifier = modifier.fillMaxSize())
        }

        is FAQUiState.InitialLoading -> {
            LoadingStateScreen()
        }

        is FAQUiState.Success -> {
            if (uiState.faqs.isEmpty()) {
                EmptyStateScreen()
            } else {
                LazyColumn(
                    modifier = modifier,
                    contentPadding =
                        PaddingValues(
                            top = festabookSpacing.paddingBody2,
                            bottom = festabookSpacing.paddingBody2,
                        ),
                    verticalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody2),
                ) {
                    items(
                        items = uiState.faqs,
                        key = { faq -> faq.questionId },
                    ) { faq ->
                        NewsItem(
                            title = stringResource(Res.string.tab_faq_question, faq.question),
                            description = faq.answer,
                            isExpanded = faq.isExpanded,
                            onclick = { onFaqClick(faq) },
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FAQScreenPreview() {
    FAQScreen(uiState = FAQUiState.Success(emptyList()), onFaqClick = {})
}
