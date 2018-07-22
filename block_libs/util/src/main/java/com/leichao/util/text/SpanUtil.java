package com.leichao.util.text;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SpannableString工具类，可以改变字符串中部分文字的显示效果
 * <p>
 * 目前功能：修改文字颜色textColor()，修改文字大小textSize()，添加点击事件textClick()，替换字符为图片textImage()。
 * 其他功能目前项目中用不到，需要用到时可自行扩展。
 * <p>
 * Created by leichao on 2018/7/12.
 */
public class SpanUtil {

    private SpannableStringBuilder mSpanBuilder;// 可拼接的SpannableString
    private CharSequence mLastAppend;// 上一次拼接的字符串
    private int mFlags;// 设置首尾的插入模式
    private int mImageAlign;// 设置图片的相对位置

    private SpanUtil() {
    }

    //------------------------------------------基础方法---------------------------------------------//

    /**
     * 开始Span建造
     */
    public static SpanUtil with(CharSequence text) {
        SpanUtil spanUtil = new SpanUtil();
        spanUtil.mSpanBuilder = new SpannableStringBuilder(text);
        spanUtil.mLastAppend = text;
        spanUtil.mFlags = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        spanUtil.mImageAlign = AlignImageSpan.ALIGN_BASELINE;
        return spanUtil;
    }

    /**
     * 设置首尾Flags
     */
    public SpanUtil flags(int flags) {
        mFlags = flags;
        return this;
    }

    /**
     * 设置图片Align
     */
    public SpanUtil imageAlign(int align) {
        mImageAlign = align;
        return this;
    }

    /**
     * 拼接一个字符串
     */
    public SpanUtil append(CharSequence text) {
        mSpanBuilder.append(text);
        mLastAppend = text;
        return this;
    }

    /**
     * 完成Span建造，并返回SpannableString
     */
    public SpannableStringBuilder build() {
        return mSpanBuilder;
    }

    /**
     * 完成Span建造，并将SpannableString设置到TextView
     */
    public void into(TextView textView) {
        textView.setText(mSpanBuilder);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //------------------------------------------文字颜色---------------------------------------------//

    /**
     * 设置文字颜色
     */
    public SpanUtil textColor(int textColor) {
        textColor(textColor, mSpanBuilder.length() - mLastAppend.length(), mSpanBuilder.length());
        return this;
    }

    /**
     * 设置文字颜色
     */
    public SpanUtil textColor(int textColor, String text) {
        Matcher m = Pattern.compile(text).matcher(mSpanBuilder.toString());
        while (m.find()) {
            textColor(textColor, m.start(), m.end());
        }
        return this;
    }

    /**
     * 设置文字颜色
     */
    public SpanUtil textColor(int textColor, int start, int end) {
        mSpanBuilder.setSpan(new ForegroundColorSpan(textColor), start, end, mFlags);
        return this;
    }

    //------------------------------------------文字大小---------------------------------------------//

    /**
     * 设置文字大小
     */
    public SpanUtil textSize(int textSize) {
        textSize(textSize, mSpanBuilder.length() - mLastAppend.length(), mSpanBuilder.length());
        return this;
    }

    /**
     * 设置文字大小
     */
    public SpanUtil textSize(int textSize, String text) {
        Matcher m = Pattern.compile(text).matcher(mSpanBuilder.toString());
        while (m.find()) {
            textSize(textSize, m.start(), m.end());
        }
        return this;
    }

    /**
     * 设置文字大小
     */
    public SpanUtil textSize(int textSize, int start, int end) {
        mSpanBuilder.setSpan(new AbsoluteSizeSpan(textSize, true), start, end, mFlags);
        return this;
    }

    //------------------------------------------文字点击---------------------------------------------//

    /**
     * 设置文字点击事件
     * TextView需要调用setMovementMethod(LinkMovementMethod.getInstance())方法才能生效
     */
    public SpanUtil textClick(View.OnClickListener listener) {
        textClick(listener, mSpanBuilder.length() - mLastAppend.length(), mSpanBuilder.length());
        return this;
    }

    /**
     * 设置文字点击事件
     * TextView需要调用setMovementMethod(LinkMovementMethod.getInstance())方法才能生效
     */
    public SpanUtil textClick(View.OnClickListener listener, String text) {
        Matcher m = Pattern.compile(text).matcher(mSpanBuilder.toString());
        while (m.find()) {
            textClick(listener, m.start(), m.end());
        }
        return this;
    }

    /**
     * 设置文字点击事件
     * TextView需要调用setMovementMethod(LinkMovementMethod.getInstance())方法才能生效
     */
    public SpanUtil textClick(final View.OnClickListener listener, int start, int end) {
        mSpanBuilder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                listener.onClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //ds.setColor(textColor);// 修改点击文字的颜色
                ds.setUnderlineText(false);// 去掉点击文字的下划线
            }
        }, start, end, mFlags);
        return this;
    }

    //--------------------------------------文字图片(Drawable)---------------------------------------//

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Drawable drawable) {
        textImage(drawable, mSpanBuilder.length() - mLastAppend.length(), mSpanBuilder.length());
        return this;
    }

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Drawable drawable, String text) {
        Matcher m = Pattern.compile(text).matcher(mSpanBuilder.toString());
        while (m.find()) {
            textImage(drawable, m.start(), m.end());
        }
        return this;
    }

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Drawable drawable, int start, int end) {
        ImageSpan imageSpan = new AlignImageSpan(drawable, mImageAlign);
        mSpanBuilder.setSpan(imageSpan, start, end, mFlags);
        return this;
    }

    //---------------------------------------文字图片(resId)-----------------------------------------//

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Context context, int resId) {
        textImage(context, resId, mSpanBuilder.length() - mLastAppend.length(), mSpanBuilder.length());
        return this;
    }

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Context context, int resId, String text) {
        Matcher m = Pattern.compile(text).matcher(mSpanBuilder.toString());
        while (m.find()) {
            textImage(context, resId, m.start(), m.end());
        }
        return this;
    }

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Context context, int resId, int start, int end) {
        ImageSpan imageSpan = new AlignImageSpan(context, resId, mImageAlign);
        mSpanBuilder.setSpan(imageSpan, start, end, mFlags);
        return this;
    }

    //--------------------------------------文字图片(Bitmap)-----------------------------------------//

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Context context, Bitmap bitmap) {
        textImage(context, bitmap, mSpanBuilder.length() - mLastAppend.length(), mSpanBuilder.length());
        return this;
    }

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Context context, Bitmap bitmap, String text) {
        Matcher m = Pattern.compile(text).matcher(mSpanBuilder.toString());
        while (m.find()) {
            textImage(context, bitmap, m.start(), m.end());
        }
        return this;
    }

    /**
     * 设置文字图片
     */
    public SpanUtil textImage(Context context, Bitmap bitmap, int start, int end) {
        ImageSpan imageSpan = new AlignImageSpan(context, bitmap, mImageAlign);
        mSpanBuilder.setSpan(imageSpan, start, end, mFlags);
        return this;
    }

    //---------------------------------------------------------------------------------------------//

    /**
     * 可以使图片居中的ImageSpan
     */
    public class AlignImageSpan extends ImageSpan {

        public static final int ALIGN_CENTER = 101;

        private AlignImageSpan(Drawable d, int verticalAlignment) {
            super(d, verticalAlignment);
        }

        private AlignImageSpan(Context context, int resourceId, int verticalAlignment) {
            super(context, resourceId, verticalAlignment);
        }

        private AlignImageSpan(Context context, Bitmap b, int verticalAlignment) {
            super(context, b, verticalAlignment);
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            Drawable drawable = getDrawable();
            canvas.save();
            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
            int transY = bottom - drawable.getBounds().bottom;
            if (mVerticalAlignment == ALIGN_CENTER) {
                transY = ((y + fm.descent) + (y + fm.ascent)) / 2 - drawable.getBounds().bottom / 2;
            } else if (mVerticalAlignment == ALIGN_BASELINE) {
                transY -= fm.descent;
            }
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            Drawable d = getDrawable();
            Rect rect = d.getBounds();
            if (fm != null) {
                if (mVerticalAlignment == ALIGN_CENTER) {
                    Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                    int fontHeight = fmPaint.bottom - fmPaint.top;
                    int drHeight = rect.bottom - rect.top;
                    int top = drHeight / 2 - fontHeight / 4;
                    int bottom = drHeight / 2 + fontHeight / 4;
                    fm.ascent = -bottom;
                    fm.top = -bottom;
                    fm.bottom = top;
                    fm.descent = top;
                } else {
                    fm.ascent = -rect.bottom;
                    fm.descent = 0;
                    fm.top = fm.ascent;
                    fm.bottom = 0;
                }
            }
            return rect.right;
        }
    }

}
