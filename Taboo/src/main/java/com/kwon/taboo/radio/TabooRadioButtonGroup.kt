package com.kwon.taboo.radio

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.IdRes
import com.kwon.taboo.R

class TabooRadioButtonGroup(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var checkedButtonId: Int = View.NO_ID
    private var onChangeRadioButtonListener: ((id:Int, position: Int) -> Unit)? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooRadioButtonGroup)
        checkedButtonId = typed.getResourceId(R.styleable.TabooRadioButtonGroup_android_checkedButton, View.NO_ID)

        typed.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        Log.d(">>>", "onFinishInflate: $checkedButtonId")

        setCheckedButtonId(checkedButtonId)
        setListenerChildRadio()
    }

    fun setCheckedButtonId(@IdRes id: Int) {
        updateCheckedButton(id)
    }

    private fun updateCheckedButton(@IdRes checkedButtonId: Int) {
        findViewById<TabooRadioButton>(this.checkedButtonId).setChecked(false)

        // 새로운 버튼 체크
        findViewById<TabooRadioButton>(checkedButtonId).let {
            it.setChecked(true)
            this.checkedButtonId = checkedButtonId
        }
    }

    private fun setListenerChildRadio() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is TabooRadioButton) {
                child.setOnClickListener {
                    updateCheckedButton(child.id)
                    onChangeRadioButtonListener?.invoke(child.id, i)
                }
            }
        }
    }

    fun setOnChangeRadioButtonListener(listener: (id:Int, position: Int) -> Unit) {
        onChangeRadioButtonListener = listener
    }

}