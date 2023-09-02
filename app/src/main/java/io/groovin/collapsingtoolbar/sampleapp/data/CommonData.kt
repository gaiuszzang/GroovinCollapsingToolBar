package io.groovin.collapsingtoolbar.sampleapp.data

import androidx.compose.runtime.staticCompositionLocalOf

class CommonData {
    var listSize: Int = 30
    var isEnterAlwaysCollapsed: Boolean = false
    var isAutoSnap: Boolean = false
    var toolBarScrollable: Boolean = false
    var requiredToolBarMaxHeight: Boolean = false
    fun getShowRoomContentList(): List<MenuItem> {
        val list = mutableListOf<MenuItem>()
        for (i in 1..listSize) {
            list.add(MenuItem("Menu Item $i"))
        }
        return list
    }
}

// use for application lifecycle
val LocalCommonData = staticCompositionLocalOf { CommonData() }
