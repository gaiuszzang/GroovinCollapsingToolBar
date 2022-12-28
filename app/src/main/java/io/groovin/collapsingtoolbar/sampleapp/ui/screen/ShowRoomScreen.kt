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
import io.groovin.collapsingtoolbar.sampleapp.ui.composable.Menu
import io.groovin.collapsingtoolbar.sampleapp.ui.theme.GroovinTheme

@Composable
fun ShowRoomScreen() {
    val action = LocalNavAction.current
    val scrollState = rememberScrollState()
    GroovinTheme {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState)
        ) {
            IntroHeader()
            IntroMenu("Collapsing Layout\n - Simple Version", action = action.moveToSimpleShowRoom)
            IntroMenu("Collapsing Layout\n - enterAlways", action = action.moveToEnterAlwaysCaseShowRoom)
            IntroMenu("Collapsing Layout\n - enterAlwaysCollapsed", action = action.moveToEnterAlwaysCollapsedShowRoom)
            IntroMenu("Collapsing Layout\n - enterAlways + AutoSnap", action = action.moveToEnterAlwaysAutoSnapShowRoom)
            IntroMenu("Collapsing Layout\n - enterAlwaysCollapsed + AutoSnap", action = action.moveToEnterAlwaysCollapsedAutoSnapShowRoom)
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
fun IntroMenu(title: String, action: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    Menu(MenuItem(title) { showDialog = true })
    if (showDialog) {
        ListSizeDialog(
            onCancelClick = {
                showDialog = false
            },
            onPositiveClick = {
                showDialog = false
                action()
            }
        )
    }
}

@Composable
fun ListSizeDialog(
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