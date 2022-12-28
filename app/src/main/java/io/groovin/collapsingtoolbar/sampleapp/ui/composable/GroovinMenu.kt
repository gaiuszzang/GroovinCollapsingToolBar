package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.collapsingtoolbar.sampleapp.data.MenuItem
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.Black

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Menu(menuItem: MenuItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.onSurface,
        contentColor = MaterialTheme.colors.surface,
        modifier = Modifier.fillMaxWidth().padding(6.dp),
        onClick = {
            menuItem.action?.invoke()
        }
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = menuItem.topBottomPadding),
            text = menuItem.title,
            color = Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

