package com.kwon.taboo.loading

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import com.airbnb.lottie.LottieDrawable
import com.kwon.taboo.databinding.TabooDownloadingBinding
import com.kwon.taboo.enums.LoadingType

class TabooLoading(
    private val context: Context
) {
    private var dialog: AlertDialog? = null
    private lateinit var binding: TabooDownloadingBinding

    private var loadingType = LoadingType.LOADING

    private var assetName: String = ""
    private var message: String = ""

    fun setLoadingType(loadingType: LoadingType) {
        this.loadingType = loadingType
    }

    fun setAssetName(assetName: String) {
        this.assetName = assetName
    }

    fun setMessage(message: String) {
        this.message = message
    }

    private fun updateLottieDownloading() {
        binding.lottieDownloading.apply {
            setAnimation(
                assetName.ifBlank {
                    getDefaultAssetName()
                }
            )
            setRepeatCount(LottieDrawable.INFINITE)
            playAnimation()
        }
    }

    /**
     * [assetName]이 빈 값인 경우, [loadingType]에 따라 기본 애셋 이름을 반환.
     */
    private fun getDefaultAssetName() = when (loadingType) {
        LoadingType.LOADING -> "animation_loading.json"
        LoadingType.DOWNLOADING -> "animation_downloading.json"
    }

    private fun updateDownloadingMessage() {
        binding.tvDownloadingMessage.text = message
    }

    fun show() {
        if (isShowing()) {
            Log.e("TabooDownloading", "Already showing")
            return
        }

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

    fun dismiss() {
        dialog?.dismiss()
    }

    fun isShowing() = dialog?.isShowing == true
}