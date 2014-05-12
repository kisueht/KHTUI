/**
 * 
 */
package com.kisueht.network.asynchttp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author kisueht
 *
 */
public class NetworkUtil {
	
	/** Network type is unknown */
	public static final int NETWORK_TYPE_UNKNOWN = 0;
	/** Current network is GPRS */
	public static final int NETWORK_TYPE_GPRS = 1;
	/** Current network is EDGE */
	public static final int NETWORK_TYPE_EDGE = 2;
	/** Current network is UMTS */
	public static final int NETWORK_TYPE_UMTS = 3;
	/** Current network is CDMA: Either IS95A or IS95B */
	public static final int NETWORK_TYPE_CDMA = 4;
	/** Current network is EVDO revision 0 */
	public static final int NETWORK_TYPE_EVDO_0 = 5;
	/** Current network is EVDO revision A */
	public static final int NETWORK_TYPE_EVDO_A = 6;
	/** Current network is 1xRTT */
	public static final int NETWORK_TYPE_1xRTT = 7;
	/** Current network is HSDPA */
	public static final int NETWORK_TYPE_HSDPA = 8;
	/** Current network is HSUPA */
	public static final int NETWORK_TYPE_HSUPA = 9;
	/** Current network is HSPA */
	public static final int NETWORK_TYPE_HSPA = 10;
	/** Current network is iDen */
	public static final int NETWORK_TYPE_IDEN = 11;
	/** Current network is EVDO revision B */
	public static final int NETWORK_TYPE_EVDO_B = 12;
	/** Current network is LTE */
	public static final int NETWORK_TYPE_LTE = 13;
	/** Current network is eHRPD */
	public static final int NETWORK_TYPE_EHRPD = 14;
	/** Current network is HSPA+ */
	public static final int NETWORK_TYPE_HSPAP = 15;

	/** Unknown network class. {@hide} */
	public static final int NETWORK_CLASS_UNKNOWN = 0;
	public static final int NETWORK_CLASS_2_G = 1;
	public static final int NETWORK_CLASS_3_G = 2;
	public static final int NETWORK_CLASS_4_G = 3;
	public static final int NETWORK_CLASS_Wifi = 4;

	public static final int MOBILE_TYPE_PHONE = 1;
	public static final int MOBILE_TYPE_PAD = 2;

	/**
	 * 
	 */
	private NetworkUtil() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 是否已经连接网络
	 * @param context
	 * @return
	 */
	public static boolean isNetworkEnable(Context context) {
		return getNetworkType(context) == NETWORK_CLASS_UNKNOWN ? false : true;
	}

	/**
	 * 获取网络类型
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobileNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (activeNetInfo == null) {
			return NETWORK_CLASS_UNKNOWN;
		} else if (wifiNetInfo != null && wifiNetInfo.isConnected()) {
			return NETWORK_CLASS_Wifi;
		} else if (mobileNetInfo != null && mobileNetInfo.isConnected()) {
			return getNetworkClass(mobileNetInfo.getSubtype());
		} else {
			return NETWORK_CLASS_UNKNOWN;
		}
	}
	
	/**
	 * 获取网络制式
	 * @param networkType
	 * @return
	 */
	public static int getNetworkClass(int networkType) {
		switch (networkType) {
		case NETWORK_TYPE_GPRS:
		case NETWORK_TYPE_EDGE:
		case NETWORK_TYPE_CDMA:
		case NETWORK_TYPE_1xRTT:
		case NETWORK_TYPE_IDEN:
			return NETWORK_CLASS_2_G;
		case NETWORK_TYPE_UMTS:
		case NETWORK_TYPE_EVDO_0:
		case NETWORK_TYPE_EVDO_A:
		case NETWORK_TYPE_HSDPA:
		case NETWORK_TYPE_HSUPA:
		case NETWORK_TYPE_HSPA:
		case NETWORK_TYPE_EVDO_B:
		case NETWORK_TYPE_EHRPD:
		case NETWORK_TYPE_HSPAP:
			return NETWORK_CLASS_3_G;
		case NETWORK_TYPE_LTE:
			return NETWORK_CLASS_4_G;
		default:
			return NETWORK_CLASS_UNKNOWN;
		}
	}
}
