package com.kisueht.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings.System;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public final class KUtil {
	public final static int WRAP = ViewGroup.LayoutParams.WRAP_CONTENT;
	@SuppressWarnings("deprecation")
	public final static int FILL = ViewGroup.LayoutParams.FILL_PARENT;

	public static int dip2px(Context context, float dipValue) {
		return dip2px(context, dipValue, false);
	}

	public static int dip2px(Context context, float dipValue, boolean isHeight) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);

		int iRet = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dipValue, metric);

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2.0D);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2.0D);
		double screenInches = Math.sqrt(x + y);
		iRet = (int) (iRet * screenInches / 3.6D);

		if (isHeight) {
			int height_for_320 = (int) ((320.0D * metric.heightPixels) / (480.0D * metric.widthPixels));

			iRet = iRet * height_for_320;
		}

		return iRet;
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);

		return (int) TypedValue.applyDimension(2, spValue, metric);
	}

	public static int[] getWidthAndHeight(Context context) {
		Activity act = (Activity) context;

		Rect rect = new Rect();
		act.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
		return new int[] { rect.width(), rect.height() };
	}

	/**
	 * check if SDcard exist or not
	 * 
	 * @return
	 */
	public static boolean isHasSDcard() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	public static String getSDImgCachePath() {
		return getSDImgCachePath("/KImageCache");
	}
	
	public static String getSDImgCachePath(String dir_name) {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/" +dir_name;
	}

	@SuppressWarnings("deprecation")
	public static int getFreeSpaceOnSD() {
		int MB = 1024*1024;
		StatFs sFs = new StatFs(Environment.getExternalStorageDirectory()
				.getPath());
		double sdFreeMB = (sFs.getAvailableBlocks() * sFs.getBlockSize()) / MB;
		return (int) sdFreeMB;
	}

	public static void removeLocalCache(String localDirPath) {
		File dir = new File(localDirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}

		int dirSize = 0;
		for (File file : files) {
			dirSize = (int) (dirSize + file.length());
		}

		if (dirSize / 1024 / 1024 > 10) {
			int removeFactor = (int) (0.4F * files.length + 1.0F);

			Arrays.sort(files, new Comparator<File>() {
				public int compare(File arg0, File arg1) {
					if (arg0.lastModified() > arg1.lastModified()) {
						return 1;
					}
					if (arg0.lastModified() == arg1.lastModified()) {
						return 0;
					}
					return -1;
				}
			});
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}
		}
	}

	public static void removeSDCache(String SDdirPath) {
		File dir = new File(SDdirPath);
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}

		int dirSize = 0;
		for (File file : files) {
			dirSize = (int) (dirSize + file.length());
		}

		int cache_size = 40;
		if ((dirSize / 1024 / 1024 > cache_size)
				|| (cache_size > getFreeSpaceOnSD() / 1024)) {
			int removeFactor = (int) (0.4F * files.length + 1.0F);

			Arrays.sort(files, new Comparator<File>() {
				public int compare(File arg0, File arg1) {
					if (arg0.lastModified() > arg1.lastModified()) {
						return 1;
					}
					if (arg0.lastModified() == arg1.lastModified()) {
						return 0;
					}
					return -1;
				}
			});
			for (int i = 0; i < removeFactor; i++) {
				files[i].delete();
			}
		}
	}

	/**
	 * get bitmap resoures from network
	 * 
	 * @param url
	 * @return
	 */
	/*
	 * public static Bitmap returnBitMap(String url) { URL myURL = null; Bitmap
	 * bitmap = null;
	 * 
	 * try { myURL = new URL(url); } catch (MalformedURLException e) {
	 * e.printStackTrace(); }
	 * 
	 * try { HttpURLConnection conn = (HttpURLConnection)
	 * myURL.openConnection(); conn.setDoInput(true); conn.connect();
	 * InputStream is = conn.getInputStream(); bitmap =
	 * BitmapFactory.decodeStream(is); is.close(); } catch (IOException e) {
	 * e.printStackTrace(); }
	 * 
	 * return bitmap; }
	 *//**
	 * reset image size
	 * 
	 * @param bitmap
	 * @param w
	 *            newWidth
	 * @param h
	 *            newHeight
	 * @return
	 */
	/*
	 * public static Drawable resizeImage(Bitmap bitmap, int w, int h) { Bitmap
	 * bitmapOrg = bitmap;
	 * 
	 * int imgWidth = bitmapOrg.getHeight(); int imgHeight =
	 * bitmapOrg.getHeight(); int newWidth = w; int newHeight = h;
	 * 
	 * float scaleWidth = ((float)newWidth)/imgWidth; float scaleHeight =
	 * ((float)newHeight)/imgHeight;
	 * 
	 * Matrix matrix = new Matrix(); matrix.postScale(scaleWidth, scaleHeight);
	 * //matrix.postRotate(45);// if you want to rotate the Bitmap
	 * 
	 * Bitmap resizedBitmap =
	 * Bitmap.createBitmap(bitmapOrg,0,0,imgWidth,imgHeight,matrix,true);
	 * 
	 * return new BitmapDrawable(resizedBitmap); }
	 */

	/**
	 * get the Android system time format is 24 hours or 12 hours of system
	 * 
	 * @param context
	 * @return
	 */
	public static boolean is24Hours(Context context) {
		ContentResolver crl = context.getContentResolver();
		String strTimeFormat = System.getString(crl, System.TIME_12_24);

		if (strTimeFormat.equals("24")) {
			return true;
		} else {
			return false;
		}
	}

	public static Drawable getDrawableFromAssets(Context context, String name) {
		return getDrawableFromAssets(context, name, false);
	}

	public static Drawable getDrawableFromAssets(Context context, String name,
			boolean isXML) {
		Drawable mDrawable = null;

		try {
			InputStream is = null;
			if (isXML) {
				is = context.getAssets().openFd(name).createInputStream();
			} else {
				is = context.getAssets().open(name);
			}

			if (null != is) {
				if (name.endsWith(".9.png")) {
					Resources res = context.getResources();
					Bitmap bitmap = BitmapFactory.decodeStream(is);
					byte[] chunk = bitmap.getNinePatchChunk();

					Rect mRect = new Rect();
					mDrawable = new NinePatchDrawable(res, bitmap, chunk,
							mRect, name);
				} else {
					mDrawable = Drawable.createFromStream(is, name);
				}

				is.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mDrawable;
	}

	public static void hideSoftInput(View v) {
		if (v != null) {
			InputMethodManager imm = (InputMethodManager) v.getContext()
					.getSystemService("input_method");
			if (imm.isActive()) {
				IBinder iBinder = v.getWindowToken();
				if (iBinder != null) {
					if (!imm.hideSoftInputFromWindow(iBinder, 2)) {
						imm.hideSoftInputFromInputMethod(iBinder, 2);
					}
				}
			}
		}
	}

	public static void hideSoftInput(Context context) {
		if (context != null) {
			InputMethodManager imm = (InputMethodManager) context
					.getSystemService("input_method");
			if (imm.isActive()) {
				Activity activity = (Activity) context;
				View v = activity.getCurrentFocus();
				if (v != null) {
					IBinder iBinder = v.getWindowToken();
					if (iBinder != null) {
						if (!imm.hideSoftInputFromWindow(iBinder, 2)) {
							imm.hideSoftInputFromInputMethod(iBinder, 2);
						}
					}
				}
			}
		}
	}

	/*
	 * public static void showSoftInput(View v) { final View mView = v; if
	 * (mView != null) { mView.requestFocus(); new Timer().schedule(new
	 * TimerTask() { public void run() { Context context = mView.getContext();
	 * InputMethodManager imm =
	 * (InputMethodManager)context.getSystemService(Context
	 * .INPUT_METHOD_SERVICE); if (!imm.isActive(mView)) {
	 * imm.showSoftInput(mView, 2);
	 * imm.toggleSoftInputFromWindow(mView.getWindowToken(), 2, 2); } } } ,
	 * 300L); } }
	 */

	public static void showSoftInput(final View v) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				v.dispatchTouchEvent(MotionEvent.obtain(
						SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						MotionEvent.ACTION_DOWN, 0.0F, 0.0F, 0));
				v.dispatchTouchEvent(MotionEvent.obtain(
						SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
						MotionEvent.ACTION_UP, 0.0F, 0.0F, 0));
			}
		}, 300L);
	}

}
