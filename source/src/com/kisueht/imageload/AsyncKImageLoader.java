/**
 * 
 */
package com.kisueht.imageload;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.kisueht.util.KUtil;

/**
 * @author kisueht
 * 
 */
public class AsyncKImageLoader {

	private static AsyncKImageLoader mAsyncKImageLoader = null;
	
	private Context context = null;
	
	private String cachePath = null;
	
	private float sd_cache_max_Mb = 20f;
	private long expired_cache_time_millis = 7*24*60*60*1000l;//7 days

	public class KImageHolder {
		String url = null;
		ImageView imageView = null;
		Bitmap bitmap = null;
		AsyncKImageTask imageTask = null;
	}

	/**
	 * 
	 */
	private AsyncKImageLoader() {
		// TODO Auto-generated constructor stub
	}

	public static AsyncKImageLoader getInstance() {
		if (null == mAsyncKImageLoader) {
			mAsyncKImageLoader = new AsyncKImageLoader();
		}
		return mAsyncKImageLoader;
	}

	public void load(ImageView iv, String img_url) {
		KImageHolder kImageHolder = new KImageHolder();
		kImageHolder.imageView = iv;
		kImageHolder.url = img_url;
		kImageHolder.imageTask = new AsyncKImageTask();
		
		context = iv.getContext();

		loadFromMemory(kImageHolder);
	}

	private void loadFromMemory(KImageHolder kImageHolder) {
		Bitmap bitmap = null;

		bitmap = getBitmapFromCache(kImageHolder.url);
		if (bitmap != null) {
			kImageHolder.imageView.setImageBitmap(bitmap);
			kImageHolder.imageView.invalidate();
			return;
		}

		loadFromTask(kImageHolder);
	}

	private void loadFromTask(KImageHolder kImageHolder) {
		kImageHolder.imageTask.execute(kImageHolder);
	}
	

	private static final int IMG_CACHE_CAPACITY_Mb = 20;
	private final HashMap<String, Bitmap> imageCacheA = new LinkedHashMap<String, Bitmap>(
			15, 0.75F, true) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(Map.Entry<String, Bitmap> eldest) {
			if (size() > IMG_CACHE_CAPACITY_Mb) {
				imagecacheB.put((String) eldest.getKey(),
						new SoftReference<Bitmap>((Bitmap) eldest.getValue()));
				return true;
			}
			return false;
		}
	};

	private HashMap<String, SoftReference<Bitmap>> imagecacheB = new HashMap<String, SoftReference<Bitmap>>();

	public void setBitmapToCache(String imgUrl, Bitmap bit) {
		synchronized (imageCacheA) {
			this.imageCacheA.put(imgUrl, bit);
		}
	}

	public Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = (Bitmap) this.imageCacheA.get(url);
		if (bitmap != null) {
			this.imageCacheA.remove(url);
			this.imageCacheA.put(url, bitmap);
			return bitmap;
		}
		
		SoftReference<Bitmap> bitmapReference = (SoftReference<Bitmap>) this.imagecacheB
				.get(url);
		if (bitmapReference != null) {
			bitmap = (Bitmap) bitmapReference.get();
			if (bitmap != null) {
				return bitmap;
			}
			this.imagecacheB.remove(url);
		}

		return null;
	}
	
	private class AsyncKImageTask extends AsyncTask<KImageHolder, Void, KImageHolder>{

		@Override
		protected KImageHolder doInBackground(KImageHolder... params) {
			// TODO Auto-generated method stub
			KImageHolder kImageHolder = params[0];

			String url = kImageHolder.url;
			if (url != null) {
				File imageFile = null;
				String bitmapName = url.substring(url.lastIndexOf("/") + 1);
				cachePath = getCachePath(kImageHolder);
				if (cachePath != null) {
					File file = new File(cachePath);
					FileFilter fileFilter = new FileFilter() {
						public boolean accept(File arg0) {
							String file_low = arg0.getAbsolutePath().toLowerCase(Locale.getDefault());
							if ((arg0.isFile())
									&& ((file_low.endsWith(".jpg")) || file_low.endsWith(".png"))) {
								return true;
							}
							return false;
						}
					};
					File[] cacheFiles = file.listFiles(fileFilter);
					if ((cacheFiles != null) && (cacheFiles.length > 0)) {
						for (File f : cacheFiles) {
							if (bitmapName.equals(f.getName())) {
								imageFile = f;
								break;
							}
						}
					}
				}

				Bitmap bmp = null;
				if (imageFile != null) {
					bmp = BitmapFactory.decodeFile(cachePath + "/"
							+ bitmapName);

					if (null!=bmp) {
						updateFileTime(cachePath, bitmapName);
						setBitmapToCache(url, bmp);
						kImageHolder.bitmap = bmp;
						
						return kImageHolder;
					}
				} 
				
				URL imageUrl = null;
				try {
					imageUrl = new URL(url);
					bmp = BitmapFactory.decodeStream(imageUrl
							.openStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if (null!=bmp) {
					kImageHolder.bitmap = bmp;
					setBitmapToCache(url, bmp);
					saveBitmapToSD(bmp, url);
					clearLocalBuffer();
				}
			}

			return kImageHolder;
		}

		@Override
		protected void onPostExecute(KImageHolder result) {
			// TODO Auto-generated method stub
			if (null != result) {
				result.imageView.setImageBitmap(result.bitmap);
				result.imageView.invalidate();
			}
			
			this.cancel(false);
		}
		
	}

	private void updateFileTime(String dir, String fileName) {
		File file = new File(dir, fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	private void removeExpiredCache(String dir, String fileName) {
		File file = new File(dir, fileName);
		if (System.currentTimeMillis() - file.lastModified() > expired_cache_time_millis)
			file.delete();
	}

	private void clearLocalBuffer() {
		File file = new File(this.cachePath);
		File[] fs = file.listFiles();
		if (null!=fs) {
			for (int i = 0; i < fs.length; i++) {
				removeExpiredCache(this.cachePath, fs[i].getName());
			}
		}
	}

	@SuppressWarnings("resource")
	@SuppressLint("DefaultLocale")
	private void saveBitmapToSD(Bitmap bm, String url) {
		if (bm == null) {
			try {
				throw new Exception("bitmap is null!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		if (sd_cache_max_Mb > KUtil.getFreeSpaceOnSD()) {
			try {
				throw new Exception("The free space on sd card is not enough!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}

		String fileName = url.substring(url.lastIndexOf("/") + 1);
		if (cachePath != null) {
			File fileDir = new File(cachePath);
			if (!fileDir.exists()) {
				if (!fileDir.mkdirs()) {
					try {
						throw new Exception("Make dirs failed!");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}
			}
			
			File outFile = new File(fileDir, fileName);
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			OutputStream outStream = null;
			try {
				outStream = new FileOutputStream(outFile);
				String file_low = fileName.toLowerCase();
				CompressFormat compressFormat = getCompressFormat(file_low.substring(file_low.lastIndexOf(".")+1));
				if (null!=compressFormat) {
					bm.compress(compressFormat, 100, outStream);
				}else {
					throw new Exception("The image format "+file_low.substring(file_low.lastIndexOf(".")+1)+"is not surported!");
				}
				
				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
	
	@SuppressLint("NewApi")
	private CompressFormat getCompressFormat(String f_format) {		
		if (null!=f_format) {
			if ("jpg".equals(f_format)) {
				return CompressFormat.JPEG;
			}else if ("png".equals(f_format)) {
				return CompressFormat.PNG;
			}else if ("webp".equals(f_format)) {
				return CompressFormat.WEBP;
			}
		}
		
		return null;
	}
	
	private String getCachePath(KImageHolder kImageHolder) {
		if (KUtil.isHasSDcard())
	    {
			String pack_name = context.getPackageName();
	      return KUtil.getSDImgCachePath(pack_name.substring(pack_name.lastIndexOf(".")+1)+"/IMG");
	    }
	    return context.getCacheDir().getAbsolutePath()+"/IMG";
	}

}
