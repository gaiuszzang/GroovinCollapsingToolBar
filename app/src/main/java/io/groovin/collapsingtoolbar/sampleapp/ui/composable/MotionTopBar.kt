package io.groovin.collapsingtoolbar.sampleapp.ui.composable

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import io.groovin.collapsingtoolbar.sampleapp.ui.LocalNavAction
import io.groovin.sampleapp.R

private const val statusBarId = "motionStatusBar"
private const val imageId = "motionTopBarImage"
private const val titleId = "motionTopBarTitle"
private const val backIconId = "motionTopBarBackIcon"

@OptIn(ExperimentalMotionApi::class)
@Composable
fun MotionTopBar(
    modifier: Modifier = Modifier,
    progress: Float
) {
    val navAction = LocalNavAction.current
    MotionLayout(
        start = startConstraintSet(),
        end = endConstraintSet(),
        progress = progress,
        modifier = modifier
            .fillMaxSize()
            .then(
                Modifier.background(MaterialTheme.colors.primary)
            )
            //.statusBarsPadding()
    ) {
        Image(
            painter = painterResource(id = R.drawable.top_bar_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .layoutId(imageId),
            contentScale = ContentScale.Crop,
            alpha = 1f - progress
        )
        val fontSize = TextUnit((42f - (18 * progress)), TextUnitType.Sp)
        Box(modifier = Modifier
            .layoutId(statusBarId)
            .fillMaxWidth()
            .statusBarsPadding()
        )
        Text(
            text = "ShowRoom List",
            color = MaterialTheme.colors.onSecondary,
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
                tint = MaterialTheme.colors.onPrimary
            )
        }
    }
}

@SuppressLint("Range")
private fun startConstraintSet() = ConstraintSet {
    val statusBar = createRefFor(statusBarId)
    val image = createRefFor(imageId)
    val title = createRefFor(titleId)
    val icon = createRefFor(backIconId)

    constrain(image) {
        top.linkTo(parent.top, 0.dp)
        bottom.linkTo(parent.bottom, 0.dp)
        start.linkTo(parent.start, 0.dp)
        end.linkTo(parent.end, 0.dp)
    }

    constrain(title) {
        top.linkTo(statusBar.bottom, 0.dp)
        bottom.linkTo(parent.bottom, 0.dp)
        start.linkTo(parent.start, 0.dp)
        end.linkTo(parent.end, 0.dp)
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
    val image = createRefFor(imageId)
    val title = createRefFor(titleId)
    val backIcon = createRefFor(backIconId)

    constrain(image) {
        top.linkTo(parent.top, 0.dp)
        bottom.linkTo(parent.bottom, 0.dp)
        start.linkTo(parent.start, 0.dp)
        end.linkTo(parent.end, 0.dp)
    }

    constrain(title) {
        top.linkTo(statusBar.bottom, 0.dp)
        bottom.linkTo(parent.bottom, 0.dp)
        start.linkTo(backIcon.end, 0.dp)
        alpha = 0.5f
    }

    constrain(backIcon) {
        top.linkTo(statusBar.bottom, 0.dp)
        start.linkTo(parent.start, 0.dp)
    }
}
