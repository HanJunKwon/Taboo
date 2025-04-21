package com.kwon.taboo.uicore.navigation.model

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

data class TabooMenuItem(
    val id: Int,
    val title: String,
    val titleTextColor: ColorStateList?,
    @DrawableRes val iconResId: Int,
    var iconTint: ColorStateList?
)