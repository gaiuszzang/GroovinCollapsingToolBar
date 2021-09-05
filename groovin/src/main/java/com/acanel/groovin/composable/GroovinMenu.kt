package com.acanel.groovin.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun GroovinBasicMenuCard(onClick: () -> Unit = {}, content: @Composable () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.onSurface,
        contentColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth().padding(6.dp),
        onClick = onClick,
        content = content
    )
}