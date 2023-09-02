package io.groovin.collapsingtoolbar.sampleapp.ui

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.groovin.collapsingtoolbar.CollapsingOption
import io.groovin.collapsingtoolbar.sampleapp.ui.screen.MotionLayoutOptionScreen
import io.groovin.collapsingtoolbar.sampleapp.ui.screen.MotionLayoutSimpleScreen
import io.groovin.collapsingtoolbar.sampleapp.ui.screen.ShowRoomScreen


object GroovinDestination {
    const val INTRO = "intro"
    const val SHOWROOM_SIMPLE = "SHOWROOM_SIMPLE_CASE"
    const val SHOWROOM_WITH_OPTION = "SHOWROOM_WITH_OPTION"
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
        composable(
            "${GroovinDestination.SHOWROOM_WITH_OPTION}?" +
                    "isEnterAlwaysCollapsed={isEnterAlwaysCollapsed}&" +
                    "isAutoSnap={isAutoSnap}&" +
                    "toolBarScrollable={toolBarScrollable}&" +
                    "requiredToolBarMaxHeight={requiredToolBarMaxHeight}",
            arguments = listOf(
                navArgument("isEnterAlwaysCollapsed") { defaultValue = false },
                navArgument("isAutoSnap") { defaultValue = false },
                navArgument("toolBarScrollable") { defaultValue = false },
                navArgument("requiredToolBarMaxHeight") { defaultValue = false },
            )
        ) {
            val isEnterAlwaysCollapsed = it.arguments?.getBoolean("isEnterAlwaysCollapsed", false) ?: false
            val isAutoSnap = it.arguments?.getBoolean("isAutoSnap", false) ?: false
            val toolBarScrollable = it.arguments?.getBoolean("toolBarScrollable", false) ?: false
            val requiredToolBarMaxHeight = it.arguments?.getBoolean("requiredToolBarMaxHeight", false) ?: false
            val collapsingOption = when (Pair(isEnterAlwaysCollapsed, isAutoSnap)) {
                false to false -> CollapsingOption.EnterAlways
                true to false -> CollapsingOption.EnterAlwaysCollapsed
                false to true -> CollapsingOption.EnterAlwaysAutoSnap
                true to true -> CollapsingOption.EnterAlwaysCollapsedAutoSnap
                else -> CollapsingOption.EnterAlways
            }
            MotionLayoutOptionScreen(
                option = collapsingOption,
                toolBarScrollable = toolBarScrollable,
                requiredToolBarMaxHeight = requiredToolBarMaxHeight

            )
        }
    }
}

class GroovinAction(private val navController: NavHostController?) {
    val moveToSimpleShowRoom: () -> Unit = {
        navController?.navigate(GroovinDestination.SHOWROOM_SIMPLE)
    }
    val moveToBack: () -> Unit = {
        navController?.popBackStack()
    }
    fun moveToShowRoomOptionScreen(
        isEnterAlwaysCollapsed: Boolean,
        isAutoSnap: Boolean,
        toolBarScrollable: Boolean,
        requiredToolBarMaxHeight: Boolean
    ) {
        navController?.navigate(
            "${GroovinDestination.SHOWROOM_WITH_OPTION}?" +
                    "isEnterAlwaysCollapsed=$isEnterAlwaysCollapsed&" +
                    "isAutoSnap=$isAutoSnap&" +
                    "toolBarScrollable=$toolBarScrollable&" +
                    "requiredToolBarMaxHeight=$requiredToolBarMaxHeight",
        )
    }
}

