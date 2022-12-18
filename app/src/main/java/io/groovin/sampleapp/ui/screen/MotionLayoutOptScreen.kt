package io.groovin.sampleapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.groovin.collapsingtoolbar.AutoSnapOption
import io.groovin.collapsingtoolbar.CollapsingToolBarLayout
import io.groovin.collapsingtoolbar.CollapsingOption
import io.groovin.collapsingtoolbar.rememberCollapsingToolBarState
import io.groovin.sampleapp.data.MenuItem
import io.groovin.sampleapp.ui.composable.Menu
import io.groovin.sampleapp.ui.composable.FloatingButton
import io.groovin.sampleapp.ui.composable.MotionTopBar
import io.groovin.sampleapp.ui.theme.GroovinTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MotionLayoutOptScreen(contentList: List<MenuItem>, option: CollapsingOption, isToolBarBackground: Boolean) {
    var toolBarHeight by remember { mutableStateOf(Dp(0f)) }
    val innerScrollState = rememberLazyListState()
    val collapsingToolBarState = rememberCollapsingToolBarState(200.dp, 56.dp, option)
    val floatingButtonVisible by remember { derivedStateOf { innerScrollState.firstVisibleItemIndex > 2 } }
    GroovinTheme {
        CollapsingToolBarLayout(
            state = collapsingToolBarState,
            autoSnapOption = AutoSnapOption.AutoSnapWithScrollableState(innerScrollState),
            toolBarIsBackground = isToolBarBackground,
            toolbar = { toolBarCollapsedInfo ->
                toolBarHeight = toolBarCollapsedInfo.toolBarHeight
                MotionTopBar(
                    modifier = Modifier
                        .height(toolBarHeight),
                    progress = toolBarCollapsedInfo.progress
                )
            }
        ) { innerPadding ->
            val scope = rememberCoroutineScope()
            var isRefreshing by remember { mutableStateOf(false) }
            val refreshState = rememberPullRefreshState(isRefreshing, onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(1500)
                    isRefreshing = false
                }
            })
            Box(modifier = Modifier.fillMaxSize().pullRefresh(refreshState)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = innerPadding,
                    state = innerScrollState
                ) {
                    items(contentList) { item ->
                        Menu(item)
                    }
                }
                FloatingButton(
                    visible = floatingButtonVisible,
                    onClick = {
                        scope.launch {
                            innerScrollState.animateScrollWithToolBarToItem(0)
                        }
                    }
                )
                PullRefreshIndicator(
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = if (isToolBarBackground) 0.dp else toolBarHeight),
                    refreshing = isRefreshing,
                    state = refreshState
                )
            }
        }
    }
}

