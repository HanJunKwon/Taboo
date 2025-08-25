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

class TabooLoading(private val context: Context) {
    private var dialog: AlertDialog? = null
    private var rootView: View? = null

    private var assetName: String? = null
    private var animationScale: Float = 1.0f
    private var loadingMessage: String? = null

    fun setAssetName(name: String): TabooLoading {
        this.assetName = name
        return this
    }

    fun getAssetName(): String? {
        return this.assetName
    }

    fun setAnimationScale(scale: Float): TabooLoading {
        this.animationScale = scale
        return this
    }

    fun getAnimationScale(): Float {
        return this.animationScale
    }

    fun setLoadingMessage(message: String): TabooLoading {
        this.loadingMessage = message
        return this
    }

    fun getLoadingMessage(): String? {
        return this.loadingMessage
    }

    private fun updateLottieDownloading() {
        rootView?.findViewById<LottieAnimationView>(R.id.lottie_downloading)?.let{
            it.setAnimation(assetName)
            it.scaleX = animationScale
            it.scaleY = animationScale
            it.setRepeatCount(LottieDrawable.INFINITE)
            it.playAnimation()
        }
    }

    private fun updateDownloadingMessage() {
        rootView?.findViewById<TextView>(R.id.tv_downloading_message)?.let {
            it.visibility = if (loadingMessage == null) View.GONE else View.VISIBLE
            it.text = loadingMessage ?: ""
        }
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