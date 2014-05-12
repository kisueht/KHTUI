package com.kisueht.util;

import android.util.Log;


/**
 * @author HT
 * @see android.util.Log
 *
 */
public class LogUtil {
	public static final String TAG = LogUtil.class.getSimpleName();
	
	private static boolean log_switch = true;

	private LogUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static void isOpen(boolean bl) {
		log_switch = bl;
	}

	public static void v(String tag, String msg) {
		run(Log.VERBOSE, tag, msg, null);
	}
	
	public static void v(String tag, String msg, Throwable tr) {
		run(Log.VERBOSE, tag, msg, tr);
	}
	
	public static void i(String tag, String msg) {
		run(Log.INFO, tag, msg, null);
	}
	
	public static void i(String tag, String msg, Throwable tr) {
		run(Log.INFO, tag, msg, tr);
	}
	
	public static void d(String tag, String msg) {
		run(Log.DEBUG, tag, msg, null);
	}
	
	public static void d(String tag, String msg, Throwable tr) {
		run(Log.DEBUG, tag, msg, tr);
	}
	
	public static void w(String tag, String msg) {
		run(Log.WARN, tag, msg, null);
	}
	
	public static void w(String tag, Throwable tr) {
		run(Log.WARN, tag, null, tr);
	}
	
	public static void w(String tag, String msg, Throwable tr) {
		run(Log.WARN, tag, msg, tr);
	}
	
	public static void e(String tag, String msg) {
		run(Log.ERROR, tag, msg, null);
	}
	
	public static void e(String tag, String msg, Throwable tr) {
		run(Log.ERROR, tag, msg, tr);
	}
	
	private static void run(int key, String tag, String msg, Throwable tr) {
		if (log_switch) {
			switch (key) {
			case Log.VERBOSE:
				Log.v(tag, msg, tr);
				break;
			case Log.INFO:
				Log.i(tag, msg, tr);
				break;
			case Log.DEBUG:
				Log.d(tag, msg, tr);
				break;
			case Log.WARN:
				Log.w(tag, msg, tr);
				break;
			case Log.ERROR:
				Log.e(tag, msg, tr);
				break;
			default:
				break;
			}
		}
	}
}
