package com.acanel.groovin.composable

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.acanel.groovin.extentions.*


@Composable
fun ScreenImmersive(content: @Composable () -> Unit) {
    content()
    val activity = LocalContext.current.findComponentActivity() ?: return
    val originalNavigationBarShown = rememberSaveable { mutableStateOf(activity.isNavigationBarShown()) }
    val originalStatusBarShown = rememberSaveable { mutableStateOf(activity.isStatusBarShown()) }

    DisposableEffect(Unit) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) activity.hideSystemUI()
        }
        activity.lifecycle.addObserver(lifecycleObserver)
        activity.hideSystemUI()
        onDispose {
            activity.showSystemUI(originalStatusBarShown.value, originalNavigationBarShown.value)
            activity.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}

@Composable
fun ScreenLandscape(content: @Composable () -> Unit) {
    val activity = LocalContext.current.findActivity() ?: return
    val currentOrientation = activity.requestedOrientation
    val originalOrientation = rememberSaveable { mutableStateOf(currentOrientation) }

    val needToChange = (currentOrientation != ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE)
    val needToDisplay = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE)

    if (needToChange) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
    }
    if (!needToChange || needToDisplay) {
        content()
        DisposableEffect(originalOrientation) {
            onDispose {
                activity.requestedOrientation = originalOrientation.value
            }
        }
    }
}

@SuppressLint("SourceLockedOrientationActivity")
@Composable
fun ScreenPortrait(content: @Composable () -> Unit) {
    val activity = LocalContext.current.findActivity() ?: return
    val currentOrientation = activity.requestedOrientation
    val originalOrientation = rememberSaveable { mutableStateOf(currentOrientation) }

    val needToChange = (currentOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    val needToDisplay = (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT)

    if (needToChange) {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
    if (!needToChange || needToDisplay) {
        content()
        DisposableEffect(originalOrientation) {
            onDispose {
                activity.requestedOrientation = originalOrientation.value
            }
        }
    }
}

@Composable
fun KeepScreenOn(content: @Composable () -> Unit) {
    content()
    val activity = LocalContext.current.findActivity() ?: return
    DisposableEffect(Unit) {
        activity.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}