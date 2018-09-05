package com.leichao.common.view

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.leichao.common.R
import com.leichao.util.ScreenUtil
import com.leichao.util.StatusBarUtil
import kotlinx.android.synthetic.main.view_title_bar.view.*

/**
 * 自定义标题栏
 * Created by leichao on 2016/1/1.
 */

class BiuTitleBar : LinearLayout {

    private var tvTitle: TextView? = null
    private var ivBack: ImageView? = null

    enum class Position {
        LEFT, MID, RIGHT
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        (context as? Activity)?.let { StatusBarUtil.setFullTranslucent(it) }
        orientation = LinearLayout.VERTICAL
        setBackgroundColor(ContextCompat.getColor(context, R.color.color_theme))
        LayoutInflater.from(context).inflate(R.layout.view_title_bar, this)
        // 默认添加标题和返回键
        tvTitle = addTextView("", Position.MID, null)
        ivBack = addImageView(R.drawable.title_back, Position.LEFT, OnClickListener {
            (context as? Activity)?.finish()
        })
        // 在xml预览时不执行
        if (!isInEditMode) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                title_status_bar.layoutParams.height = ScreenUtil.dip2px(20f)
            }
            ivBack?.setPadding(ScreenUtil.dip2px(18f), 0, ScreenUtil.dip2px(18f), 0)
        }
    }

    /**
     * 设置标题文字
     */
    fun setTitleText(title: String) {
        tvTitle?.text = title
    }

    /**
     * 设置标题颜色
     */
    fun setTitleColor(color: Int) {
        tvTitle?.setTextColor(color)
    }

    /**
     * 设置标题点击监听
     */
    fun setTitleListener(listener: View.OnClickListener) {
        tvTitle?.setOnClickListener(listener)
    }

    /**
     * 设置返回键图片资源
     */
    fun setBackDrawable(resid: Int) {
        ivBack?.setImageResource(resid)
    }

    /**
     * 设置返回键是否显示
     */
    fun setBackVisible(visibility: Int) {
        ivBack?.visibility = visibility
    }

    /**
     * 设置返回键点击监听
     */
    fun setBackListener(listener: View.OnClickListener) {
        ivBack?.setOnClickListener(listener)
    }

    /**
     * 设置底部间隔线颜色
     */
    fun setLineViewColor(color: Int) {
        title_bar_line.setBackgroundColor(color)
    }

    /**
     * 设置底部间隔线是否显示
     */
    fun setLineViewVisible(visibility: Int) {
        title_bar_line.visibility = visibility
    }

    /**
     * 添加TextView
     *
     * @param text      文字
     * @param position 位置
     * @param listener 监听
     * @return TextView
     */
    fun addTextView(text: String, position: Position, listener: View.OnClickListener?): TextView {
        val textView = TextView(context)
        textView.text = text
        textView.setTextColor(ContextCompat.getColor(context, R.color.color_ffffff))
        textView.textSize = 20f
        textView.setLines(1)
        listener?.let { textView.setOnClickListener(it) }
        addCustomView(textView, position)
        return textView
    }

    /**
     * 添加ImageView
     *
     * @param resId    图片资源id
     * @param position 位置
     * @param listener 监听
     * @return ImageView
     */
    fun addImageView(resId: Int, position: Position, listener: View.OnClickListener?): ImageView {
        val imageView = ImageView(context)
        imageView.setImageResource(resId)
        imageView.setBackgroundResource(R.drawable.selector_common_click)
        imageView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT)
        listener?.let { imageView.setOnClickListener(it) }
        addCustomView(imageView, position)
        return imageView
    }

    /**
     * 添加View
     *
     * @param view     View
     * @param position 位置
     */
    fun addCustomView(view: View, position: Position) {
        when (position) {
            Position.LEFT -> title_bar_left.addView(view)
            Position.MID -> title_bar_mid.addView(view)
            Position.RIGHT -> title_bar_right.addView(view)
        }
    }

}
