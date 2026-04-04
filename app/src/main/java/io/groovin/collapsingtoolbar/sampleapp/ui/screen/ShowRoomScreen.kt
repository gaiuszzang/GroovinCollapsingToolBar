package io.groovin.collapsingtoolbar.sampleapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.groovin.collapsingtoolbar.sampleapp.data.LocalCommonData
import io.groovin.collapsingtoolbar.sampleapp.ui.LocalNavAction
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.GroovinSwitch
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.GroovinTheme

@Composable
fun ShowRoomScreen() {
    val scrollState = rememberScrollState()
    val navAction = LocalNavAction.current
    val commonData = LocalCommonData.current
    var listSize by remember { mutableIntStateOf(commonData.listSize) }
    var isEnterAlwaysCollapsed by remember { mutableStateOf(commonData.isEnterAlwaysCollapsed) }
    var isAutoSnap by remember { mutableStateOf(commonData.isAutoSnap) }
    var toolBarScrollable by remember { mutableStateOf(commonData.toolBarScrollable) }
    var requiredToolBarMaxHeight by remember { mutableStateOf(commonData.requiredToolBarMaxHeight) }
    var pullToRefresh: Boolean by remember { mutableStateOf(commonData.pullToRefresh) }

    GroovinTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
        ) {
            IntroHeader(modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 24.dp),
                text = "List Item Size: $listSize",
            )
            Slider(
                modifier = Modifier.padding(horizontal = 24.dp),
                value = listSize.toFloat(),
                onValueChange = {
                    val newSize = it.toInt()
                    listSize = newSize
                    commonData.listSize = newSize
                },
                valueRange = 0f..100f,
                steps = 0
            )
            Spacer(modifier = Modifier.height(16.dp))
            OptionSwitchRow(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = "EnterAlwaysCollapsed",
                checked = isEnterAlwaysCollapsed,
                onCheckedChange = {
                    isEnterAlwaysCollapsed = it
                    commonData.isEnterAlwaysCollapsed = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OptionSwitchRow(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = "isAutoSnap",
                checked = isAutoSnap,
                onCheckedChange = {
                    isAutoSnap = it
                    commonData.isAutoSnap = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OptionSwitchRow(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = "toolBarScrollable",
                checked = toolBarScrollable,
                onCheckedChange = {
                    toolBarScrollable = it
                    commonData.toolBarScrollable = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OptionSwitchRow(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = "toolBarFillMaxHeight",
                checked = requiredToolBarMaxHeight,
                onCheckedChange = {
                    requiredToolBarMaxHeight = it
                    commonData.requiredToolBarMaxHeight = it
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OptionSwitchRow(
                modifier = Modifier.padding(horizontal = 24.dp),
                label = "Pull To Refresh",
                checked = pullToRefresh,
                onCheckedChange = {
                    pullToRefresh = it
                    commonData.pullToRefresh = it
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    navAction.moveToShowRoomOptionScreen(
                        isEnterAlwaysCollapsed = isEnterAlwaysCollapsed,
                        isAutoSnap = isAutoSnap,
                        toolBarScrollable = toolBarScrollable,
                        requiredToolBarMaxHeight = requiredToolBarMaxHeight,
                        pullToRefresh = pullToRefresh
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Go to ShowRoom",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun IntroHeader(modifier: Modifier = Modifier) {
    Text(
        text = "Groovin\nCollapsing ToolBar\nShowRoom",
        color = Color.White,
        fontSize = 28.sp,
        lineHeight = 1.4.em,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.secondary)
            .systemBarsPadding()
            .fillMaxWidth()
            .padding(vertical = 32.dp)
            .then(modifier)
    )
}

@Composable
private fun OptionSwitchRow(
    modifier: Modifier = Modifier,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().then(modifier),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = label)
        GroovinSwitch(
            modifier = Modifier.align(Alignment.CenterEnd),
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}
