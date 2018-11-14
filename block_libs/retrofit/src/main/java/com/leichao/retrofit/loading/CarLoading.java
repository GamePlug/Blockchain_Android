package com.leichao.retrofit.loading;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leichao.retrofit.R;

/**
 * 汽车加载动画
 * Created by leichao on 2017/3/7.
 */
public class CarLoading extends Dialog implements BaseLoading {

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
        this.message = message;
        this.cancelable = cancelable;
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
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        super.dismiss();
        if (carView != null) {
            carView.stop();
        }
    }

    /**
     * 加载汽车跑动View
     * Created by leichao on 2017/3/4.
     */
    public static class CarView extends View {

        private Paint paint;
        private int startX;
        private Bitmap bg, car;
        private Rect bgSrc, bgDst1, bgDst2;
        private Rect carSrc, carDst;
        private static final float bgScale = 3.6f;// bg与LoadingCarView宽度的比值
        private static final float carScale = 0.5f;// car与LoadingCarView宽度的比值
        private ObjectAnimator animator;

        public CarView(Context context) {
            super(context);
            init(context);
        }

        public CarView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init(context);
        }

        public CarView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init(context);
        }

        private void init(Context context) {
            this.paint = new Paint();
            bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_car_loading);
            car = BitmapFactory.decodeResource(getResources(), R.drawable.icon_car_loading);
            // 由于背景图片左右两边有黑线，所以左右两边各截取10像素，如果背景图正常，该行代码可以去掉
            bg = Bitmap.createBitmap(bg, 10, 0, bg.getWidth() - 20, bg.getHeight());

            bgSrc = new Rect();
            bgDst1 = new Rect();
            bgDst2 = new Rect();
            carSrc = new Rect();
            carDst = new Rect();

            bgSrc.left = 0;
            bgSrc.top = 0;
            bgSrc.right = bg.getWidth();
            bgSrc.bottom = bg.getHeight();

            carSrc.left = 0;
            carSrc.top = 0;
            carSrc.right = car.getWidth();
            carSrc.bottom = car.getHeight();

            setStartX(0);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int with = getWidth();
            int height = getHeight();
            paint.setAntiAlias(true);

            bgDst1.left = startX;
            bgDst1.top = (int) (height - bg.getHeight()*(with*bgScale)/bg.getWidth());
            bgDst1.right = (int) (startX + with*bgScale);
            bgDst1.bottom = height;

            bgDst2.left = (int) (startX + with*bgScale);
            bgDst2.top = (int) (height - bg.getHeight()*(with*bgScale)/bg.getWidth());
            bgDst2.right = (int) (startX + with*bgScale*2);
            bgDst2.bottom = height;

            carDst.left = (int) (with*(1-carScale)/2);
            carDst.top = (int) (height - car.getHeight()*(with*carScale)/car.getWidth());
            carDst.right = (int) (with - with*(1-carScale)/2);
            carDst.bottom = height;
            // 小车往上再移一点
            carDst.top -= height*0.1;
            carDst.bottom -= height*0.1;

            canvas.drawBitmap(getCircleBitmap(), 0, 0, paint);
        }

        /**
         * 将小车移动的Bitmap变换成圆形
         */
        private Bitmap getCircleBitmap() {
            Bitmap source = getCarBitmap();
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
            Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        /**
         * 获取小车移动的Bitmap
         */
        private Bitmap getCarBitmap() {
            Bitmap result = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(bg, bgSrc, bgDst1, paint);
            canvas.drawBitmap(bg, bgSrc, bgDst2, paint);
            canvas.drawBitmap(car, carSrc, carDst, paint);
            return result;
        }

        @Keep
        public void setStartX(int startX) {
            this.startX = startX;
            invalidate();
        }

        public void start() {
            this.post(new Runnable() {
                @Override
                public void run() {
                    animator = ObjectAnimator.ofInt(CarView.this, "startX", 0, (int)(-getWidth()*bgScale));
                    animator.setDuration((long) (3 * 1000));
                    animator.setRepeatCount(-1);
                    animator.setInterpolator(new LinearInterpolator());
                    animator.start();
                }
            });
        }

        public void stop() {
            if (animator != null) {
                animator.cancel();
            }
        }

    }

}