package io.groovin.sampleapp.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.groovin.sampleapp.data.MenuItem
import io.groovin.sampleapp.ui.LocalNavAction
import io.groovin.sampleapp.ui.composable.Menu
import io.groovin.sampleapp.ui.theme.GroovinTheme

@Composable
fun ShowRoomScreen() {
    val action = LocalNavAction.current
    val menuList = remember { mutableStateListOf(
        MenuItem("Collapsing Layout\n - Simple Version", action = action.collapsingLayout1ShowRoomAction),
        MenuItem("Collapsing Layout\n - enterAlways", action = action.collapsingLayout2ShowRoomAction),
        MenuItem("Collapsing Layout\n - enterAlwaysCollapsed", action = action.collapsingLayout3ShowRoomAction),
        MenuItem("Collapsing Layout\n - enterAlwaysCollapsed|background", action = action.collapsingLayout4ShowRoomAction)
    )}
    GroovinTheme {
        Column(
            Modifier.fillMaxSize()
        ) {
            IntroHeader("Showroom")
            Column {
                menuList.forEach { menu ->
                    Menu(menu)
                }
            }
        }
    }
}

@Composable
fun IntroHeader(text: String) {
    Box(modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 40.dp, bottom = 40.dp)) {
        Text(
            text = text,
            color = MaterialTheme.colors.onSecondary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
