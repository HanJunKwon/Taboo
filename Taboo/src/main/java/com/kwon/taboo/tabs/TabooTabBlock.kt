package com.kwon.taboo.tabs

import java.util.UUID

data class TabooTabBlock (
    var tabName: String,
    var tabNumber: Int = 0
) {
    val uuid: String = UUID.randomUUID().toString()
}