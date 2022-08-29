package io.groovin.sampleapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.groovin.collapsingtoolbar.CollapsingToolBarLayout
import io.groovin.collapsingtoolbar.rememberCollapsingToolBarState
import io.groovin.sampleapp.data.MenuItem
import io.groovin.sampleapp.ui.composable.Menu
import io.groovin.sampleapp.ui.composable.MotionLayoutHeader
import io.groovin.sampleapp.ui.theme.GroovinTheme


@Composable
fun MotionLayoutSimpleScreen(contentList: List<MenuItem>) {
    val collapsingToolBarState = rememberCollapsingToolBarState(200.dp, 56.dp)
    GroovinTheme {
        CollapsingToolBarLayout(
            state = collapsingToolBarState,
            toolbar = {
                MotionLayoutHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(it.toolBarHeight),
                    progress = it.progress
                )
            }
        ) { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding
                ) {
                    items(contentList) { item ->
                        Menu(item)
                    }
                }
            }
        }
    }
}
