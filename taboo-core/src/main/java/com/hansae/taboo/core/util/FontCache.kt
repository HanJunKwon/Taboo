package com.hansae.taboo.core.util

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

/**
 * Typeface 및 Font 관련된 캐시를 관리하는 객체입니다.
 */
object FontCache {
    private val fontCache = mutableMapOf<Int, Typeface>()

    fun getFont(context: Context, @FontRes fontResId: Int): Typeface? {
        return fontCache.getOrPut(fontResId) {
            ResourcesCompat.getFont(context, fontResId) ?: Typeface.DEFAULT
        }
    }
}