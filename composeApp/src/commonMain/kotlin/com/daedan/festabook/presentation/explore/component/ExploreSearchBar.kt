package com.daedan.festabook.presentation.explore.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTheme
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.explore_no_search_result_text
import festabookkmp.composeapp.generated.resources.explore_search_hint_text
import festabookkmp.composeapp.generated.resources.ic_close
import festabookkmp.composeapp.generated.resources.ic_search
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExploreSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    isError: Boolean,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier =
            modifier
                .fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(Res.string.explore_search_hint_text),
                style = FestabookTypography.titleMedium,
                color = FestabookColor.gray400,
            )
        },
        supportingText =
            if (isError) {
                {
                    Text(
                        stringResource(Res.string.explore_no_search_result_text),
                        color = FestabookColor.error,
                    )
                }
            } else {
                null
            },
        singleLine = true,
        textStyle =
            FestabookTypography.titleMedium.copy(
                color = FestabookColor.gray800,
            ),
        shape = festabookShapes.radiusFull,
        colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = FestabookColor.gray800,
                unfocusedBorderColor = FestabookColor.gray400,
                errorBorderColor = FestabookColor.error,
                cursorColor = FestabookColor.gray800,
                errorCursorColor = FestabookColor.error,
                disabledBorderColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
            ),
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = "Clear text",
                        tint = Color.Unspecified,
                    )
                }
            } else {
                IconButton(onClick = { onSearch(query) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_search),
                        contentDescription = "Search",
                        tint = Color.Unspecified,
                    )
                }
            }
        },
        keyboardOptions =
            KeyboardOptions(
                imeAction = ImeAction.Search,
                showKeyboardOnFocus = true,
            ),
        keyboardActions =
            KeyboardActions(
                onSearch = {
                    onSearch(query)
                    keyboardController?.hide()
                },
            ),
        isError = isError,
    )
}

@Preview(showBackground = true)
@Composable
private fun ExploreSearchBarPreview() {
    FestabookTheme {
        ExploreSearchBar(
            query = "",
            onQueryChange = {},
            isError = false,
            modifier = Modifier.padding(16.dp),
            onSearch = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ExploreSearchBarErrorPreview() {
    FestabookTheme {
        ExploreSearchBar(
            query = "서울시립대학교",
            onQueryChange = {},
            isError = true,
            modifier = Modifier.padding(16.dp),
            onSearch = {},
        )
    }
}
