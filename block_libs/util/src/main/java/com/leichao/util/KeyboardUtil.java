package com.leichao.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public final class KeyboardUtil {

    private KeyboardUtil() {
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        showSoftInput(view);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(View view) {
        InputMethodManager imm = KeyboardManager.getInputManager();
        if (imm == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) view = new View(activity);
        hideSoftInput(view);
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(View view) {
        InputMethodManager imm = KeyboardManager.getInputManager();
        if (imm == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * 强制显示软键盘
     */
    public static void toggleSoftInput() {
        InputMethodManager imm = KeyboardManager.getInputManager();
        if (imm == null) return;
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 判断软键盘是否显示
     */
    public static boolean isSoftInputShow(Activity activity) {
        return KeyboardManager.isSoftInputShow(activity);
    }

    /**
     * 设置软键盘显示或隐藏的监听，无法主动移除(会自动随Activity销毁而移除)
     * 如果需要手动移除，请调用如下两个方法
     * {@link #addStatusListener(Activity, OnKeyboardStatusListener)}
     * {@link #removeStatusListener(Activity, OnKeyboardStatusListener)}
     */
    public static void addStatusListenerNoNeedRemove(Activity activity, OnKeyboardStatusListener listener) {
        new StatusListenerImpl().setStatusListener(activity, listener);
    }

    /**
     * 设置软键盘显示或隐藏的监听
     */
    public static void addStatusListener(Activity activity, OnKeyboardStatusListener listener) {
        StatusListenerImpl impl = new StatusListenerImpl();
        impl.setStatusListener(activity, listener);
        StatusListenerImpl lastImpl = KeyboardManager.listeners.put(listener, impl);// put返回的是在put之前已存在的值
        if (lastImpl != null) lastImpl.removeStatusListener(activity);// 如果存在被覆盖的监听器则移除
    }

    /**
     * 移除软键盘显示或隐藏的监听
     */
    public static void removeStatusListener(Activity activity, OnKeyboardStatusListener listener) {
        StatusListenerImpl impl = KeyboardManager.listeners.remove(listener);
        if (impl != null) impl.removeStatusListener(activity);
    }

    /**
     * 解决全屏沉浸式状态栏和输入法resize模式冲突的bug
     * 在Activity的onCreate中setContentView方法调用之后再调用此方法
     * {@link Activity#onCreate(Bundle)} {@link Activity#setContentView}
     */
    public static void fixAndroidBug5497(Activity activity) {
        new AndroidBug5497Impl().fixAndroidBug5497(activity);
    }

    /**
     * 解决InputMethodManager持有了EditText的引用导致的内存泄漏问题
     * 在Activity的onDestroy的super之前调用{@link Activity#onDestroy()}
     */
    public static void fixSoftInputLeaks(Activity activity) {
        KeyboardManager.fixSoftInputLeaks(activity);
    }

    /**
     * 点击空白区域隐藏软键盘
     * 复制方法中注释的代码到你的Activity
     */
    public static void clickBlankHideSoftInput() {
        /*
        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    InputMethodManager imm =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS
                    );
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // Return whether touch the view.
        private boolean isShouldHideKeyboard(View v, MotionEvent event) {
            if (v != null && (v instanceof EditText)) {
                int[] l = {0, 0};
                v.getLocationInWindow(l);
                int left = l[0],
                        top = l[1],
                        bottom = top + v.getHeight(),
                        right = left + v.getWidth();
                return !(event.getX() > left && event.getX() < right
                        && event.getY() > top && event.getY() < bottom);
            }
            return false;
        }
        */
    }

    /**
     * 软键盘显示或隐藏的状态变化回调
     */
    public interface OnKeyboardStatusListener {
        void onKeyboardStatus(boolean isShow);
    }


    //------------------------------------------内部方法---------------------------------------------//

    private static class KeyboardManager {
        private static Map<OnKeyboardStatusListener, StatusListenerImpl> listeners = new HashMap<>();

        private static InputMethodManager getInputManager() {
            return (InputMethodManager) AppUtil.getApp().getSystemService(Activity.INPUT_METHOD_SERVICE);
        }

        private static boolean isSoftInputShow(Activity activity) {
            return KeyboardManager.getContentViewInvisibleHeight(activity) >= 200;
        }

        private static void fixSoftInputLeaks(final Context context) {
            if (context == null) return;
            InputMethodManager imm =
                    (InputMethodManager) AppUtil.getApp().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) return;
            String[] strArr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
            for (int i = 0; i < 3; i++) {
                try {
                    Field declaredField = imm.getClass().getDeclaredField(strArr[i]);
                    if (declaredField == null) continue;
                    if (!declaredField.isAccessible()) {
                        declaredField.setAccessible(true);
                    }
                    Object obj = declaredField.get(imm);
                    if (obj == null || !(obj instanceof View)) continue;
                    View view = (View) obj;
                    if (view.getContext() == context) {
                        declaredField.set(imm, null);
                    } else {
                        return;
                    }
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }

        private static int getContentViewInvisibleHeight(final Activity activity) {
            final FrameLayout contentView = activity.findViewById(android.R.id.content);
            final View contentViewChild = contentView.getChildAt(0);
            final Rect outRect = new Rect();
            contentViewChild.getWindowVisibleDisplayFrame(outRect);
            return contentViewChild.getBottom() - outRect.bottom;
        }
    }

    private static class AndroidBug5497Impl {
        private int sContentViewInvisibleHeightPre5497;

        private void fixAndroidBug5497(final Activity activity) {
            final int flags = activity.getWindow().getAttributes().flags;
            if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
            final FrameLayout contentView = activity.findViewById(android.R.id.content);
            final View contentViewChild = contentView.getChildAt(0);
            final int paddingBottom = contentViewChild.getPaddingBottom();
            sContentViewInvisibleHeightPre5497 = KeyboardManager.getContentViewInvisibleHeight(activity);
            contentView.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            int height = KeyboardManager.getContentViewInvisibleHeight(activity);
                            if (sContentViewInvisibleHeightPre5497 != height) {
                                contentViewChild.setPadding(
                                        contentViewChild.getPaddingLeft(),
                                        contentViewChild.getPaddingTop(),
                                        contentViewChild.getPaddingRight(),
                                        paddingBottom + height
                                );
                                sContentViewInvisibleHeightPre5497 = height;
                            }
                        }
                    });
        }
    }

    private static class StatusListenerImpl {
        private int sContentViewInvisibleHeightPre;
        private OnKeyboardStatusListener mStatusListener;
        private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

        private void setStatusListener(final Activity activity, final OnKeyboardStatusListener listener) {
            final int flags = activity.getWindow().getAttributes().flags;
            if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
            final FrameLayout contentView = activity.findViewById(android.R.id.content);
            sContentViewInvisibleHeightPre = KeyboardManager.getContentViewInvisibleHeight(activity);
            mStatusListener = listener;
            onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mStatusListener != null) {
                        int height = KeyboardManager.getContentViewInvisibleHeight(activity);
                        if (sContentViewInvisibleHeightPre != height) {
                            mStatusListener.onKeyboardStatus(height >= 200);
                            sContentViewInvisibleHeightPre = height;
                        }
                    }
                }
            };
            contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }

        private void removeStatusListener(Activity activity) {
            final View contentView = activity.findViewById(android.R.id.content);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                contentView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
            } else {
                contentView.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
            }
            mStatusListener = null;
            onGlobalLayoutListener = null;
        }
    }

}