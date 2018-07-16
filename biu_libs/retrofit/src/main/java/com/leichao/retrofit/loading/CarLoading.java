package com.leichao.retrofit.loading;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leichao.retrofit.R;

/**
 * 加载提示dialog
 * Created by leichao on 2017/3/7.
 */
public class CarLoading extends Dialog {

    private Context context;
    private String message;
    private boolean cancelable = true;
    private CarView carView;

    public CarLoading(Context context) {
        this(context, null, true);
    }

    public CarLoading(Context context, String message) {
        this(context, message, true);
    }

    public CarLoading(Context context, String message, boolean cancelable) {
        super(context, R.style.dialog_dim_bg);
        this.context = context;
        this.cancelable = cancelable;
        this.message = message;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.layout_car_loading, null);// 得到加载view
        carView = view.findViewById(R.id.carView);
        if (!TextUtils.isEmpty(message)) {// 提示文字
            TextView tvMessage = view.findViewById(R.id.tvMessage);
            tvMessage.setText(message);
            tvMessage.setVisibility(View.VISIBLE);
        }
        setCancelable(cancelable);// 可以用“返回键”取消
        setContentView(view, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));// 设置布局
    }

    @Override
    public void show() {
        super.show();
        if (carView != null) {
            carView.start();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (carView != null) {
            carView.stop();
        }
    }

}