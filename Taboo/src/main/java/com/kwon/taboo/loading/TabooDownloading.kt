package com.kwon.taboo.loading

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.airbnb.lottie.LottieDrawable
import com.kwon.taboo.databinding.TabooDownloadingBinding

class TabooDownloading(
    private val context: Context
) {
    private var dialog: AlertDialog? = null
    private lateinit var binding: TabooDownloadingBinding

    private var assetName: String = "animation_downloading.json"
    private var lottieScaleXY: Float = 1.0f
    private var message: String = ""

    fun setAssetName(assetName: String) {
        this.assetName = assetName
    }

    fun setLottieScaleXY(lottieScaleXY: Float) {
        this.lottieScaleXY = lottieScaleXY
    }

    fun setMessage(message: String) {
        this.message = message
    }

    private fun updateLottieDownloading() {
        binding.lottieDownloading.apply {
            setAnimation(assetName)
            setRepeatCount(LottieDrawable.INFINITE)
            playAnimation()
        }
    }

    private fun updateDownloadingMessage() {
        binding.tvDownloadingMessage.text = message
    }

    fun show() {
        if (isShowing()) return

        binding = TabooDownloadingBinding.inflate(LayoutInflater.from(context))
        updateLottieDownloading()
        updateDownloadingMessage()

        dialog = AlertDialog
            .Builder(context)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        dialog?.window?.let { window ->
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window.requestFeature(Window.FEATURE_NO_TITLE)
        }

        dialog?.show()

    }

    fun hide() {
        dialog?.dismiss()
    }

    fun isShowing() = dialog?.isShowing == true
}