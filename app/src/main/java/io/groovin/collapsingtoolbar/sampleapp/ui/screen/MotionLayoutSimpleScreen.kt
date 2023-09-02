package io.groovin.collapsingtoolbar.sampleapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.groovin.collapsingtoolbar.CollapsingToolBarLayout
import io.groovin.collapsingtoolbar.rememberCollapsingToolBarState
import io.groovin.collapsingtoolbar.sampleapp.data.LocalCommonData
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.Menu
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.MotionTopBar
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.rememberStatusBarHeight
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.GroovinTheme


@Composable
fun MotionLayoutSimpleScreen() {
    val commonData = LocalCommonData.current
    val contentList by remember { mutableStateOf(commonData.getShowRoomContentList()) }
    val statusBarHeight by rememberStatusBarHeight()
    val innerScrollState = rememberLazyListState()
    GroovinTheme {
        CollapsingToolBarLayout(
            modifier = Modifier.navigationBarsPadding(),
            state = rememberCollapsingToolBarState(200.dp, 56.dp),
            updateToolBarHeightManually = true,
            toolbar = {
                MotionTopBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(collapsedInfo.toolBarHeight + statusBarHeight)
                        .toolBarScrollable(),
                    progress = collapsedInfo.progress
                )
            }
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = innerScrollState
            ) {
                items(contentList) { item ->
                    Menu(item)
                }
            }
        }
    }
}
