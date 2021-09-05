package com.acanel.groovinapp.data

import androidx.compose.ui.text.AnnotatedString

data class DialogShowRoomMenuVO(
    val menuTitle: String,
    val dialogInfo: DialogInfo
)

sealed class DialogInfo {
    data class OkayCancelDialog(
        val title: String,
        val contents: AnnotatedString,
        val showOkayButton: Boolean = true,
        val showCancelButton: Boolean = true
    ) : DialogInfo()
}