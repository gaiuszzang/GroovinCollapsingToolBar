package io.groovin.sampleapp.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
data class MenuItem(
    val title: String,
    val topBottomPadding: Dp = 20.dp,
    val action: (() -> Unit)? = null
)