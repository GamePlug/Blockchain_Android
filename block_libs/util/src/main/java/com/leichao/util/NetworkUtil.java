package com.leichao.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

public final class NetworkUtil {

    private NetworkUtil() {
    }

    /**
     * 获取当前网络状态
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static NetworkStatus getNetworkStatus() {
        return NetworkManager.getNetworkStatus();
    }

    /**
     * 添加网络状态变化监听
     *
     * @param listener OnStatusListener
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static void addStatusListener(OnNetworkStatusListener listener) {
        NetworkManager.addStatusListener(listener);
    }

    /**
     * 移除网络状态变化监听
     *
     * @param listener OnStatusListener
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static void removeStatusListener(OnNetworkStatusListener listener) {
        NetworkManager.removeStatusListener(listener);
    }

    /**
     * 网络状态
     */
    public static class NetworkStatus {
        public boolean isAvailable;
        public NetworkType networkType;
        public MobileType mobileType;
    }

    /**
     * 网络类型
     */
    public enum NetworkType {
        NETWORK_WIFI,
        NETWORK_MOBILE,
        NETWORK_UNKNOWN,
        NETWORK_NO
    }

    /**
     * 移动网络类型
     */
    public enum MobileType {
        MOBILE_4G,
        MOBILE_3G,
        MOBILE_2G,
        MOBILE_UNKNOWN,
        MOBILE_NO
    }

    /**
     * 网络状态变化回调
     */
    public interface OnNetworkStatusListener {
        void onNetworkStatus(NetworkStatus status);
    }


    //------------------------------------------内部方法---------------------------------------------//

    private static class NetworkManager {
        private static boolean isInitCallback;
        private static NetworkStatus mStatus;
        private static final List<OnNetworkStatusListener> listeners = new ArrayList<>();

        // 添加网络状态变化监听
        @RequiresPermission(ACCESS_NETWORK_STATE)
        private static void addStatusListener(OnNetworkStatusListener listener) {
            // 添加时统一立即回调一次
            mStatus = getNetworkStatus();
            listener.onNetworkStatus(mStatus);
            // 添加到监听器集合
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
            // 初始化网络状态回调
            initStatusCallback();
        }

        // 移除网络状态变化监听
        @RequiresPermission(ACCESS_NETWORK_STATE)
        private static void removeStatusListener(OnNetworkStatusListener listener) {
            listeners.remove(listener);
        }

        @RequiresPermission(ACCESS_NETWORK_STATE)
        private static NetworkStatus getNetworkStatus() {
            NetworkStatus status = new NetworkStatus();
            status.isAvailable = false;
            status.networkType = NetworkType.NETWORK_NO;
            status.mobileType = MobileType.MOBILE_NO;

            ConnectivityManager manager = (ConnectivityManager)
                    AppUtil.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
            if (info != null && info.isAvailable()) {
                status.isAvailable = true;
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    status.networkType = NetworkType.NETWORK_WIFI;
                } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    status.networkType = NetworkType.NETWORK_MOBILE;
                    switch (info.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_GSM:
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN:
                            status.mobileType = MobileType.MOBILE_2G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_TD_SCDMA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        case TelephonyManager.NETWORK_TYPE_EHRPD:
                        case TelephonyManager.NETWORK_TYPE_HSPAP:
                            status.mobileType = MobileType.MOBILE_3G;
                            break;
                        case TelephonyManager.NETWORK_TYPE_IWLAN:
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            status.mobileType = MobileType.MOBILE_4G;
                            break;
                        default:
                            String subtypeName = info.getSubtypeName();
                            if ("TD-SCDMA".equalsIgnoreCase(subtypeName)
                                    || "WCDMA".equalsIgnoreCase(subtypeName)
                                    || "CDMA2000".equalsIgnoreCase(subtypeName)) {
                                status.mobileType = MobileType.MOBILE_3G;
                            } else {
                                status.mobileType = MobileType.MOBILE_UNKNOWN;
                            }
                            break;
                    }
                } else {
                    status.networkType = NetworkType.NETWORK_UNKNOWN;
                }
            }
            return status;
        }

        /**
         * 初始化网络状态变化回调
         *
         * 不使用{@link ConnectivityManager#registerNetworkCallback}方法，原因如下：
         * 1.registerNetworkCallback方法，注册后，有网络状态下会立即回调一次，无网络状态下不会回调，
         *   不一致，需要额外统一处理
         * 2.registerNetworkCallback方法，当手动关闭网络时，会立马回调onLost方法，有小概率出现
         *   此时NetworkInfo网络状态未及时更新，需要稍微延时处理
         *
         * 不使用{@link BroadcastReceiver}静态注册广播，原因：Android 8.0开始静态注册该广播失效
         *
         * 因此使用{@link BroadcastReceiver}动态注册广播
         */
        @SuppressLint("MissingPermission")
        private static void initStatusCallback() {
            if (isInitCallback) {
                return;
            }
            isInitCallback = true;
            BroadcastReceiver netStatusReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    NetworkStatus status = getNetworkStatus();
                    if (mStatus != null && mStatus.isAvailable == status.isAvailable
                            && mStatus.networkType == status.networkType
                            && mStatus.mobileType == status.mobileType) {
                        return;
                    }
                    mStatus = status;
                    for (OnNetworkStatusListener listener : listeners) {
                        listener.onNetworkStatus(mStatus);
                    }
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            AppUtil.getApp().registerReceiver(netStatusReceiver, intentFilter);
        }
    }

}
