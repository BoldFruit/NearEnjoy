package com.example.base.nettype.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import androidx.annotation.Nullable;

/**
 * Time:2020/2/7 20:45
 * Author: han1254
 * Email: 1254763408@qq.com
 * Function:
 */
public class NetTypeUtil {

    private static final String TAG = NetTypeUtil.class.getClass().getSimpleName();
    private static final int NETWORK_TYPE_GSM = 16;
    private static final int NETWORK_TYPE_TD_SCDMA = 17;
    private static final int NETWORK_TYPE_IWLAN = 18;
    private static Application application;

    private NetTypeUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    public static void register(Application app) {
        application = app;
    }


    /**
     * 获取活动网络信息
     *
     * @return NetworkInfo
     */
    private static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * 判断网络是否连接
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return info != null && info.isConnected();
    }

    /**
     * 判断网络是否可用
     * <p>需添加权限 {@code <uses-permission android:name="android.permission.INTERNET"/>}</p>
     * <p>需要异步 ping，如果 ping 不通就说明网络不可用</p>
     * <p>ping 的 ip 为阿里巴巴公共 ip：223.5.5.5</p>
     *
     * @return {@code true}: 可用<br>{@code false}: 不可用
//     */
//    public static boolean isAvailableByPing() {
//        return isAvailableByPing(null);
//    }

    /**
     * 判断网络是否可用
     * <p>需要异步 ping，如果 ping 不通就说明网络不可用</p>
     *
     * @param ip ip 地址（自己服务器 ip），如果为空，ip 为阿里巴巴公共 ip
     * @return {@code true}: 可用<br>{@code false}: 不可用
     */
//    public static boolean isAvailableByPing(String ip) {
//        if (ip == null || ip.length() <= 0) {
//            // 阿里巴巴公共 ip
//            ip = "223.5.5.5";
//        }
//        ShellUtil.CommandResult result = ShellUtil.execCmd(String.format("ping -c 1 %s", ip), false);
//        boolean ret = result.result == 0;
//        if (result.successMsg != null) {
//            LogUtil.d("NetTypeUtil", "isAvailableByPing() called" + result.successMsg);
//        }
//        if (result.errorMsg != null) {
//            LogUtil.d("NetTypeUtil", "isAvailableByPing() called" + result.errorMsg);
//        }
//        return ret;
//    }

    /**
     * 判断 wifi 是否打开
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isWifiEnabled() {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 打开或关闭 wifi
     *
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    public static void setWifiEnabled(boolean enabled) {
        @SuppressLint("WifiManagerLeak")
        WifiManager wifiManager = (WifiManager)application.getSystemService(Context.WIFI_SERVICE);
        if (enabled) {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
        } else {
            if (wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(false);
            }
        }
    }

    /**
     * 判断 wifi 是否连接
     *
     * @return {@code true}: 连接<br>{@code false}: 未连接
     */
    public static boolean isWifiConnected() {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断 wifi 数据是否可用
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
//    public static boolean isWifiAvailable() {
//        return isWifiEnabled() && isAvailableByPing();
//    }

    /**
     * 判断移动数据是否打开
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isMobileDataEnabled() {
        try {
            TelephonyManager tm = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
            Method getMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("getDataEnabled");
            if (getMobileDataEnabledMethod != null) {
                return (boolean) getMobileDataEnabledMethod.invoke(tm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 打开或关闭移动数据
     *
     * @param enabled {@code true}: 打开<br>{@code false}: 关闭
     */
    public static void setMobileDataEnabled(boolean enabled) {
        try {
            TelephonyManager tm = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
            Method setMobileDataEnabledMethod = tm.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
            if (null != setMobileDataEnabledMethod) {
                setMobileDataEnabledMethod.invoke(tm, enabled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取网络运营商名称
     * <p>中国移动、如中国联通、中国电信</p>
     *
     * @return 运营商名称
     */
    public static String getNetworkOperatorName() {
        TelephonyManager tm = (TelephonyManager) application.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getNetworkOperatorName() : null;
    }

    /**
     * 获取当前网络类型
     *
     * @return 网络类型
     * <ul>
     * <li>{@link CurrentNetworkType#NETWORK_WIFI   } </li>
     * <li>{@link CurrentNetworkType#NETWORK_4G     } </li>
     * <li>{@link CurrentNetworkType#NETWORK_3G     } </li>
     * <li>{@link CurrentNetworkType#NETWORK_2G     } </li>
     * <li>{@link CurrentNetworkType#NETWORK_UNKNOWN} </li>
     * <li>{@link CurrentNetworkType#NETWORK_NO     } </li>
     * </ul>
     */
    public static CurrentNetworkType getNetworkType() {
        CurrentNetworkType netType = CurrentNetworkType.NETWORK_NO;
        NetworkInfo info = getActiveNetworkInfo();
        if (info != null && info.isAvailable()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                netType = CurrentNetworkType.NETWORK_WIFI;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {

                    case NETWORK_TYPE_GSM:
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netType = CurrentNetworkType.NETWORK_2G;
                        break;

                    case NETWORK_TYPE_TD_SCDMA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netType = CurrentNetworkType.NETWORK_3G;
                        break;

                    case NETWORK_TYPE_IWLAN:
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netType = CurrentNetworkType.NETWORK_4G;
                        break;
                    default:

                        String subtypeName = info.getSubtypeName();
                        //  中国移动 联通 电信 三种 3G 制式
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA")
                                || subtypeName.equalsIgnoreCase("WCDMA")
                                || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            netType = CurrentNetworkType.NETWORK_3G;
                        } else {
                            netType = CurrentNetworkType.NETWORK_UNKNOWN;
                        }
                        break;
                }
            } else {
                netType = CurrentNetworkType.NETWORK_UNKNOWN;
            }
        }
        return netType;
    }

    /**
     * 通过 wifi 获取本地 IP 地址
     *
     * @return IP 地址
     */
    public static String getIpAddressByWifi() {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) application.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获取 IP 地址
     *
     * @param useIPv4 是否用 IPv4
     * @return IP 地址
     */
    public static String getIPAddress(boolean useIPv4) {
        try {
            for (Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces(); nis.hasMoreElements(); ) {
                NetworkInterface ni = nis.nextElement();
                // 防止小米手机返回 10.0.2.15
                if (!ni.isUp()) continue;
                for (Enumeration<InetAddress> addresses = ni.getInetAddresses(); addresses.hasMoreElements(); ) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        boolean isIPv4 = hostAddress.indexOf(':') < 0;
                        if (useIPv4) {
                            if (isIPv4) return hostAddress;
                        } else {
                            if (!isIPv4) {
                                int index = hostAddress.indexOf('%');
                                return index < 0 ? hostAddress.toUpperCase() : hostAddress.substring(0, index).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取域名 IP 地址
     *
     * @param domain 域名
     * @return IP 地址
     */
    public static String getDomainAddress(@Nullable String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum CurrentNetworkType {
        // wifi
        NETWORK_WIFI,
        // 4G 网
        NETWORK_4G,
        // 3G 网
        NETWORK_3G,
        // 2G 网
        NETWORK_2G,
        // 未知网络
        NETWORK_UNKNOWN,
        // 没有网络
        NETWORK_NO
    }


}

