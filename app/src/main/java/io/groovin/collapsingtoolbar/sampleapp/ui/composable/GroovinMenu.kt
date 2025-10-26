package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.collapsingtoolbar.sampleapp.data.MenuItem
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.Black

@Composable
fun Menu(menuItem: MenuItem) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.surface
        ),
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

