package com.kwon.taboo.tabs

import androidx.annotation.DrawableRes
import java.util.UUID

data class TabooTabBlock (
    var tabName: String,
    var tabNumber: Int = 0,
    @DrawableRes var tabIcon: Int = 0
) {
    val uuid: String = UUID.randomUUID().toString()
}