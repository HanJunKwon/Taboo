package com.kwon.taboo.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooPreviewButtonBinding

class TabooPreviewButton(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = TabooPreviewButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var text = "Preview Button"
    private var description = "Preview Button Description"
    private var preview = "Preview"
    private var iconResource: Drawable? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooPreviewButton)
        val isEnabled = typed.getBoolean(R.styleable.TabooPreviewButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooPreviewButton_android_text) ?: "Preview Button"
        val description = typed.getString(R.styleable.TabooPreviewButton_description) ?: "Preview Button Description"
        val preview = typed.getString(R.styleable.TabooPreviewButton_preview) ?: "Preview"
        val iconResourceId = typed.getResourceId(R.styleable.TabooPreviewButton_icon, R.drawable.ic_default_icon)

        typed.recycle()

        setEnabled(isEnabled)
        setText(text)
        setDescription(description)
        setPreview(preview)
        setIconResourceId(iconResourceId)

        binding.root.background = ContextCompat.getDrawable(context, R.drawable.taboo_button_ripple_effect)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateEnabled()
    }

    private fun updateEnabled() {
        binding.root.alpha = if (isEnabled) 1.0f else 0.3f
    }

    fun setText(text: String) {
        this.text = text
        updateText()
    }

    private fun updateText() {
        binding.tvButtonName.text = text
    }

    fun setDescription(description: String) {
        this.description = description
        updateDescription()
    }

    private fun updateDescription() {
        binding.tvButtonDescription.text = description
    }

    fun setPreview(preview: String) {
        this.preview = preview
        updatePreview()
    }

    private fun updatePreview() {
        binding.tvButtonPreview.text = preview
    }

    fun setIconResourceId(resourceId: Int) {
        this.iconResource = ContextCompat.getDrawable(context, resourceId)
        updateIconResource()
    }

    fun setIconResource(resource: Drawable?) {
        this.iconResource = resource
        updateIconResource()
    }

    private fun updateIconResource() {
        binding.ivButtonIcon.setImageDrawable(iconResource)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
    }

//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.action) {
//            MotionEvent.ACTION_DOWN -> isPressed = true
//            MotionEvent.ACTION_UP -> performClick()
//            MotionEvent.ACTION_CANCEL -> isPressed = false
//        }
//
//        return super.onTouchEvent(event)
//    }
//
//    override fun performClick(): Boolean {
//        return super.performClick()
//    }
}