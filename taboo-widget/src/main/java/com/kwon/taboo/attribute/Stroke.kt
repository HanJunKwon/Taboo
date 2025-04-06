package com.kwon.taboo.attribute

import android.content.res.ColorStateList

data class Stroke(
    val width: Int = 0,
    val color: ColorStateList? = null,
    val dashWidth: Float = 0f,
    val dashGap: Float = 0f
)