package com.kwon.taboo.button

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
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

    private var menuType = MENU_TYPE_NONE

    private var preview = "Preview"
    private var previewGravity: Int = PREVIEW_GRAVITY_TOP
    private var iconResource: Drawable? = null

    private var inflatedView: View? = null

    init {
        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooMenuButton)
        val isEnabled = typed.getBoolean(R.styleable.TabooMenuButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooMenuButton_android_text) ?: "Preview Button"
        val description = typed.getString(R.styleable.TabooMenuButton_description) ?: "Preview Button Description"
        val type = typed.getInt(R.styleable.TabooMenuButton_menuType, MENU_TYPE_NONE)
        val preview = typed.getString(R.styleable.TabooMenuButton_preview) ?: "Preview"
        val previewGravity = typed.getInt(R.styleable.TabooMenuButton_previewGravity, PREVIEW_GRAVITY_TOP)
        val iconResourceId = typed.getResourceId(R.styleable.TabooMenuButton_icon, R.drawable.ic_box_44dp)

        typed.recycle()

        setEnabled(isEnabled)
        setText(text)
        setDescription(description)
        setMenuType(type)
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

    /**
     * 버튼의 타이틀을 설정한다.
     *
     * @param text 타이틀에 표시할 텍스트.
     */
    fun setText(text: String) {
        this.text = text
        updateText()
    }

    /**
     * 타이틀을 업데이트 한다.
     */
    private fun updateText() {
        binding.tvButtonName.text = text
    }

    /**
     * 버튼의 description 을 설정한다.
     *
     * @param description 버튼의 description
     */
    fun setDescription(description: String) {
        this.description = description
        updateDescription()
    }

    /**
     * description 을 업데이트 한다.
     */
    private fun updateDescription() {
        binding.tvButtonDescription.text = description
    }

    /**
     * [menuType]을 설정한다.
     *
     * @param menuType [MENU_TYPE_NONE], [MENU_TYPE_PREVIEW], [MENU_TYPE_TOGGLE]
     *
     * @see [updateMenuType]
     */
    private fun setMenuType(@MenuType menuType: Int) {
        this.menuType = menuType

        updateMenuType()
    }

    /**
     * [menuType]에 따라 ViewStub 에 inflate 할 레이아웃을 변경한다.
     * - [MENU_TYPE_NONE] : 레이아웃을 변경하지 않는다.
     * - [MENU_TYPE_PREVIEW] : [R.layout.taboo_menu_button_preview_fragment]
     * - [MENU_TYPE_TOGGLE] : [R.layout.taboo_menu_button_toggle_fragment].
     * @see [setMenuType]
     */
    private fun updateMenuType() {
        val view = when (menuType) {
            MENU_TYPE_PREVIEW -> R.layout.taboo_menu_button_preview_fragment
            MENU_TYPE_TOGGLE -> R.layout.taboo_menu_button_toggle_fragment
            else -> return
        }

        inflatedView = binding.vsFragment.viewStub?.let { viewStub ->
            viewStub.layoutResource = view
            viewStub.inflate()
        }
    }

    /**
     * 미리보기의 텍스트를 설정한다.
     */
    fun setPreview(preview: String) {
        this.preview = preview

        updatePreview()
    }

    /**
     * 미리보기의 UI를 업데이트 한다.
     *
     * [menuType]이 [MENU_TYPE_PREVIEW]일 때만 동작한다.
     */
    private fun updatePreview() {
        if (menuType != MENU_TYPE_PREVIEW) return

        inflatedView?.findViewById<TextView>(R.id.tv_button_preview)?.text = preview
    }

    fun getPreviewGravity() = previewGravity

    /**
     * 미리보기의 위치를 설정한다. 미리보기는 우측으로 고정되어 있기 때문에 수직 위치만 변경할 수 있다.
     *
     * @param previewGravity 미리보기의 수직 위치 값.
     * [PREVIEW_GRAVITY_TOP], [PREVIEW_GRAVITY_CENTER], [PREVIEW_GRAVITY_BOTTOM] 중 하나여야 한다.
     */
    fun setPreviewGravity(@PreviewGravity previewGravity: Int) {
        this.previewGravity = previewGravity

        updatePreviewGravity()
    }

    /**
     * 미리보기의 위치를 설정하는 함수.
     * 현재 설정된 [previewGravity] 값에 따라 미리보기(ViewStub)의 제약 조건을 변경하여 배치한다.
     *
     * - [PREVIEW_GRAVITY_TOP] : 미리보기를 부모 뷰의 상단에 위치시킨다.
     * - [PREVIEW_GRAVITY_CENTER] : 미리보기를 부모 뷰의 수직 중앙에 위치시킨다.
     * - [PREVIEW_GRAVITY_BOTTOM] : 미리보기를 부모 뷰의 하단에 위치시킨다.
     *
     * 또한, 버튼 정보 컨테이너([clButtonInformationContainer])와 미리보기 뷰 간의 좌우 제약 조건을 설정하여,
     * 버튼 정보 컨테이너가 미리보기 뷰의 왼쪽에 배치되도록 한다.
     *
     * 이 함수는 [binding.wrapper]에 새로운 [ConstraintSet]을 적용하여 변경된 레이아웃을 반영한다.
     *
     * @see [setPreviewGravity]
     */
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

    /**
     * 버튼 왼쪽에 표시되는 아이콘을 설정한다.
     *
     * @param resourceId 아이콘 리소스 ID
     *
     * @see [setIconResource]
     */
    fun setIconResourceId(@DrawableRes resourceId: Int) {
        setIconResource(ContextCompat.getDrawable(context, resourceId))
    }

    /**
     * 버튼 왼쪽에 표시되는 아이콘을 설정한다.
     *
     * @see [setIconResourceId]
     *
     * @param resource 아이콘 Drawable
     */
    fun setIconResource(resource: Drawable?) {
        this.iconResource = resource

        updateIconResource()
    }

    /**
     * 아이콘을 업데이트 한다.
     */
    private fun updateIconResource() {
        binding.ivButtonIcon.setImageDrawable(iconResource)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener(l)
    }

    @IntDef(MENU_TYPE_NONE, MENU_TYPE_PREVIEW, MENU_TYPE_TOGGLE)
    @Retention(AnnotationRetention.SOURCE)
    annotation class MenuType

    @IntDef(PREVIEW_GRAVITY_TOP, PREVIEW_GRAVITY_CENTER, PREVIEW_GRAVITY_BOTTOM)
    @Retention(AnnotationRetention.SOURCE)
    annotation class PreviewGravity

    companion object {
        /**
         * 레이아웃의 높이와 관계없이 미리보기를 부모 뷰의 상단에 고정할 때 사용한다. (기본값)
         * @see [setPreviewGravity]
         */
        const val PREVIEW_GRAVITY_TOP = 0

        /**
         * 미리보기를 부모 뷰의 중앙에 배치할 때 사용한다.
         *
         * 레이아웃의 높이에 따라 위치가 달라질 수 있으므로 주의가 필요하다.
         */
        const val PREVIEW_GRAVITY_CENTER = 1

        /**
         * 레이아웃의 높이와 관계없이 미리보기를 부모 뷰의 하단에 고정할 때 사용한다.
         *
         * 레이아웃의 높이가 높아지는 경우에는 미리보기가 하단에 위치하기 때문에 UX에 영향을 줄 수 있다.
         */
        const val PREVIEW_GRAVITY_BOTTOM = 2

        /**
         * 버튼이 아이콘, 타이틀, Description 으로만 구성된 버튼을 생성할 때 사용한다.(기본값)
         */
        const val MENU_TYPE_NONE = 0

        /**
         * 우측에 미리보기가 표시되는 버튼을 생성할 때 사용한다.
         */
        const val MENU_TYPE_PREVIEW = 1

        /**
         * 버튼 우측에 Toggle 버튼이 있는 버튼을 생성할 때 사용한다.
         */
        const val MENU_TYPE_TOGGLE = 2
    }
}