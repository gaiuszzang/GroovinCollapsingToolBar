package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import io.groovin.collapsingtoolbar.sampleapp.ui.LocalNavAction

private const val statusBarId = "motionStatusBar"
private const val titleId = "motionTopBarTitle"
private const val backIconId = "motionTopBarBackIcon"

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionTopBar(
    modifier: Modifier = Modifier,
    progress: Float
) {
    val navAction = LocalNavAction.current
    val expandedColor = MaterialTheme.colorScheme.secondary
    val collapsedColor = MaterialTheme.colorScheme.primary
    val backgroundColor = lerp(expandedColor, collapsedColor, progress)
    MotionLayout(
        start = startConstraintSet(),
        end = endConstraintSet(),
        progress = progress,
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        val fontSize = TextUnit((42f - (18 * progress)), TextUnitType.Sp)
        Box(modifier = Modifier
            .layoutId(statusBarId)
            .fillMaxWidth()
            .statusBarsPadding()
        )
        Text(
            text = "ShowRoom List",
            color = Color.White,
            modifier = Modifier
                .layoutId(titleId)
                .wrapContentHeight(),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        IconButton(
            modifier = Modifier
                .layoutId(backIconId)
                .size(56.dp),
            onClick = {
                navAction.moveToBack()
            }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@SuppressLint("Range")
private fun startConstraintSet() = ConstraintSet {
    val statusBar = createRefFor(statusBarId)
    val title = createRefFor(titleId)
    val icon = createRefFor(backIconId)

    constrain(title) {
        bottom.linkTo(parent.bottom, 16.dp)
        start.linkTo(parent.start, 16.dp)
        alpha = 1f
    }

    constrain(icon) {
        top.linkTo(statusBar.bottom, 0.dp)
        start.linkTo(parent.start, 0.dp)
    }
}

@SuppressLint("Range")
private fun endConstraintSet() = ConstraintSet {
    val statusBar = createRefFor(statusBarId)
    val title = createRefFor(titleId)
    val backIcon = createRefFor(backIconId)

    constrain(title) {
        top.linkTo(statusBar.bottom, 0.dp)
        bottom.linkTo(parent.bottom, 0.dp)
        start.linkTo(backIcon.end, 0.dp)
        alpha = 1f
    }

    constrain(backIcon) {
        top.linkTo(statusBar.bottom, 0.dp)
        start.linkTo(parent.start, 0.dp)
    }
}
