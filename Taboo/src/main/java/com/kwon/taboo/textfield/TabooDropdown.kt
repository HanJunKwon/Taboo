package com.kwon.taboo.textfield

import android.content.Context
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListPopupWindow
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.kwon.taboo.R
import com.kwon.taboo.adapter.TabooDropdownAdapter
import com.kwon.taboo.enums.AffixType

class TabooDropdown(
    private val context: Context,
    private val parent: ConstraintLayout
) : TabooTextField(context, parent) {
    private var ivDropdown: ImageView? = null

    private var adapter: TabooDropdownAdapter? = null

    private var listener: (() -> Unit)? = null
    private var items = arrayOf<String>()

    private var itemSelectedListener: ((parent: AdapterView<*>?, view: View?, position: Int, id: Long) -> Unit)? = null
    private var itemChangedListener: ((position: Int) -> Unit)? = null

    init {
        setFocusable(false)
        setClickable(false)
    }

    private fun createDropdownIcon() {
        ivDropdown = ImageView(context).apply {
            setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_round_arrow_bottom))
            setDropdownIconColor(ContextCompat.getColor(context, R.color.taboo_gray_03))
            id = View.generateViewId()
        }
    }

    fun bindDropdownIcon() {
        if (ivDropdown == null) {
            createDropdownIcon()
        }

        val parentView = view.findViewById<ConstraintLayout>(R.id.cl_edit_text_wrapper)

        // 자식 뷰에 추가
        parentView.addView(ivDropdown)

        ConstraintSet().apply {
            clone(parentView)
            applyAffixConstraints(this, ivDropdown!!, AffixType.SUFFIX, parentView)
            applyTo(parentView)
        }

        setDropdownClickListener()
    }

    private fun applyAffixConstraints(
        constraintSet: ConstraintSet,
        view: View,
        affixType: AffixType,
        parentView: ConstraintLayout
    ) {
        when (affixType) {
            AffixType.PREFIX -> {
                constraintSet.connect(view.id, ConstraintSet.START, parentView.id, ConstraintSet.START)
                constraintSet.connect(view.id, ConstraintSet.END, editText.id, ConstraintSet.START)
                constraintSet.connect(editText.id, ConstraintSet.START, view.id, ConstraintSet.END)
            }
            AffixType.SUFFIX -> {
                constraintSet.connect(view.id, ConstraintSet.START, editText.id, ConstraintSet.END)
                constraintSet.connect(view.id, ConstraintSet.END, parentView.id, ConstraintSet.END)
                constraintSet.connect(editText.id, ConstraintSet.END, view.id, ConstraintSet.START)
            }
        }

        // 공통 제약 조건 추가
        constraintSet.connect(view.id, ConstraintSet.TOP, parentView.id, ConstraintSet.TOP)
        constraintSet.connect(view.id, ConstraintSet.BOTTOM, parentView.id, ConstraintSet.BOTTOM)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        ivDropdown?.isEnabled = enabled
    }

    fun setDropdownIcon(@DrawableRes icon: Int) {
        ivDropdown?.setImageDrawable(AppCompatResources.getDrawable(context, icon))
    }

    fun setDropdownIconColor(color: Int) {
        ivDropdown?.setColorFilter(color)
    }

    fun setItems(items: Array<String>) {
        this.items = items
    }

    fun setSelectedPosition(position: Int) {
        adapter?.setSelectedPosition(position)
        itemChangedListener?.invoke(position)
    }

    fun setDropdownClickListener(listener: () -> Unit) {
        this.listener = listener
    }

    private fun setDropdownClickListener() {
        editText.setOnClickListener {
            view.performClick()
        }

        ivDropdown?.setOnClickListener {
            view.performClick()
        }

        view.setOnClickListener {
            showPopupWindow()

            listener?.invoke()
        }
    }

    private fun showPopupWindow() {
        if (adapter == null) {
            adapter = TabooDropdownAdapter(
                context = context,
                layout = R.layout.taboo_dropdown_list_item,
                items = items
            ).apply {
                setDropDownViewResource(R.layout.taboo_dropdown_list_item)
            }
        }

        ListPopupWindow(context)
            .apply {
                setAnchorView(view)
                setModal(true)
                setPromptPosition(ListPopupWindow.POSITION_PROMPT_ABOVE)
                setAdapter(adapter)
                setOnItemClickListener({ parent, v, position, id ->
                    // 아이템 선택 & 변경 리스너 콜백
                    itemSelectedListener?.invoke(parent, v, position, id)
                    itemChangedListener?.invoke(position)

                    // 선택 아이템 포지션 저장 및 텍스트 설정
                    adapter?.setSelectedPosition(position)
                    editText.setText(items[position])

                    // 팝업 닫기
                    dismiss()
                })
                width = this@TabooDropdown.getWidth()
                verticalOffset = 10
            }.show()
    }

    private fun getWidth() = view.width

    fun setOnItemSelectedListener(listener: ((parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) -> Unit)? = null) {
        itemSelectedListener = listener
    }

    fun setOnItemChangedListener(listener: ((position: Int) -> Unit)? = null) {
        itemChangedListener = listener
    }
}