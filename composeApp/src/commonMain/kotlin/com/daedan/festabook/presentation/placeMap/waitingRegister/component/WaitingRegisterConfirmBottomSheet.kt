package com.daedan.festabook.presentation.placeMap.waitingRegister.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.daedan.festabook.presentation.theme.FestabookColor
import com.daedan.festabook.presentation.theme.FestabookTypography
import com.daedan.festabook.presentation.theme.festabookShapes
import com.daedan.festabook.presentation.theme.festabookSpacing
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.img_waiting_push_notification_alert
import festabookkmp.composeapp.generated.resources.waiting_register_confirm_checkbox
import festabookkmp.composeapp.generated.resources.waiting_register_confirm_description
import festabookkmp.composeapp.generated.resources.waiting_register_confirm_later
import festabookkmp.composeapp.generated.resources.waiting_register_confirm_submit
import festabookkmp.composeapp.generated.resources.waiting_register_confirm_title
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaitingRegisterConfirmBottomSheet(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var isChecked by rememberSaveable { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = FestabookColor.white,
        modifier = modifier,
        dragHandle = {},
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = festabookSpacing.paddingScreenGutter)
                    .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(Res.drawable.img_waiting_push_notification_alert),
                contentDescription = null,
                modifier =
                    Modifier
                        .padding(vertical = festabookSpacing.paddingTitleHorizontal)
                        .size(100.dp),
            )

            Text(
                text = stringResource(Res.string.waiting_register_confirm_title),
                style = FestabookTypography.displaySmall,
                color = FestabookColor.black,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(festabookSpacing.paddingBody2))

            Text(
                text = stringResource(Res.string.waiting_register_confirm_description),
                style = FestabookTypography.bodyLarge,
                color = FestabookColor.gray500,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { isChecked = !isChecked },
            ) {
                Checkbox(
                    modifier = Modifier.clip(festabookShapes.radius1),
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = FestabookColor.black,
                            uncheckedColor = FestabookColor.gray400,
                            checkmarkColor = FestabookColor.white,
                        ),
                )
                Text(
                    text = stringResource(Res.string.waiting_register_confirm_checkbox),
                    style = FestabookTypography.bodyMedium,
                    color = FestabookColor.black,
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(festabookSpacing.paddingBody4),
            ) {
                ConfirmBottomSheetButton(
                    text = stringResource(Res.string.waiting_register_confirm_later),
                    isEnabled = true,
                    isOutlined = true,
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                )

                ConfirmBottomSheetButton(
                    text = stringResource(Res.string.waiting_register_confirm_submit),
                    isEnabled = isChecked,
                    isOutlined = false,
                    onClick = {
                        if (isChecked) {
                            onConfirm()
                        }
                    },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun ConfirmBottomSheetButton(
    text: String,
    isEnabled: Boolean,
    isOutlined: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .clip(festabookShapes.radius2)
                .then(
                    if (isOutlined) {
                        Modifier
                            .border(
                                width = 1.dp,
                                color = FestabookColor.gray300,
                                shape = festabookShapes.radius2,
                            ).background(FestabookColor.white)
                    } else {
                        Modifier.background(
                            if (isEnabled) FestabookColor.black else FestabookColor.gray300,
                        )
                    },
                ).clickable(enabled = isEnabled || isOutlined) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.padding(vertical = festabookSpacing.paddingBody4),
            text = text,
            style = FestabookTypography.displaySmall,
            color =
                when {
                    isOutlined -> FestabookColor.black
                    isEnabled -> FestabookColor.white
                    else -> FestabookColor.white
                },
        )
    }
}
