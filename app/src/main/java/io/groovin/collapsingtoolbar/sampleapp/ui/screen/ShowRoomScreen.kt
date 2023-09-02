package io.groovin.collapsingtoolbar.sampleapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chargemap.compose.numberpicker.NumberPicker
import io.groovin.collapsingtoolbar.sampleapp.data.LocalCommonData
import io.groovin.collapsingtoolbar.sampleapp.data.MenuItem
import io.groovin.collapsingtoolbar.sampleapp.ui.LocalNavAction
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.GroovinOkayCancelDialog
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.GroovinSwitch
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.Menu
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.GroovinTheme

@Composable
fun ShowRoomScreen() {
    val scrollState = rememberScrollState()
    GroovinTheme {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .navigationBarsPadding()
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            IntroHeader()
            SimpleShowRoomMenu()
            OptionShowRoomMenu()
        }
    }
}

@Composable
fun IntroHeader() {
    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            text = "Groovin\nCollapsing ToolBar ShowRoom",
            color = MaterialTheme.colors.onSecondary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
        )
    }
}

@Composable
fun SimpleShowRoomMenu() {
    val navAction = LocalNavAction.current
    var showDialog by remember { mutableStateOf(false) }
    Menu(MenuItem("Simple Version") { showDialog = true })
    if (showDialog) {
        SimpleShowRoomDialog(
            onCancelClick = {
                showDialog = false
            },
            onPositiveClick = {
                showDialog = false
                navAction.moveToSimpleShowRoom()
            }
        )
    }
}

@Composable
fun OptionShowRoomMenu() {
    val navAction = LocalNavAction.current
    var showDialog by remember { mutableStateOf(false) }
    Menu(MenuItem("Detail Option Version") { showDialog = true })
    if (showDialog) {
        OptionShowRoomDialog(
            onCancelClick = {
                showDialog = false
            },
            onPositiveClick = { isEnterAlwaysCollapsed, isAutoSnap, toolBarScrollable, requiredToolBarMaxHeight ->
                showDialog = false
                navAction.moveToShowRoomOptionScreen(
                    isEnterAlwaysCollapsed = isEnterAlwaysCollapsed,
                    isAutoSnap = isAutoSnap,
                    toolBarScrollable = toolBarScrollable,
                    requiredToolBarMaxHeight = requiredToolBarMaxHeight
                )
            }
        )
    }
}

@Composable
fun SimpleShowRoomDialog(
    onCancelClick: () -> Unit,
    onPositiveClick: () -> Unit
) {
    val commonData = LocalCommonData.current
    var listSize by remember { mutableStateOf(commonData.listSize) }
    GroovinOkayCancelDialog(
        onCancelClick = onCancelClick,
        onPositiveClick = onPositiveClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(end = 20.dp),
                text = "List Item Size"
            )
            NumberPicker(
                value = listSize,
                range = 0..1000,
                dividersColor = MaterialTheme.colors.primary,
                onValueChange = {
                    listSize = it
                    commonData.listSize = listSize
                }
            )
        }
    }
}


@Composable
fun OptionShowRoomDialog(
    onCancelClick: () -> Unit,
    onPositiveClick: (Boolean, Boolean, Boolean, Boolean) -> Unit
) {
    val commonData = LocalCommonData.current
    var listSize by remember { mutableStateOf(commonData.listSize) }
    var isEnterAlwaysCollapsed by remember { mutableStateOf(commonData.isEnterAlwaysCollapsed) }
    var isAutoSnap: Boolean by remember { mutableStateOf(commonData.isAutoSnap) }
    var toolBarScrollable: Boolean by remember { mutableStateOf(commonData.toolBarScrollable) }
    var requiredToolBarMaxHeight: Boolean by remember { mutableStateOf(commonData.requiredToolBarMaxHeight) }
    GroovinOkayCancelDialog(
        onCancelClick = onCancelClick,
        onPositiveClick = {
            onPositiveClick(isEnterAlwaysCollapsed, isAutoSnap, toolBarScrollable, requiredToolBarMaxHeight)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(text = "List Item Size")
            NumberPicker(
                value = listSize,
                range = 0..1000,
                dividersColor = MaterialTheme.colors.primary,
                onValueChange = {
                    listSize = it
                    commonData.listSize = listSize
                }
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "EnterAlwaysCollapsed")
                GroovinSwitch(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = isEnterAlwaysCollapsed,
                    onCheckedChange = {
                        isEnterAlwaysCollapsed = it
                        commonData.isEnterAlwaysCollapsed = it
                    }
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "isAutoSnap")
                GroovinSwitch(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = isAutoSnap,
                    onCheckedChange = {
                        isAutoSnap = it
                        commonData.isAutoSnap = it
                    }
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "toolBarScrollable")
                GroovinSwitch(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = toolBarScrollable,
                    onCheckedChange = {
                        toolBarScrollable = it
                        commonData.toolBarScrollable = it
                    }
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(text = "toolBarFillMaxHeight")
                GroovinSwitch(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    checked = requiredToolBarMaxHeight,
                    onCheckedChange = {
                        requiredToolBarMaxHeight = it
                        commonData.requiredToolBarMaxHeight = it
                    }
                )
            }
        }
    }
}