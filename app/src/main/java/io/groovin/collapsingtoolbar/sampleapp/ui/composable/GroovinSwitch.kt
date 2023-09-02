package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.DarkSwitchDisabledColor
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.SwitchDisabledColor


@Composable
fun GroovinSwitch(checked: Boolean,
                  onCheckedChange: ((Boolean) -> Unit)?,
                  modifier: Modifier = Modifier,
                  enabled: Boolean = true) {
    Switch(
        checked = checked,
        modifier = modifier,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colors.primary,
            uncheckedThumbColor = if (isSystemInDarkTheme()) SwitchDisabledColor else DarkSwitchDisabledColor,
            checkedTrackColor = MaterialTheme.colors.primary,
            uncheckedTrackColor = if (isSystemInDarkTheme()) SwitchDisabledColor else DarkSwitchDisabledColor
        )
    )
}