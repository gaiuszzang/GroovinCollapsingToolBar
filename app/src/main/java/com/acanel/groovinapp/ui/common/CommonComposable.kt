package com.acanel.groovinapp.ui.common

import android.content.Context
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.*
import com.acanel.groovin.extentions.findComponentActivity


@Composable
inline fun <reified VM: ViewModel> componentActivityViewModel(context: Context = LocalContext.current): VM {
    val vm: VM by context.findComponentActivity()!!.viewModels()
    return vm
}
