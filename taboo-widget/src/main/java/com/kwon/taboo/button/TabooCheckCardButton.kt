package com.kwon.taboo.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooCheckCardButtonBinding

class TabooCheckCardButton(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs), Checkable {
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_check_card_button, this, true)

    private var title = ""
    private var description = ""

    private var isChecked = false

    private var listener: OnClickListener? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooCheckCardButton)
        val title = typed.getString(R.styleable.TabooCheckCardButton_title) ?: ""
        val description = typed.getString(R.styleable.TabooCheckCardButton_description) ?: ""
        val isChecked = typed.getBoolean(R.styleable.TabooCheckCardButton_isChecked, false)

        typed.recycle()

        setTitle(title)
        setDescription(description)
        setChecked(isChecked)

        initCheckCard()
    }

    private fun initCheckCard() {
        updateTitle()
        updateDescription()
        updateCheck()

        setOnClickListener(null)
    }

    fun getTitle() = title

    fun setTitle(title: String) {
        this.title = title

        updateTitle()
    }

    private fun updateTitle() {
        rootView.findViewById<TextView>(R.id.tv_title).text = title
    }

    fun getDescription() = description

    fun setDescription(description: String) {
        this.description = description

        updateDescription()
    }

    private fun updateDescription() {
        rootView.findViewById<TextView>(R.id.tv_description).text = description
    }

    override fun setChecked(checked: Boolean) {
        this.isChecked = checked

        updateCheck()
    }

    private fun updateCheck() {
        val backgroundDrawable =
            if (isChecked) R.drawable.shape_rect_r5_a100_f9fafb_a100_b0b8c1
            else R.drawable.shape_rect_r5_a100_000000_a100_b0b8c1
        background = ContextCompat.getDrawable(context, backgroundDrawable)

        rootView.findViewById<ImageView>(R.id.iv_check).visibility = if (isChecked) VISIBLE else INVISIBLE
    }

    override fun isChecked() = isChecked

    override fun toggle() {
        isChecked = !isChecked

        updateCheck()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener {
            toggle()
            listener?.onClick(this)
        }
    }
}