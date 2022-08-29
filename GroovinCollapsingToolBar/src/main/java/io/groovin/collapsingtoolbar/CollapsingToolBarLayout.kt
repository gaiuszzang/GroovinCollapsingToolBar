package io.groovin.collapsingtoolbar

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.util.fastSumBy
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope


sealed class CollapsingOption(
    val collapsingWhenTop: Boolean,
) {
    object EnterAlways: CollapsingOption(collapsingWhenTop = false)
    object EnterAlwaysCollapsed: CollapsingOption(collapsingWhenTop = true)

    companion object {
        private val optionList by lazy { listOf(EnterAlways, EnterAlwaysCollapsed) }
        fun toIndex(option: CollapsingOption): Int = optionList.indexOf(option)
        fun toOption(id: Int): CollapsingOption = optionList[id]
    }
}

sealed class AutoSnapOption(
    val isAutoSnap: Boolean = false
) {
    object NoAutoSnap: AutoSnapOption()
    class AutoSnapWithScrollableState(private val scrollableState: ScrollableState): AutoSnapOption(true) {
        override fun scrollTo(coroutineScope: CoroutineScope, offset: Int) {
            coroutineScope.launch {
                scrollableState.animateScrollBy(-offset.toFloat())
            }
        }
    }

    open fun scrollTo(coroutineScope: CoroutineScope, offset: Int) {
        //basically nothing to do
    }
}

@Stable
data class ToolBarCollapsedInfo(
    val progress: Float,
    val toolBarHeight: Dp
)

class CollapsingToolBarLayoutContentScope(
    private val state: CollapsingToolBarState
) {
    suspend fun ScrollableState.scrollWithToolBarBy(value: Float) {
        val consumed = scrollBy(value)
        state.onPostScroll(Offset(0f, -consumed), Offset(0f, -(value - consumed)))
    }

    suspend fun ScrollableState.animateScrollWithToolBarBy(value: Float, animationSpec: AnimationSpec<Float> = spring()) {
        val millisToNanos = 1_000_000L
        val duration = 8L //8ms
        val vectorConverter = Float.VectorConverter
        val vectorAnimationSpec = animationSpec.vectorize(vectorConverter)
        vectorConverter.convertToVector(0f)
        val durationNanos = vectorAnimationSpec.getDurationNanos(vectorConverter.convertToVector(0f), vectorConverter.convertToVector(value), vectorConverter.convertToVector(0f))
        var playTimeNanos = 0L
        var prevValue = 0f
        while (playTimeNanos < durationNanos) {
            val valueVector = vectorAnimationSpec.getValueFromNanos(playTimeNanos, vectorConverter.convertToVector(0f), vectorConverter.convertToVector(value), vectorConverter.convertToVector(0f))
            val newValue = vectorConverter.convertFromVector(valueVector)
            val currentOffset = newValue - prevValue
            scrollWithToolBarBy(currentOffset)
            delay(duration)
            playTimeNanos += duration * millisToNanos
            prevValue = newValue
        }
    }

    suspend fun LazyListState.animateScrollWithToolBarToItem(index: Int, scrollOffset: Int = 0) {
        var expectedCount = ANIMATE_SCROLL_TIME / ANIMATE_SCROLL_DURATION
        //Note : collapsingWhenTop = true 인 경우 && scroll 방향이 위 (currentIndex > targetIndex)인 경우에는 topBarOffset 이 변경되면 중단해야 한다.
        val beginTopBarOffset = state.toolBarMaxHeightPx - state.toolBarHeightPx
        val exitWhenTopBarOffsetChanged = state.collapsingOption.collapsingWhenTop && firstVisibleItemIndex > index
        while (expectedCount > 0) {
            val topBarOffset = state.toolBarMaxHeightPx - state.toolBarHeightPx
            if (firstVisibleItemIndex == index && firstVisibleItemScrollOffset == scrollOffset + topBarOffset) break
            if (exitWhenTopBarOffsetChanged && beginTopBarOffset != topBarOffset) break
            val expectedDistance = expectedDistanceTo(index, scrollOffset + topBarOffset)
            if (expectedDistance == 0) break
            val expectedFrameDistance = expectedDistance.toFloat() / expectedCount
            expectedCount -= 1
            scrollWithToolBarBy(expectedFrameDistance)
            delay(ANIMATE_SCROLL_DURATION)
        }
        // 후보정 (TODO 코드 정리 필요)
        val centerPx = (state.toolBarMaxHeightPx + state.toolBarMinHeightPx) / 2
        val toolBarHeightPx = state.toolBarHeightPx
        if (toolBarHeightPx < centerPx) {
            coroutineScope {
                launch {
                    scrollBy(-(state.toolBarMinHeightPx - toolBarHeightPx).toFloat())
                }
                launch {
                    state.updateToolBar(false)
                }
            }
        } else {
            coroutineScope {
                launch {
                    scrollBy(-(state.toolBarMaxHeightPx - toolBarHeightPx).toFloat())
                }
                launch {
                    state.updateToolBar(true)
                }
            }
        }
    }

    private fun LazyListState.expectedDistanceTo(index: Int, targetScrollOffset: Int): Int {
        val visibleItems = layoutInfo.visibleItemsInfo
        val averageSize = visibleItems.fastSumBy { it.size } / visibleItems.size
        val indexesDiff = index - firstVisibleItemIndex
        return (averageSize * indexesDiff) + targetScrollOffset - firstVisibleItemScrollOffset
    }

    companion object {
        private const val ANIMATE_SCROLL_DURATION   = 4L
        private const val ANIMATE_SCROLL_TIME       = 100L
    }
}

@Stable
class CollapsingToolBarState(
    private val density: Density,
    val toolBarMaxHeight: Dp,
    val toolBarMinHeight: Dp,
    val collapsingOption: CollapsingOption
) {
    var progress: Float by mutableStateOf(0f)
        internal set
    var contentOffset: Float by mutableStateOf(0f)
        internal set
    internal var toolbarOffsetHeightPx: Float by mutableStateOf(0f)

    internal val toolBarMaxHeightPx: Int = with(density) { toolBarMaxHeight.roundToPx() }
    internal val toolBarMinHeightPx: Int = with(density) { toolBarMinHeight.roundToPx() }
    val toolBarHeight by derivedStateOf {
        toolBarMaxHeight - ((toolBarMaxHeight - toolBarMinHeight) * progress)
    }
    internal val toolBarHeightPx by derivedStateOf {
        with(density) { toolBarHeight.roundToPx() }
    }
    private val toolbarHeightRangePx by derivedStateOf { toolBarMaxHeightPx - toolBarMinHeightPx }

    suspend fun snapToolBar(isExpand: Boolean) {
        val beginValue = toolBarHeightPx.toFloat()
        val finishValue = if (isExpand) toolBarMaxHeightPx.toFloat() else toolBarMinHeightPx.toFloat()
        var prevValue = beginValue
        animate(beginValue, finishValue, animationSpec = spring()) { currentValue, _ ->
            val diff = -(prevValue - currentValue)
            if (collapsingOption.collapsingWhenTop) {
                contentOffset -= diff
                if (contentOffset < 0f) {
                    contentOffset = 0f
                }
                toolbarOffsetHeightPx = (contentOffset).coerceIn(0f, toolbarHeightRangePx.toFloat())
            } else {
                val newOffset = toolbarOffsetHeightPx - diff
                toolbarOffsetHeightPx = (newOffset).coerceIn(0f, toolbarHeightRangePx.toFloat())
            }
            progress = if (toolbarHeightRangePx > 0) 1f - ((toolbarHeightRangePx - toolbarOffsetHeightPx) / toolbarHeightRangePx) else 0f
            prevValue = currentValue
        }
    }

    fun updateToolBar(isExpand: Boolean) {
        val beginValue = toolBarHeightPx.toFloat()
        val finishValue = if (isExpand) toolBarMaxHeightPx.toFloat() else toolBarMinHeightPx.toFloat()
        val diff = -(beginValue - finishValue)
        if (collapsingOption.collapsingWhenTop) {
            contentOffset -= diff
            if (contentOffset < 0f) {
                contentOffset = 0f
            }
            toolbarOffsetHeightPx = (contentOffset).coerceIn(0f, toolbarHeightRangePx.toFloat())
        } else {
            val newOffset = toolbarOffsetHeightPx - diff
            toolbarOffsetHeightPx = (newOffset).coerceIn(0f, toolbarHeightRangePx.toFloat())
        }
        progress = if (toolbarHeightRangePx > 0) 1f - ((toolbarHeightRangePx - toolbarOffsetHeightPx) / toolbarHeightRangePx) else 0f
    }

    internal fun onPostScroll(consumed: Offset, available: Offset) {
        if (collapsingOption.collapsingWhenTop) {
            contentOffset -= consumed.y
            // Correction Logic. same as Google code.
            // Reset the total content offset to zero when scrolling all the way down. This
            // will eliminate some float precision inaccuracies.
            if (consumed.y == 0f && available.y > 0f || contentOffset < 0f) {
                contentOffset = 0f
            }
            toolbarOffsetHeightPx = (contentOffset).coerceIn(0f, toolbarHeightRangePx.toFloat())
        } else {
            val newOffset = toolbarOffsetHeightPx - consumed.y
            toolbarOffsetHeightPx = (newOffset).coerceIn(0f, toolbarHeightRangePx.toFloat())
        }
        progress = if (toolbarHeightRangePx > 0) 1f - ((toolbarHeightRangePx - toolbarOffsetHeightPx) / toolbarHeightRangePx) else 0f
    }


    companion object {
        val Saver: Saver<CollapsingToolBarState, *> = listSaver(
            save = {
                listOf(
                    it.density.density,
                    it.density.fontScale,
                    it.toolBarMaxHeight.value,
                    it.toolBarMinHeight.value,
                    CollapsingOption.toIndex(it.collapsingOption),
                    it.progress,
                    it.contentOffset,
                    it.toolbarOffsetHeightPx
                )
            },
            restore = {
                CollapsingToolBarState(
                    Density(it[0] as Float, it[1] as Float),
                    Dp(it[2] as Float),
                    Dp(it[3] as Float),
                    CollapsingOption.toOption(it[4] as Int)
                ).apply {
                    progress = it[5] as Float
                    contentOffset = it[6] as Float
                    toolbarOffsetHeightPx = it[7] as Float
                }
            }
        )
    }
}

@Composable
fun rememberCollapsingToolBarState(
    toolBarMaxHeight: Dp = 56.dp,
    toolBarMinHeight: Dp = 0.dp,
    collapsingOption: CollapsingOption = CollapsingOption.EnterAlwaysCollapsed
): CollapsingToolBarState {
    val density = LocalDensity.current
    return rememberSaveable(saver = CollapsingToolBarState.Saver) {
        CollapsingToolBarState(density, toolBarMaxHeight, toolBarMinHeight, collapsingOption)
    }
}

@Composable
fun CollapsingToolBarLayout(
    modifier: Modifier = Modifier,
    state: CollapsingToolBarState,
    autoSnapOption: AutoSnapOption = AutoSnapOption.NoAutoSnap,
    toolBarIsBackground: Boolean = false,
    toolbar: @Composable (info: ToolBarCollapsedInfo) -> Unit,
    content: @Composable CollapsingToolBarLayoutContentScope.(contentPadding: PaddingValues) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            private var snapAnimationJob: Job? = null
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                snapAnimationJob?.cancel()
                return super.onPreScroll(available, source)
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                state.onPostScroll(consumed, available)
                return super.onPostScroll(consumed, available, source)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (autoSnapOption.isAutoSnap) {
                    val centerPx = (state.toolBarMaxHeightPx + state.toolBarMinHeightPx) / 2
                    val toolBarHeightPx = state.toolBarHeightPx
                    if (toolBarHeightPx < centerPx) {
                        autoSnapOption.scrollTo(coroutineScope, state.toolBarMinHeightPx - toolBarHeightPx)
                        snapAnimationJob = coroutineScope.launch {
                            state.snapToolBar(false)
                            snapAnimationJob = null
                        }
                    } else {
                        autoSnapOption.scrollTo(coroutineScope, state.toolBarMaxHeightPx - toolBarHeightPx)
                        snapAnimationJob = coroutineScope.launch {
                            state.snapToolBar(true)
                            snapAnimationJob = null
                        }
                    }
                }
                return super.onPostFling(consumed, available)
            }
        }
    }
    Box(
        modifier = Modifier
            .nestedScroll(nestedScrollConnection)
            .then(modifier)
    ) {
        //Modifier configuration
        val toolBarModifier = Modifier
            .offset {
                IntOffset(x = 0, y = 0)
            }

        //ToolBar (background case)
        if (toolBarIsBackground) {
            Box(modifier = toolBarModifier) {
                toolbar(ToolBarCollapsedInfo(state.progress, state.toolBarHeight))
            }
        }
        //Content
        CollapsingToolBarLayoutContentScope(state).content(PaddingValues(top = state.toolBarMaxHeight))

        //ToolBar (foreground case)
        if (!toolBarIsBackground) {
            Box(modifier = toolBarModifier) {
                toolbar(ToolBarCollapsedInfo(state.progress, state.toolBarHeight))
            }
        }
    }
}

