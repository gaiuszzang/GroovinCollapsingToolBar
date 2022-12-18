package io.groovin.sampleapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.groovin.collapsingtoolbar.AutoSnapOption
import io.groovin.collapsingtoolbar.CollapsingToolBarLayout
import io.groovin.collapsingtoolbar.rememberCollapsingToolBarState
import io.groovin.sampleapp.data.MenuItem
import io.groovin.sampleapp.ui.composable.Menu
import io.groovin.sampleapp.ui.composable.MotionTopBar
import io.groovin.sampleapp.ui.theme.GroovinTheme


@Composable
fun MotionLayoutSimpleScreen(contentList: List<MenuItem>) {
    val innerScrollState = rememberLazyListState()
    GroovinTheme {
        //Note : EnterAlwaysCollapsed, AutoSnap Option
        CollapsingToolBarLayout(
            state = rememberCollapsingToolBarState(200.dp, 56.dp),
            autoSnapOption = AutoSnapOption.AutoSnapWithScrollableState(innerScrollState),
            toolbar = { toolBarCollapsedInfo ->
                MotionTopBar(
                    modifier = Modifier
                        .height(toolBarCollapsedInfo.toolBarHeight),
                    progress = toolBarCollapsedInfo.progress
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = innerPadding,
                state = innerScrollState
            ) {
                items(contentList) { item ->
                    Menu(item)
                }
            }
        }
    }
}
