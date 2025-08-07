package com.kwon.taboo.uicore.util

import android.view.Window
import androidx.annotation.IntDef
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

object WindowUtil {
    /**
     * 현재 [ScreenMode]에 따라 [Window]의 화면 모드를 적용합니다.
     */
    fun applyWindowScreenMode(window: Window, @ScreenMode screenMode: Int) {
        when (screenMode) {
            NORMAL_SCREEN -> applyWindowNormalScreen(window)    // 일반 모드: 상태 표시줄, 네비게이션 바 표시
            FULL_SCREEN -> applyWindowFullScreen(window)        // 전체 화면 모드: 상태 표시줄, 네비게이션 바 숨김
        }
    }

    fun applyWindowNormalScreen(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, true)

        WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.systemBars())
    }

    fun applyWindowFullScreen(window: Window) {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    @IntDef(
        NORMAL_SCREEN,
        FULL_SCREEN
    )
    annotation class ScreenMode

    const val NORMAL_SCREEN = 0
    const val FULL_SCREEN = 1
}