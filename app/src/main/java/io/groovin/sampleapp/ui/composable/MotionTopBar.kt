package io.groovin.sampleapp.ui.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import coil.compose.rememberAsyncImagePainter
import io.groovin.sampleapp.R


@OptIn(ExperimentalMotionApi::class, ExperimentalUnitApi::class)
@Composable
fun MotionTopBar(
    modifier: Modifier = Modifier,
    progress: Float
) {
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.motion_header_scene)
            .readBytes()
            .decodeToString()
    }
    MotionLayout(
        motionScene = MotionScene(motionScene),
        progress = progress,
        modifier = modifier.fillMaxWidth().then(
            Modifier.background(MaterialTheme.colors.primary)
        )
    ) {
        Image(
            painter = rememberAsyncImagePainter(
                model = "https://images.unsplash.com/photo-1528590005476-4f5a6f2bdd9e"
            ),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .layoutId("image"),
            contentScale = ContentScale.Crop,
            alpha = 1f - progress
        )
        val fontSize = TextUnit((32f - (8 * progress)), TextUnitType.Sp) //fontSize : 32(progress = 0) to 24(progress = 1)
        Text(
            text = "Header",
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .layoutId("title")
                .wrapContentHeight(),
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}