package com.kwon.taboo.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooMenuButtonBinding

class TabooMenuButton(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs) {
    private val binding = TabooMenuButtonBinding.inflate(LayoutInflater.from(context), this, true)

    private var text = "Preview Button"
    private var description = "Preview Button Description"

    private var type = TYPE_NONE

    private var preview = "Preview"
    private var previewGravity: Int = PREVIEW_GRAVITY_TOP
    private var iconResource: Drawable? = null

    private var inflatedView: View? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooMenuButton)
        val isEnabled = typed.getBoolean(R.styleable.TabooMenuButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooMenuButton_android_text) ?: "Preview Button"
        val description = typed.getString(R.styleable.TabooMenuButton_description) ?: "Preview Button Description"
        val type = typed.getInt(R.styleable.TabooMenuButton_menuType, TYPE_NONE)
        val preview = typed.getString(R.styleable.TabooMenuButton_preview) ?: "Preview"
        val previewGravity = typed.getInt(R.styleable.TabooMenuButton_previewGravity, PREVIEW_GRAVITY_TOP)
        val iconResourceId = typed.getResourceId(R.styleable.TabooMenuButton_icon, R.drawable.ic_box_44dp)

        typed.recycle()

        setEnabled(isEnabled)
        setText(text)
        setDescription(description)
        setType(type)
        setPreview(preview)
        setPreviewGravity(previewGravity)
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

    private fun setType(type: Int) {
        this.type = type

        updateType()
    }

    private fun updateType() {
        val view = when (type) {
            TYPE_PREVIEW -> {
                R.layout.taboo_menu_button_preview_fragment
            }
            TYPE_TOGGLE -> {
                R.layout.taboo_menu_button_toggle_fragment
            }
            else -> return
        }

        inflatedView = binding.vsFragment.viewStub?.let { viewStub ->
            viewStub.layoutResource = view
            viewStub.inflate()
        }
    }

    fun setPreview(preview: String) {
        this.preview = preview
        updatePreview()
    }

    private fun updatePreview() {
        if (type != TYPE_PREVIEW) return
        binding.vsFragment.viewStub?.inflatedId?.let {
            val inflatedView = findViewById<ConstraintLayout>(it)
            inflatedView.findViewById<TextView>(R.id.tv_button_preview).text = preview
        }
    }

    fun getPreviewGravity() = previewGravity

    fun setPreviewGravity(previewGravity: Int) {
        this.previewGravity = previewGravity
        updatePreviewGravity()
    }

    private fun updatePreviewGravity() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.wrapper)

        val buttonInfoId = binding.clButtonInformationContainer.id
        val viewStubId = inflatedView?.id ?: return
        val parentId = binding.wrapper.id

        when (previewGravity) {
            PREVIEW_GRAVITY_TOP -> {
                constraintSet.connect(viewStubId, TOP, parentId, TOP)
                constraintSet.connect(viewStubId, END, parentId, END)
                constraintSet.connect(viewStubId, BOTTOM, -1, BOTTOM)
            }
            PREVIEW_GRAVITY_CENTER -> {
                constraintSet.connect(viewStubId, TOP, parentId, TOP)
                constraintSet.connect(viewStubId, BOTTOM, parentId, BOTTOM)
            }
            PREVIEW_GRAVITY_BOTTOM -> {
                constraintSet.connect(viewStubId, TOP, -1, TOP)
                constraintSet.connect(viewStubId, END, parentId, END)
                constraintSet.connect(viewStubId, BOTTOM, parentId, BOTTOM)
            }
        }

        constraintSet.connect(buttonInfoId, END, viewStubId, START)
        constraintSet.connect(viewStubId, START, buttonInfoId, END)

        constraintSet.applyTo(binding.wrapper)
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

    companion object {
        const val PREVIEW_GRAVITY_TOP = 0
        const val PREVIEW_GRAVITY_CENTER = 1
        const val PREVIEW_GRAVITY_BOTTOM = 2

        const val TYPE_NONE = 0
        const val TYPE_PREVIEW = 1
        const val TYPE_TOGGLE = 2
    }
}