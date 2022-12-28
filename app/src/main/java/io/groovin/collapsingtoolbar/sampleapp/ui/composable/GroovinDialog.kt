package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.Black


@Composable
fun GroovinDialogSurface(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        color = MaterialTheme.colors.onSurface,
        contentColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth().padding(6.dp)
    ) {
        content()
    }
}

@Composable
fun GroovinDialog(
    cancelable: Boolean = true,
    onDismiss: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = { if (cancelable) onDismiss() }) {
        GroovinDialogSurface {
            content()
        }
    }
}

@Composable
fun GroovinOkayCancelDialog(
    showOkayButton: Boolean = true,
    showCancelButton: Boolean = true,
    cancelable: Boolean = true,
    okayButtonText: String? = null,
    cancelButtonText: String? = null,
    onCancelClick: () -> Unit = {},
    onPositiveClick: () -> Unit = {},
    title: String? = null,
    content: @Composable () -> Unit
) {
    GroovinDialog(
        cancelable = cancelable,
        onDismiss = onCancelClick
    ) {
        Column(modifier = Modifier.padding(16.dp, 10.dp, 16.dp, 10.dp)) {
            if (title != null) {
                Row(
                    modifier = Modifier.padding(0.dp, 6.dp, 0.dp, 6.dp)
                ) {
                    Text(
                        text = title,
                        color = Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Row(
                modifier = Modifier.padding(0.dp, 6.dp, 0.dp, 6.dp)
            ) {
                content()
            }
            Row {
                ConstraintLayout(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val (cancelButton, okayButton) = createRefs()
                    if (showCancelButton) {
                        TextButton(
                            onClick = onCancelClick,
                            modifier = Modifier
                                .constrainAs(cancelButton) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(if (showOkayButton) okayButton.start else parent.end)
                                }
                                .fillMaxWidth(if (showOkayButton) 0.5f else 1f)
                        ) {
                            Text(
                                text = cancelButtonText ?: "CANCEL",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    if (showOkayButton) {
                        TextButton(
                            onClick = onPositiveClick,
                            modifier = Modifier
                                .constrainAs(okayButton) {
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(if (showCancelButton) cancelButton.end else parent.start)
                                    end.linkTo(parent.end)
                                }
                                .fillMaxWidth(if (showCancelButton) 0.5f else 1f)
                        ) {
                            Text(
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                text = okayButtonText ?: "OK"
                            )
                        }
                    }
                }
            }
        }
    }
}