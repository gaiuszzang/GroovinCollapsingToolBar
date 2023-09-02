package io.groovin.collapsingtoolbar.sampleapp.ui.screen

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
import androidx.compose.ui.unit.dp
import io.groovin.collapsingtoolbar.CollapsingToolBarLayout
import io.groovin.collapsingtoolbar.CollapsingOption
import io.groovin.collapsingtoolbar.rememberCollapsingToolBarState
import io.groovin.collapsingtoolbar.sampleapp.data.LocalCommonData
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.Menu
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.FloatingButton
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.MotionTopBar
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.conditional
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.rememberStatusBarHeight
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.GroovinTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MotionLayoutOptionScreen(
    option: CollapsingOption,
    toolBarScrollable: Boolean = true,
    requiredToolBarMaxHeight: Boolean = false
) {
    val commonData = LocalCommonData.current
    val contentList by remember { mutableStateOf(commonData.getShowRoomContentList()) }
    val lazyListState = rememberLazyListState()
    val statusBarHeight by rememberStatusBarHeight()
    val collapsingToolBarState = rememberCollapsingToolBarState(220.dp, 56.dp, option)
    val floatingButtonVisible by remember { derivedStateOf { lazyListState.firstVisibleItemIndex > 2 } }
    val refreshEnable by remember { derivedStateOf { collapsingToolBarState.progress == 0f } }
    GroovinTheme {
        CollapsingToolBarLayout(
            modifier = Modifier.navigationBarsPadding(),
            state = collapsingToolBarState,
            updateToolBarHeightManually = true,
            toolbar = {
                MotionTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(collapsedInfo.toolBarHeight + statusBarHeight)
                        .conditional(
                            condition = requiredToolBarMaxHeight,
                            trueConditionModifier = {
                                requiredToolBarMaxHeight(collapsingToolBarState.toolBarMaxHeight + statusBarHeight)
                            }
                        )
                        .conditional(
                            condition = toolBarScrollable,
                            trueConditionModifier = {
                                toolBarScrollable()
                            }
                        ),
                    progress = collapsedInfo.progress
                )
            }
        ) {
            val scope = rememberCoroutineScope()
            var isRefreshing by remember { mutableStateOf(false) }
            val refreshState = rememberPullRefreshState(isRefreshing, onRefresh = {
                scope.launch {
                    isRefreshing = true
                    delay(1500)
                    isRefreshing = false
                }
            })
            Box(modifier = Modifier
                .fillMaxSize()
                .pullRefresh(refreshState, refreshEnable)) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState
                ) {
                    items(contentList) { item ->
                        Menu(item)
                    }
                }
                FloatingButton(
                    visible = floatingButtonVisible,
                    onClick = {
                        scope.launch {
                            lazyListState.animateScrollWithToolBarToItem(0)
                        }
                    }
                )
                PullRefreshIndicator(
                    modifier = Modifier.align(Alignment.TopCenter),
                    refreshing = isRefreshing,
                    state = refreshState
                )
            }
        }
    }
}

