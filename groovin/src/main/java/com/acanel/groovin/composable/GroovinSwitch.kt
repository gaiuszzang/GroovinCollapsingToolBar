package com.acanel.groovin.composable

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.acanel.groovin.theme.DarkSwitchDisabledColor
import com.acanel.groovin.theme.GroovinPreviewTheme
import com.acanel.groovin.theme.SwitchDisabledColor

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
            checkedThumbColor = MaterialTheme.colors.secondary,
            uncheckedThumbColor = if (isSystemInDarkTheme()) SwitchDisabledColor else DarkSwitchDisabledColor,
            checkedTrackColor = MaterialTheme.colors.secondary,
            uncheckedTrackColor = if (isSystemInDarkTheme()) SwitchDisabledColor else DarkSwitchDisabledColor
        )
    )
}

@Preview
@Composable
fun PreviewGroovinSwitch() {
    GroovinPreviewTheme {
        Column(
            Modifier.fillMaxWidth()
        ) {
            GroovinSwitch(checked = true, {})
            GroovinSwitch(checked = false, {})
        }
    }
}