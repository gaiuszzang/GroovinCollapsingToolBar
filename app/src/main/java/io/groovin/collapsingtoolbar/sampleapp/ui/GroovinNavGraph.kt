package io.groovin.collapsingtoolbar.sampleapp.ui

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.groovin.collapsingtoolbar.CollapsingOption
import io.groovin.collapsingtoolbar.sampleapp.ui.screen.MotionLayoutOptionScreen
import io.groovin.collapsingtoolbar.sampleapp.ui.screen.MotionLayoutSimpleScreen
import io.groovin.collapsingtoolbar.sampleapp.ui.screen.ShowRoomScreen


object GroovinDestination {
    const val INTRO = "intro"
    const val SHOWROOM_SIMPLE                           = "SHOWROOM_SIMPLE_CASE"
    const val SHOWROOM_ENTER_ALWAYS                     = "SHOWROOM_ENTER_ALWAYS_CASE"
    const val SHOWROOM_ENTER_ALWAYS_COLLAPSED           = "SHOWROOM_ENTER_ALWAYS_COLLAPSE_CASE"
    const val SHOWROOM_ENTER_ALWAYS_AUTO_SNAP           = "SHOWROOM_ENTER_ALWAYS_AUTO_SNAP_CASE"
    const val SHOWROOM_ENTER_ALWAYS_COLLAPSED_AUTO_SNAP = "SHOWROOM_ENTER_ALWAYS_COLLAPSE_AUTO_SNAP_CASE"
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
        composable(GroovinDestination.SHOWROOM_SIMPLE) {
            MotionLayoutSimpleScreen()
        }
        composable(GroovinDestination.SHOWROOM_ENTER_ALWAYS) {
            MotionLayoutOptionScreen(CollapsingOption.EnterAlways)
        }
        composable(GroovinDestination.SHOWROOM_ENTER_ALWAYS_COLLAPSED) {
            MotionLayoutOptionScreen(CollapsingOption.EnterAlwaysCollapsed)
        }
        composable(GroovinDestination.SHOWROOM_ENTER_ALWAYS_AUTO_SNAP) {
            MotionLayoutOptionScreen(CollapsingOption.EnterAlwaysAutoSnap)
        }
        composable(GroovinDestination.SHOWROOM_ENTER_ALWAYS_COLLAPSED_AUTO_SNAP) {
            MotionLayoutOptionScreen(CollapsingOption.EnterAlwaysCollapsedAutoSnap)
        }
    }
}

class GroovinAction(private val navController: NavHostController?) {
    val moveToSimpleShowRoom: () -> Unit = {
        navController?.navigate(GroovinDestination.SHOWROOM_SIMPLE)
    }
    val moveToEnterAlwaysCaseShowRoom: () -> Unit = {
        navController?.navigate(GroovinDestination.SHOWROOM_ENTER_ALWAYS)
    }
    val moveToEnterAlwaysCollapsedShowRoom: () -> Unit = {
        navController?.navigate(GroovinDestination.SHOWROOM_ENTER_ALWAYS_COLLAPSED)
    }
    val moveToEnterAlwaysAutoSnapShowRoom: () -> Unit = {
        navController?.navigate(GroovinDestination.SHOWROOM_ENTER_ALWAYS_AUTO_SNAP)
    }
    val moveToEnterAlwaysCollapsedAutoSnapShowRoom: () -> Unit = {
        navController?.navigate(GroovinDestination.SHOWROOM_ENTER_ALWAYS_COLLAPSED_AUTO_SNAP)
    }
    val moveToBack: () -> Unit = {
        navController?.popBackStack()
    }
}

