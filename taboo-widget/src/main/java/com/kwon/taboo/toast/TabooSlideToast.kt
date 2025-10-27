package com.kwon.taboo.toast

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.kwon.taboo.R
import com.kwon.taboo.uicore.toast.presenter.SlideToastPresenter
import com.kwon.taboo.uicore.util.ResourceUtils

class TabooSlideToast(private val context: Context) {
    private val toastPresenter = SlideToastPresenter(context)

    private val viewWrapper = LinearLayout(context).apply {
        layoutParams = FrameLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        minimumHeight = ResourceUtils.dpToPx(context, 120f)
        clipToPadding = false
        setPadding(ResourceUtils.dpToPx(context, 10f))
    }
    private val view = LayoutInflater.from(context).inflate(R.layout.taboo_slide_toast_content, null, false)

    private var iconDrawable: Drawable? = null
    private var text: CharSequence = ""

    @SlideToastPresenter.ToastDuration
    private var duration: Int = SlideToastPresenter.ToastDuration.SHORT

    @SlideToastPresenter.ToastPosition
    private var position = SlideToastPresenter.ToastPosition.TOP

    fun makeText(
        text: CharSequence
    ) : TabooSlideToast {
        this.text = text

        return this
    }

    fun makeText(
        iconDrawable: Drawable?,
        text: CharSequence
    ) : TabooSlideToast {
        this.iconDrawable = iconDrawable
        this.text = text

        return this
    }

    fun makeText(
        iconDrawable: Drawable?,
        text: CharSequence,
        @SlideToastPresenter.ToastDuration duration: Int
    ) : TabooSlideToast {
        this.iconDrawable = iconDrawable
        this.text = text
        this.duration = duration

        return this
    }

    fun setPosition(@SlideToastPresenter.ToastPosition position: Int): TabooSlideToast {
        this.position = position

        return this
    }

    fun show() {
        view.findViewById<ImageView>(R.id.iv_toast_icon).apply {
            setImageDrawable(iconDrawable)
            visibility = if (iconDrawable == null) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        view.findViewById<TextView>(R.id.tv_toast_message).text = text
        viewWrapper.addView(view)

        toastPresenter.show(
            view = viewWrapper,
            position = position,
            duration = duration
        )
    }

    fun hide() {
        toastPresenter.hide()
    }
}