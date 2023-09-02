package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun rememberStatusBarHeight(): State<Dp> {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val insets = WindowInsets.statusBars
    val paddingValues = insets.asPaddingValues(density)
    return remember(configuration, density, insets, paddingValues) {
        derivedStateOf {
            val height = paddingValues.calculateTopPadding() + paddingValues.calculateBottomPadding()
            height
        }
    }
}

fun Modifier.conditional(
    condition : Boolean,
    trueConditionModifier : Modifier.() -> Modifier,
    falseConditionModifier: (Modifier.() -> Modifier)? = null
) : Modifier {
    return if (condition) {
        then(trueConditionModifier(Modifier))
    } else {
        if (falseConditionModifier != null) {
            then(falseConditionModifier(Modifier))
        } else {
            this
        }
    }
}