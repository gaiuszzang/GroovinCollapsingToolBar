package com.acanel.groovinapp.ui

import android.net.Uri
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acanel.groovinapp.ui.screen.DialogShowRoomScreen
import com.acanel.groovinapp.ui.screen.IntroScreen
import com.acanel.groovinapp.ui.screen.SettingScreen

object GroovinDestination {
    const val INTRO = "intro"
    const val DIALOG_SHOWROOM = "dialog_showroom"
    const val SETTING = "setting"
}
val LocalNavAction = compositionLocalOf<GroovinAction> { error("can't find GroovinAction") }

@Composable
fun GroovinNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = GroovinDestination.INTRO) {
        composable(GroovinDestination.INTRO) {
            IntroScreen()
        }
        composable(GroovinDestination.DIALOG_SHOWROOM) {
            DialogShowRoomScreen()
        }
        composable(GroovinDestination.SETTING) {
            SettingScreen()
        }
    }
}

class GroovinAction(private val navController: NavHostController?) {
    val introAction: () -> Unit = {
        navController?.navigate(GroovinDestination.INTRO)
    }
    val dialogShowRoomAction: () -> Unit = {
        navController?.navigate(GroovinDestination.DIALOG_SHOWROOM)
    }
    val settingAction: () -> Unit = {
        navController?.navigate(GroovinDestination.SETTING)
    }
    val schemeAction: (String) -> Unit = { url ->
        val uri: Uri = Uri.parse(url)
        if ("groovin" == uri.scheme) {
            navController?.navigate(uri.host!!)
        }
    }
    val backAction: () -> Unit = {
        navController?.popBackStack()
    }
}