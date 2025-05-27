package com.kwon.taboo.button

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewStub
import android.view.animation.DecelerateInterpolator
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.appcompat.widget.SwitchCompat
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
    private val rootView = LayoutInflater.from(context).inflate(R.layout.taboo_menu_button, this, true)

    private var menuTitle = "Preview Button"
    private var description = "Preview Button Description"

    private var menuType = MENU_TYPE_NONE

    private var preview = "Preview"
    private var previewGravity: Int = PREVIEW_GRAVITY_TOP

    private var isToggleChecked = false

    private var iconDrawable: Drawable? = null

    private var inflatedView: View? = null

    private var isPressedAnimationsEnabled = true
    private var duration: Long = 100L
    private var scaleRatio: Float = 0.95f
    private var pressedEnterScaleXAnim: ObjectAnimator? = null
    private var pressedEnterScaleYAnim: ObjectAnimator? = null
    private var pressedExitScaleXAnim: ObjectAnimator? = null
    private var pressedExitScaleYAnim: ObjectAnimator? = null

    init {
        isFocusable = true
        isClickable = true

        val typed = context.obtainStyledAttributes(attrs, R.styleable.TabooMenuButton)
        val isEnabled = typed.getBoolean(R.styleable.TabooMenuButton_android_enabled, true)
        val text = typed.getString(R.styleable.TabooMenuButton_menuTitle) ?: "Menu Title"
        val description = typed.getString(R.styleable.TabooMenuButton_menuDescription) ?: "Menu Description"
        val menuType = typed.getInt(R.styleable.TabooMenuButton_menuType, MENU_TYPE_NONE)
        val preview = typed.getString(R.styleable.TabooMenuButton_preview) ?: "Preview"
        val previewGravity = typed.getInt(R.styleable.TabooMenuButton_previewGravity, PREVIEW_GRAVITY_TOP)
        val isToggleChecked = typed.getBoolean(R.styleable.TabooMenuButton_toggleChecked, false)
        val iconResourceId = typed.getResourceId(R.styleable.TabooMenuButton_icon, 0)
        val pressedAnimation = typed.getBoolean(R.styleable.TabooMenuButton_pressedAnimation, true)
        val duration = typed.getInt(R.styleable.TabooMenuButton_animationDuration, 50).toLong()
        val scaleRatio = typed.getFloat(R.styleable.TabooMenuButton_pressedScaleRatio, 0.95f)

        typed.recycle()

        setText(text)
        setDescription(description)
        setMenuType(menuType)
        setPreview(preview)
        setPreviewGravity(previewGravity)
        isToggleChecked(isToggleChecked)
        setIconResourceId(iconResourceId)
        setEnabled(isEnabled)
        setPressedAnimationsEnabledInternal(pressedAnimation)

        background = ContextCompat.getDrawable(context, R.drawable.taboo_menu_button_background)

        if (isPressedAnimationsEnabled) {
            setDurationInternal(duration)
            setScaleRatioInternal(scaleRatio)

            applyPressedAnimations()
        }
    }

    private fun applyPressedAnimations() {
        updatePressedAnimations()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        updateEnabled()
    }

    private fun updateEnabled() {
        alpha = if (isEnabled) 1.0f else 0.3f

        inflatedView?.findViewById<TextView>(R.id.switch_menu)?.isEnabled = this.isEnabled
    }

    fun getText() = menuTitle

    /**
     * 버튼의 타이틀을 설정한다.
     *
     * @param text 타이틀에 표시할 텍스트.
     */
    fun setText(text: String) {
        this.menuTitle = text
        updateText()
    }

    /**
     * 타이틀을 업데이트 한다.
     */
    private fun updateText() {
        rootView.findViewById<TextView>(R.id.tv_button_name).text = menuTitle
    }

    fun getDescription() = description

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
     *
     * [description]이 비어있을 경우, Description TextView [View.GONE] 처리.
     */
    private fun updateDescription() {
        rootView.findViewById<TextView>(R.id.tv_button_description).apply {
            text = description
            visibility = if (description.isBlank()) View.GONE else View.VISIBLE
        }
    }

    fun getMenuType() = menuType

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

        inflatedView = rootView.findViewById<ViewStub>(R.id.vs_fragment)?.let { viewStub ->
            viewStub.layoutResource = view
            viewStub.inflate()
        }
    }

    fun getPreview() = preview

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
        constraintSet.clone(rootView.findViewById<ConstraintLayout>(R.id.wrapper))

        val buttonInfoId = rootView.findViewById<ConstraintLayout>(R.id.cl_button_information_container).id
        val viewStubId = inflatedView?.id ?: return
        val parentId = rootView.findViewById<ConstraintLayout>(R.id.wrapper).id

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

        constraintSet.applyTo(rootView.findViewById<ConstraintLayout>(R.id.wrapper))
    }

    /**
     * 버튼 왼쪽에 표시되는 아이콘을 설정한다.
     *
     * @param iconResourceId 아이콘 리소스 ID
     *
     * @see [setIconResource]
     */
    fun setIconResourceId(@DrawableRes iconResourceId: Int) {
        val iconDrawable = if (iconResourceId == 0) null else ContextCompat.getDrawable(context, iconResourceId)

        setIconResource(iconDrawable)
    }

    /**
     * 버튼 왼쪽에 표시되는 아이콘을 설정한다.
     *
     * @see [setIconResourceId]
     *
     * @param iconDrawable 아이콘 Drawable
     */
    fun setIconResource(iconDrawable: Drawable?) {
        this.iconDrawable = iconDrawable

        updateIconResource()
    }

    /**
     * 아이콘을 업데이트 한다.
     */
    private fun updateIconResource() {
        rootView.findViewById<ImageView>(R.id.iv_button_icon).setImageDrawable(iconDrawable)
    }

    fun isToggleChecked(): Boolean {
        return if (menuType == MENU_TYPE_TOGGLE) {
            inflatedView?.findViewById<SwitchCompat>(R.id.switch_menu)?.isChecked ?: false
        } else {
            false
        }
    }

    fun isToggleChecked(isChecked: Boolean) {
        if (menuType == MENU_TYPE_TOGGLE) {
            inflatedView?.findViewById<SwitchCompat>(R.id.switch_menu)?.isChecked = isChecked
        }
    }

    fun isPressedAnimationsEnabled(): Boolean {
        return isPressedAnimationsEnabled
    }

    private fun setPressedAnimationsEnabledInternal(enabled: Boolean) {
        isPressedAnimationsEnabled = enabled
    }

    fun isPressedAnimationsEnabled(enabled: Boolean) {
        setPressedAnimationsEnabledInternal(enabled)
        updatePressedAnimations()
    }

    fun getDuration(): Long {
        return duration
    }

    private fun setDurationInternal(duration: Long) {
        if (duration <= 0) {
            throw IllegalArgumentException("Duration must be greater than 0")
        }

        this.duration = duration

    }

    fun setDuration(duration: Long) {
        setDurationInternal(duration)

        updatePressedAnimations()
    }

    fun getScaleRatio(): Float {
        return scaleRatio
    }

    private fun setScaleRatioInternal(scaleRatio: Float) {
        if (scaleRatio <= 0 || scaleRatio > 1) {
            throw IllegalArgumentException("Scale ratio must be between 0 and 1")
        }

        this.scaleRatio = scaleRatio
    }

    fun setScaleRatio(scaleRatio: Float) {
        setScaleRatioInternal(scaleRatio)

        updatePressedAnimations()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (isPressedAnimationsEnabled) {
            when (ev?.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressedEnterScaleXAnim?.start()
                    pressedEnterScaleYAnim?.start()
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    pressedExitScaleXAnim?.start()
                    pressedExitScaleYAnim?.start()
                }
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun updatePressedAnimations() {
        if (isPressedAnimationsEnabled) {
            pressedEnterScaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, scaleRatio).apply {
                this.duration = duration
                this.interpolator = DecelerateInterpolator()
            }

            pressedEnterScaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, scaleRatio).apply {
                this.duration = duration
                this.interpolator = DecelerateInterpolator()
            }

            pressedExitScaleXAnim = ObjectAnimator.ofFloat(this, "scaleX", scaleRatio, 1.0f).apply {
                this.duration = duration
                this.interpolator = DecelerateInterpolator()
            }

            pressedExitScaleYAnim = ObjectAnimator.ofFloat(this, "scaleY", scaleRatio, 1.0f).apply {
                this.duration = duration
                this.interpolator = DecelerateInterpolator()
            }
        } else {
            pressedEnterScaleXAnim?.cancel()
            pressedEnterScaleYAnim?.cancel()
            pressedExitScaleXAnim?.cancel()
            pressedExitScaleYAnim?.cancel()

            scaleX = 1.0f
            scaleY = 1.0f

            pressedExitScaleXAnim = null
            pressedExitScaleYAnim = null
            pressedEnterScaleXAnim = null
            pressedEnterScaleYAnim = null
        }
    }

    /**
     * Toggle 버튼의 상태 변경 이벤트를 설정한다.
     *
     * [menuType]이 [MENU_TYPE_TOGGLE]일 때만 동작한다.
     *
     * 클릭 시 이벤트를 등록하고 싶으면 [setOnClickListener]를 사용한다.
     */
    fun setOnCheckedChangeListener(l: CompoundButton.OnCheckedChangeListener?) {
        if (menuType == MENU_TYPE_TOGGLE) {
            inflatedView?.findViewById<SwitchCompat>(R.id.switch_menu)?.setOnCheckedChangeListener { buttonView, isChecked ->
                l?.onCheckedChanged(buttonView, isChecked)
            }
        }
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