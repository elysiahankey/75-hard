package com.example.a75hard.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.a75hard.R

@Composable
fun ResetAppDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    val openAlertDialog = remember { mutableStateOf(false) }

    when {
        openAlertDialog.value -> {
            AlertDialog(
                title = {
                    Text(text = stringResource(R.string.reset_app_dialog_title))
                },
                text = {
                    Text(text = stringResource(R.string.reset_app_dialog_text))
                },
                onDismissRequest = {
                    onDismissRequest()
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onConfirmation()
                        }
                    ) {
                        Text(stringResource(R.string.reset_app_dialog_confirm))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onDismissRequest()
                        }
                    ) {
                        Text(stringResource(R.string.reset_app_dialog_cancel))
                    }
                }
            )
        }
    }
}

@Preview
@Composable
fun ResetAppDialogPreview() {
    ResetAppDialog(
        onDismissRequest = {},
        onConfirmation = {},
    )
}