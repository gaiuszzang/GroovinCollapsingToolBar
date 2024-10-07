package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import androidx.compose.ui.Modifier

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
