package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun GroovinSwitch(checked: Boolean,
                  onCheckedChange: ((Boolean) -> Unit)?,
                  modifier: Modifier = Modifier,
                  enabled: Boolean = true) {
    Switch(
        checked = checked,
        modifier = modifier,
        onCheckedChange = onCheckedChange,
        enabled = enabled
    )
}
