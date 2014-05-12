package com.kisueht;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;

public class KApplication extends Application {
	
	private UncaughtExceptionHandler mUncaughtExceptionHandler = new UncaughtExceptionHandler() {
		
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			// TODO Auto-generated method stub
			unCaughtException(thread, ex);
		}
	};

	public KApplication() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);
	}
	
	/**
	 * 处理未捕获的全局异常
	 * @param thread
	 * @param ex
	 */
	public void unCaughtException(Thread thread, Throwable ex) {
		
	}

}
