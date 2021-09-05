package com.acanel.groovinapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acanel.groovin.composable.GroovinBasicMenuCard
import com.acanel.groovin.extentions.toString
import com.acanel.groovinapp.R
import com.acanel.groovinapp.data.IntroMenuVO
import com.acanel.groovinapp.ui.GroovinAction
import com.acanel.groovinapp.ui.GroovinDestination
import com.acanel.groovinapp.ui.LocalNavAction
import com.acanel.groovinapp.ui.theme.Black
import com.acanel.groovinapp.ui.theme.GroovinTheme

@Composable
fun IntroScreen() {
    val context = LocalContext.current
    val action = LocalNavAction.current
    val menuList = remember { mutableStateListOf(
        IntroMenuVO(R.string.intro_menu_01.toString(context), action = action.dialogShowRoomAction),
        IntroMenuVO(R.string.intro_menu_02.toString(context), url = "groovin://${GroovinDestination.SETTING}"),
        IntroMenuVO("Test1"),
        IntroMenuVO("Test2"),
        IntroMenuVO("Test3", spanned = 2),
        IntroMenuVO("Test4"),
        IntroMenuVO("Test5"),
    ) }
    GroovinTheme {
        IntroScreenIn(R.string.intro_header.toString(context), 2, menuList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntroScreenIn(titleText: String, cellCount:Int, menuList: List<IntroMenuVO>) {
    Column(
        Modifier.fillMaxSize()
    ) {
        IntroAppBar()
        LazyVerticalGrid(
            cells = GridCells.Fixed(cellCount)
        ) {
            //Header
            item(span = { GridItemSpan(cellCount) }) {
                IntroHeader(titleText)
            }
            //Contents
            items(items = menuList, spans = { GridItemSpan(it.spanned) } ) { menu ->
                IntroMenu(menu)
            }
        }
    }
}

@Composable
fun IntroAppBar() {
    val navAction = LocalNavAction.current
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        Image(
            modifier = Modifier
                .width(160.dp)
                .height(74.dp)
                .padding(16.dp)
                .align(Alignment.CenterVertically),
            painter = painterResource(id = R.drawable.img_appbar_title),
            contentDescription = null)
        Text(
            modifier = Modifier.weight(1f),
            text = ""
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = {
                navAction.settingAction()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null
            )
        }
    }
}

@Composable
fun IntroHeader(text: String) {
    Box(modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 40.dp, bottom = 40.dp)) {
        Text(
            text = text,
            color = MaterialTheme.colors.onSecondary,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun IntroMenu(menu: IntroMenuVO) {
    val navAction = LocalNavAction.current
    GroovinBasicMenuCard(
        onClick = {
            if (menu.action != null) {
                menu.action.invoke()
            } else if (menu.url != null) {
                navAction.schemeAction(menu.url)
            }
        }
    ) {
        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = menu.title ?: "",
                color = Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview
@Composable
fun PreviewIntroTitle() {
    CompositionLocalProvider(LocalNavAction provides GroovinAction(null)) {
        GroovinTheme {
            IntroHeader("Get Recycle\nOur Old VINtage")
        }
    }
}

@Preview
@Composable
fun PreviewIntroMenu() {
    CompositionLocalProvider(LocalNavAction provides GroovinAction(null)) {
        GroovinTheme {
            IntroMenu(IntroMenuVO("Menu1", "url"))
        }
    }
}

@Preview
@Composable
fun PreviewIntroScreen() {
    CompositionLocalProvider(LocalNavAction provides GroovinAction(null)) {
        GroovinTheme {
            IntroScreenIn(
                titleText = "Get Recycle\nOur Old VINtage",
                cellCount = 2,
                menuList = listOf(
                    IntroMenuVO("Menu1", "url"),
                    IntroMenuVO("Menu2", "url"),
                    IntroMenuVO("Menu3", "url"),
                    IntroMenuVO("Menu4", "url"),
                    IntroMenuVO("Menu5", "url")
                )
            )
        }
    }

}
