package com.daedan.festabook.presentation.setting.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.daedan.festabook.presentation.theme.FestabookColor
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.confirm
import festabookkmp.composeapp.generated.resources.notification_permission_message
import festabookkmp.composeapp.generated.resources.setting_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NotificationPermissionDialog(onConfirm: () -> Unit) {
    val title = stringResource(Res.string.setting_title)
    val message = stringResource(Res.string.notification_permission_message)
    val confirmText = stringResource(Res.string.confirm)
    AlertDialog(
        containerColor = FestabookColor.white,
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = FestabookColor.black,
                        contentColor = FestabookColor.white,
                    ),
            ) { Text(confirmText) }
        },
        title = { Text(title) },
        text = { Text(message) },
    )
}

@Preview
@Composable
private fun NotificationPermissionDialogPreview() {
    NotificationPermissionDialog(
        onConfirm = { },
    )
}
