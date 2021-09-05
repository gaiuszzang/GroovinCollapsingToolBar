package com.acanel.groovin.extentions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun Context.findComponentActivity(): ComponentActivity? = when(this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findComponentActivity()
    else -> null
}

fun Activity.hideSystemUI(statusBar: Boolean = true, navigationBar: Boolean = true) {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).apply {
        var types = 0
        if (statusBar) types = types or WindowInsetsCompat.Type.statusBars()
        if (navigationBar) types = types or WindowInsetsCompat.Type.navigationBars()
        hide(types)
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Activity.showSystemUI(statusBar: Boolean = true, navigationBar: Boolean = true) {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, window.decorView).apply {
        var types = 0
        if (statusBar) types = types or WindowInsetsCompat.Type.statusBars()
        if (navigationBar) types = types or WindowInsetsCompat.Type.navigationBars()
        show(types)
    }
}

fun Activity.isStatusBarShown() : Boolean {
    return WindowInsetsCompat.toWindowInsetsCompat(window.decorView.rootWindowInsets).isVisible(WindowInsetsCompat.Type.statusBars())
}

fun Activity.isNavigationBarShown() : Boolean {
    return WindowInsetsCompat.toWindowInsetsCompat(window.decorView.rootWindowInsets).isVisible(WindowInsetsCompat.Type.navigationBars())
}

fun Int.toString(context: Context) : String = context.getString(this)