package com.leichao.retrofit.loading;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leichao.retrofit.R;

/**
 * 默认加载动画
 * Created by leichao on 2017/3/7.
 */
public class DefaultLoading extends Dialog implements BaseLoading {

    private Context context;
    private String message;
    private boolean cancelable = true;
    private ImageView ivImage;

    public DefaultLoading(Context context) {
        this(context, null, true);
    }

    public DefaultLoading(Context context, String message) {
        this(context, message, true);
    }

    public DefaultLoading(Context context, String message, boolean cancelable) {
        super(context, R.style.dialog_light_bg);
        this.context = context;
        this.cancelable = cancelable;
        this.message = message;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.layout_default_loading, null);// 得到加载view
        LinearLayout layout = view.findViewById(R.id.dialog_view);// 加载布局
        ivImage = view.findViewById(R.id.tvImage);
        if (!TextUtils.isEmpty(message)) {// 提示文字
            TextView tvMessage = view.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);
        }
        setCancelable(cancelable);// 可以用“返回键”取消
        setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
    }

    @Override
    public void show() {
        super.show();
        if (ivImage != null) {
            Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                    context, R.anim.anim_default_loading);
            ivImage.startAnimation(hyperspaceJumpAnimation);
        }
    }

    @Override
    public void dismiss() {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        super.dismiss();
        if (ivImage != null) {
            ivImage.clearAnimation();
        }
    }

}