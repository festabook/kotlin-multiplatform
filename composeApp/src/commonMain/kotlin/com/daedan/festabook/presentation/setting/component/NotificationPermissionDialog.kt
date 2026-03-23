package com.daedan.festabook.presentation.setting.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import festabookkmp.composeapp.generated.resources.Res
import festabookkmp.composeapp.generated.resources.cancel
import festabookkmp.composeapp.generated.resources.confirm
import festabookkmp.composeapp.generated.resources.notification_permission_message
import festabookkmp.composeapp.generated.resources.setting_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun NotificationPermissionDialog(onDismissRequest: () -> Unit) {
    val title = stringResource(Res.string.setting_title)
    val message = stringResource(Res.string.notification_permission_message)
    val confirmText = stringResource(Res.string.confirm)
    val cancelText = stringResource(Res.string.cancel)
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onDismissRequest) { Text(confirmText) }
        },
        dismissButton = {
            Button(onClick = onDismissRequest) { Text(cancelText) }
        },
        title = { Text(title) },
        text = { Text(message) },
    )
}
