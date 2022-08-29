package io.groovin.sampleapp.ui

import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.groovin.collapsingtoolbar.CollapsingOption
import io.groovin.sampleapp.data.MenuItem
import io.groovin.sampleapp.ui.screen.*

object GroovinDestination {
    const val INTRO = "intro"
    const val COLLAPSING_LAYOUT_1_SHOWROOM = "collapsing_layout_1_showroom"
    const val COLLAPSING_LAYOUT_2_SHOWROOM = "collapsing_layout_2_showroom"
    const val COLLAPSING_LAYOUT_3_SHOWROOM = "collapsing_layout_3_showroom"
    const val COLLAPSING_LAYOUT_4_SHOWROOM = "collapsing_layout_4_showroom"
}
val LocalNavAction = compositionLocalOf<GroovinAction> { error("can't find GroovinAction") }

@Composable
fun GroovinNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = GroovinDestination.INTRO) {
        composable(GroovinDestination.INTRO) {
            ShowRoomScreen()
        }
        composable(GroovinDestination.COLLAPSING_LAYOUT_1_SHOWROOM) {
            MotionLayoutSimpleScreen(showRoomContentList)
        }
        composable(GroovinDestination.COLLAPSING_LAYOUT_2_SHOWROOM) {
            MotionLayoutOptScreen(showRoomContentList, CollapsingOption.EnterAlways, false)
        }
        composable(GroovinDestination.COLLAPSING_LAYOUT_3_SHOWROOM) {
            MotionLayoutOptScreen(showRoomContentList, CollapsingOption.EnterAlwaysCollapsed, false)
        }
        composable(GroovinDestination.COLLAPSING_LAYOUT_4_SHOWROOM) {
            MotionLayoutOptScreen(showRoomContentList, CollapsingOption.EnterAlwaysCollapsed, true)
        }
    }
}

class GroovinAction(private val navController: NavHostController?) {
    val collapsingLayout1ShowRoomAction: () -> Unit = {
        navController?.navigate(GroovinDestination.COLLAPSING_LAYOUT_1_SHOWROOM)
    }
    val collapsingLayout2ShowRoomAction: () -> Unit = {
        navController?.navigate(GroovinDestination.COLLAPSING_LAYOUT_2_SHOWROOM)
    }
    val collapsingLayout3ShowRoomAction: () -> Unit = {
        navController?.navigate(GroovinDestination.COLLAPSING_LAYOUT_3_SHOWROOM)
    }
    val collapsingLayout4ShowRoomAction: () -> Unit = {
        navController?.navigate(GroovinDestination.COLLAPSING_LAYOUT_4_SHOWROOM)
    }
}

private val showRoomContentList = listOf(
    MenuItem("Menu 1", 20.dp), MenuItem("Menu 2", 25.dp), MenuItem("Menu 3", 30.dp),
    MenuItem("Menu 4", 10.dp), MenuItem("Menu 5", 20.dp), MenuItem("Menu 6", 10.dp),
    MenuItem("Menu 7", 20.dp), MenuItem("Menu 8", 25.dp), MenuItem("Menu 9", 30.dp),
    MenuItem("Menu 10", 20.dp), MenuItem("Menu 11", 20.dp), MenuItem("Menu 12", 20.dp),
    MenuItem("Menu 13", 25.dp), MenuItem("Menu 14", 15.dp), MenuItem("Menu 15", 20.dp),
    MenuItem("Menu 16", 20.dp), MenuItem("Menu 17", 20.dp), MenuItem("Menu 18", 20.dp),
    MenuItem("Menu 19", 25.dp), MenuItem("Menu 20", 40.dp), MenuItem("Menu 21", 30.dp),
    MenuItem("Menu 22", 30.dp), MenuItem("Menu 23", 20.dp), MenuItem("Menu 24", 10.dp),
    MenuItem("Menu 25", 25.dp), MenuItem("Menu 26", 15.dp), MenuItem("Menu 27", 25.dp),
    MenuItem("Menu 28", 20.dp), MenuItem("Menu 29", 25.dp), MenuItem("Menu 30", 25.dp),
)
