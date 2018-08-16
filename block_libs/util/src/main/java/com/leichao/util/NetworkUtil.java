package com.leichao.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
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
    public static void addStatusListener(OnStatusListener listener) {
        NetworkManager.addStatusListener(listener);
    }

    /**
     * 移除网络状态变化监听
     *
     * @param listener OnStatusListener
     */
    @RequiresPermission(ACCESS_NETWORK_STATE)
    public static void removeStatusListener(OnStatusListener listener) {
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
    public interface OnStatusListener {
        void onStatus(NetworkStatus status);
    }


    //------------------------------------------内部方法---------------------------------------------//

    private static class NetworkManager {
        private static boolean isInitCallback;
        private static NetworkStatus mStatus;
        private static Handler handler = new Handler(Looper.getMainLooper());
        private static final List<OnStatusListener> listeners = new ArrayList<>();

        // 添加网络状态变化监听
        @RequiresPermission(ACCESS_NETWORK_STATE)
        private static void addStatusListener(OnStatusListener listener) {
            initStatusCallback();
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }

        // 移除网络状态变化监听
        private static void removeStatusListener(OnStatusListener listener) {
            listeners.remove(listener);
        }

        @RequiresPermission(ACCESS_NETWORK_STATE)
        private static NetworkStatus getNetworkStatus() {
            NetworkStatus status = new NetworkStatus();
            status.isAvailable = false;
            status.networkType = NetworkType.NETWORK_NO;
            status.mobileType = MobileType.MOBILE_NO;

            ConnectivityManager manager = getConnectivityManager();
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

        private static ConnectivityManager getConnectivityManager() {
            return (ConnectivityManager) AppUtil.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        }

        @SuppressLint("MissingPermission")
        private static void statusChange() {
            // post到主线程执行
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    NetworkStatus status = NetworkManager.getNetworkStatus();
                    if (mStatus != null && mStatus.isAvailable == status.isAvailable
                            && mStatus.networkType == status.networkType
                            && mStatus.mobileType == status.mobileType) {
                        return;
                    }
                    mStatus = status;
                    for (OnStatusListener listener : NetworkManager.listeners) {
                        listener.onStatus(mStatus);
                    }
                }
            }, 100);// 延时是因为NetworkCallback的onLost被调用时，可能NetworkStatus还未及时改变
        }

        @RequiresPermission(ACCESS_NETWORK_STATE)
        private static void initStatusCallback() {
            if (isInitCallback) {
                return;
            }
            isInitCallback = true;
            ConnectivityManager manager = getConnectivityManager();
            if (manager != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // 设置网络状态变化回调
                manager.registerNetworkCallback(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        statusChange();
                    }

                    @Override
                    public void onLost(Network network) {
                        statusChange();
                    }
                });
            } else {
                // 注册网络状态变化广播接收者
                BroadcastReceiver netStatusReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        statusChange();
                    }
                };
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
                AppUtil.getApp().registerReceiver(netStatusReceiver, intentFilter);
            }
        }
    }

}
