package io.groovin.collapsingtoolbar

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.SplineBasedFloatDecayAnimationSpec
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.gestures.ScrollableState
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
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.util.fastSumBy
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Job


sealed class CollapsingOption(
    val collapsingWhenTop: Boolean,
    val isAutoSnap: Boolean
) {
    object EnterAlways: CollapsingOption(collapsingWhenTop = false, isAutoSnap = false)
    object EnterAlwaysCollapsed: CollapsingOption(collapsingWhenTop = true, isAutoSnap = false)
    object EnterAlwaysAutoSnap: CollapsingOption(collapsingWhenTop = false, isAutoSnap = true)
    object EnterAlwaysCollapsedAutoSnap: CollapsingOption(collapsingWhenTop = true, isAutoSnap = true)

    companion object {
        private val optionList by lazy { listOf(EnterAlways, EnterAlwaysCollapsed, EnterAlwaysAutoSnap, EnterAlwaysCollapsedAutoSnap) }
        fun toIndex(option: CollapsingOption): Int = optionList.indexOf(option)
        fun toOption(id: Int): CollapsingOption = optionList[id]
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
        val consumedOffset = state.onPreScroll(Offset(0f, -value))
        val consumed = scrollBy(value + consumedOffset.y)
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
        while (expectedCount > 0) {
            if (firstVisibleItemIndex == index && firstVisibleItemScrollOffset == scrollOffset) break
            val expectedDistance = expectedDistanceTo(index, scrollOffset)
            if (expectedDistance == 0) break
            val expectedFrameDistance = expectedDistance.toFloat() / expectedCount
            expectedCount -= 1
            scrollWithToolBarBy(expectedFrameDistance)
            delay(ANIMATE_SCROLL_DURATION)
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


@SuppressLint("AutoboxingStateCreation")
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
            }
            toolbarOffsetHeightPx = (toolbarOffsetHeightPx - diff).coerceIn(0f, toolbarHeightRangePx.toFloat())
            progress = if (toolbarHeightRangePx > 0) 1f - ((toolbarHeightRangePx - toolbarOffsetHeightPx) / toolbarHeightRangePx) else 0f
            prevValue = currentValue
        }
    }

    private fun consumeScrollHeight(availableY: Float): Float {
        val nextToolbarHeightPx = (toolbarOffsetHeightPx - availableY).coerceIn(0f, toolbarHeightRangePx.toFloat())
        val consumedY = toolbarOffsetHeightPx - nextToolbarHeightPx
        toolbarOffsetHeightPx = nextToolbarHeightPx
        progress = if (toolbarHeightRangePx > 0) 1f - ((toolbarHeightRangePx - toolbarOffsetHeightPx) / toolbarHeightRangePx) else 0f
        return consumedY
    }

    internal fun onPreScroll(available: Offset): Offset {
        val directionDown = available.y < 0
        return if (directionDown) {
            Offset(0f, if (toolbarOffsetHeightPx < toolbarHeightRangePx) consumeScrollHeight(available.y) else 0f)
        } else {
            if (collapsingOption.collapsingWhenTop) {
                Offset(0f, if (contentOffset <= 0f) consumeScrollHeight(available.y) else 0f)
            } else {
                Offset(0f, if (toolbarOffsetHeightPx > 0) consumeScrollHeight(available.y) else 0f)
            }
        }
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
        }
    }

    internal suspend fun flingDown(velocityY: Float) {
        contentOffset = 0f
        var prevValue = 0f
        animateDecay(0f, velocityY, SplineBasedFloatDecayAnimationSpec(density)) { value, _ ->
            val diff = value - prevValue
            prevValue = value
            val consumedOffset = onPreScroll(Offset(0f, diff))
            onPostScroll(consumedOffset, Offset.Zero)
        }
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


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollapsingToolBarLayout(
    modifier: Modifier = Modifier,
    state: CollapsingToolBarState,
    toolbar: @Composable (info: ToolBarCollapsedInfo) -> Unit,
    content: @Composable CollapsingToolBarLayoutContentScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val nestedScrollConnection = remember {
        object: NestedScrollConnection {
            private var snapAnimationJob: Job? = null
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                snapAnimationJob?.cancel()
                return state.onPreScroll(available)
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                state.onPostScroll(consumed, available)
                return super.onPostScroll(consumed, available, source)
            }


            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                snapAnimationJob = coroutineScope.launch {
                    if (available.y > 0f) {
                        state.flingDown(available.y)
                    }
                    if (state.collapsingOption.isAutoSnap) {
                        val centerPx = (state.toolBarMaxHeightPx + state.toolBarMinHeightPx) / 2
                        val toolBarHeightPx = state.toolBarHeightPx
                        state.snapToolBar(toolBarHeightPx >= centerPx)
                    }
                    snapAnimationJob = null
                }
                return if (available.y > 0f) Velocity(0f, available.y) else Velocity.Zero
            }
        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
            //.then(modifier)
    ) {
        //ToolBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(state.toolBarHeight)
                .zIndex(1f)
        ) {
            toolbar(ToolBarCollapsedInfo(state.progress, state.toolBarHeight))
        }
        //Content
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && state.collapsingOption.collapsingWhenTop) {
                //BugFix
                // Since Android 12(S), Overscroll effect consumes scroll event first.
                // So, Disable Overscroll effect in >= Android 12 version case.
                CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
                    CollapsingToolBarLayoutContentScope(state).content()
                }
            } else {
                CollapsingToolBarLayoutContentScope(state).content()
            }
        }
    }
}
