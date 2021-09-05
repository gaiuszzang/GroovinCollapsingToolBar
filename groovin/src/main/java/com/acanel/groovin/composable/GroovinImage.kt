package com.acanel.groovin.composable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.rememberImagePainter

@Composable
fun GroovinUrlImage(url: String, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Crop, alpha: Float = 1f) {
    Image(
        painter = rememberImagePainter(
            data = url
        ),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale,
        alpha = alpha
    )
}

@Composable
fun GroovinUrlCrossFadeImage(url: String, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Crop) {
    val isFirstImage = remember { mutableStateOf(false) }
    val url1 = remember { mutableStateOf("") }
    val url2 = remember { mutableStateOf("") }
    if (url1.value != url && url2.value != url) {
        isFirstImage.value = !isFirstImage.value // Toggle
        if (isFirstImage.value) {
            url1.value = url
        } else {
            url2.value = url
        }
    }
    GroovinDoubleImage(url1.value, url2.value, modifier, contentScale, if (isFirstImage.value) 1f else 0f)
}

@Composable
private fun GroovinDoubleImage(url1: String, url2: String, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Crop, alpha: Float) {
    val animatedFloat = animateFloatAsState(
        targetValue = alpha,
        animationSpec = tween(
            durationMillis = 3000,
            easing = LinearEasing
        )
    )
    ConstraintLayout(
        modifier = modifier
    ) {
        val (firstImage, secondImage) = createRefs()
        GroovinUrlImage(
            url = url1,
            modifier = Modifier.constrainAs(firstImage) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.fillMaxSize(),
            contentScale = contentScale,
            alpha = animatedFloat.value
        )
        GroovinUrlImage(
            url = url2,
            modifier = Modifier.constrainAs(secondImage) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }.fillMaxSize(),
            contentScale = contentScale,
            alpha = 1f - animatedFloat.value
        )
    }
}