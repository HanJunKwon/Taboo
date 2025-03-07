package com.kwon.taboo.button

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Checkable
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.databinding.TabooCheckCardButtonBinding

class TabooCheckCardButton(
    context: Context,
    attrs: AttributeSet
) : ConstraintLayout(context, attrs), Checkable {
    private val binding = TabooCheckCardButtonBinding.inflate(LayoutInflater.from(context), this, true)

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
        binding.tvTitle.text = title
    }

    fun getDescription() = description

    fun setDescription(description: String) {
        this.description = description

        updateDescription()
    }

    private fun updateDescription() {
        binding.tvDescription.text = description
    }

    override fun setChecked(checked: Boolean) {
        this.isChecked = checked

        updateCheck()
    }

    private fun updateCheck() {
        val backgroundDrawable =
            if (isChecked) R.drawable.shape_rect_r5_a100_f9fafb_a100_8b95a1
            else R.drawable.shape_rect_r5_a100_000000_a100_8b95a1
        background = ContextCompat.getDrawable(context, backgroundDrawable)

        binding.ivCheck.visibility = if (isChecked) VISIBLE else INVISIBLE
    }

    override fun isChecked() = isChecked

    override fun toggle() {
        isChecked = !isChecked

        updateCheck()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.root.setOnClickListener {
            toggle()
            listener?.onClick(this)
        }
    }
}