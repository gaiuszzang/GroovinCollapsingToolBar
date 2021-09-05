package com.acanel.groovinapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acanel.groovin.composable.GroovinBasicMenuCard
import com.acanel.groovin.composable.GroovinOkayCancelDialog
import com.acanel.groovin.extentions.toString
import com.acanel.groovinapp.R
import com.acanel.groovinapp.data.DialogInfo
import com.acanel.groovinapp.data.DialogShowRoomMenuVO
import com.acanel.groovinapp.data.IntroMenuVO
import com.acanel.groovinapp.ui.GroovinAction
import com.acanel.groovinapp.ui.GroovinDestination
import com.acanel.groovinapp.ui.LocalNavAction
import com.acanel.groovinapp.ui.theme.Black
import com.acanel.groovinapp.ui.theme.GroovinTheme


@Composable
fun DialogShowRoomScreen() {
    val context = LocalContext.current
    val defaultDialogContent = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Black)) {
            append("Hello, This is Dialog Contents.\n")
        }
        withStyle(style = SpanStyle(color = Color.Blue)) {
            append("Groovin")
        }
        withStyle(style = SpanStyle(color = Color.Black)) {
            append(" provides Dialog Example.\n\nThen see you later!")
        }
    }
    val menuList = remember { mutableStateListOf(
        DialogShowRoomMenuVO("OkayCancel Dialog", DialogInfo.OkayCancelDialog("OkayCancel Dialog", defaultDialogContent, )),
        DialogShowRoomMenuVO("OkayCancel Dialog without CancelButton", DialogInfo.OkayCancelDialog("OkayCancel Dialog 2", defaultDialogContent, showCancelButton = false))
    ) }
    GroovinTheme {
        DialogShowRoomScreenScreenIn(menuList)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DialogShowRoomScreenScreenIn(menuList: List<DialogShowRoomMenuVO>) {
    LazyColumn(
        Modifier.fillMaxSize()
    ) {
        //AppBar
        item { DialogShowRoomScreenAppBar() }

        //Contents
        items(items = menuList) { menu ->
            DialogShowRoomMenu(menu)
        }

    }
}

@Composable
fun DialogShowRoomScreenAppBar() {
    val navAction = LocalNavAction.current
    TopAppBar(
        elevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            onClick = {
                navAction.backAction()
            }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = "Dialog ShowRoom"
        )
    }
}

@Composable
fun DialogShowRoomMenu(menu: DialogShowRoomMenuVO) {
    val (isShowDialog, isShowDialogAction) = remember { mutableStateOf(false) }
    GroovinBasicMenuCard(
        onClick = { isShowDialogAction(true) }
    ) {
        Row(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = menu.menuTitle,
                color = Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
    
    if (isShowDialog) {
        when (menu.dialogInfo) {
            is DialogInfo.OkayCancelDialog -> {
                GroovinOkayCancelDialog(
                    title = menu.dialogInfo.title,
                    showOkayButton = menu.dialogInfo.showOkayButton,
                    showCancelButton = menu.dialogInfo.showCancelButton,
                    cancelable = menu.dialogInfo.showCancelButton,
                    onPositiveClick = { isShowDialogAction(false) },
                    onCancelClick = { isShowDialogAction(false) }
                ) {
                    Text(menu.dialogInfo.contents)
                }
            }
        }
    }
}

