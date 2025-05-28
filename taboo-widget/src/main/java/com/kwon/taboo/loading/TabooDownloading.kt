package com.kwon.taboo.loading

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.kwon.taboo.R

class TabooDownloading(
    private val context: Context
) {
    private var dialog: AlertDialog? = null
    private var rootView : View? = null

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
        rootView?.findViewById<LottieAnimationView>(R.id.lottie_downloading)?.let{
            it.setAnimation(assetName)
            it.setRepeatCount(LottieDrawable.INFINITE)
            it.playAnimation()
        }
    }

    private fun updateDownloadingMessage() {
        rootView?.findViewById<TextView>(R.id.tv_downloading_message)?.text = message
    }


    fun show() {
        if (isShowing()) return

        rootView = LayoutInflater.from(context).inflate(R.layout.taboo_downloading, null)

        updateLottieDownloading()
        updateDownloadingMessage()

        dialog = AlertDialog
            .Builder(context)
            .setView(rootView)
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
        dialog?.setView(null)
    }

    fun isShowing() = dialog?.isShowing == true
}