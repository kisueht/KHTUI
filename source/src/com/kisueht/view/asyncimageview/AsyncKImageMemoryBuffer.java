package com.kisueht.view.asyncimageview;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.Bitmap;
import android.util.Log;

public class AsyncKImageMemoryBuffer
{
  private static final int IMG_CACHE_CAPACITY = 30;
  private final HashMap<String, Bitmap> imageCacheA = new LinkedHashMap<String, Bitmap>(
    15, 0.75F, true)
  {
    private static final long serialVersionUID = 1L;

    protected boolean removeEldestEntry(Map.Entry<String, Bitmap> eldest)
    {
      if (size() > IMG_CACHE_CAPACITY)
      {
        AsyncKImageMemoryBuffer.this.imagecacheB.put((String)eldest.getKey(), new SoftReference<Bitmap>((Bitmap)eldest.getValue()));
        return true;
      }
      return false;
    }
  };

  private HashMap<String, SoftReference<Bitmap>> imagecacheB = new HashMap<String, SoftReference<Bitmap>>();

  public void setBitmapToCache(String imgUrl, Bitmap bit)
  {
    this.imageCacheA.put(imgUrl, bit);
  }

  public Bitmap getBitmapFromCache(String url)
  {
    synchronized (this.imageCacheA)
    {
      Bitmap bitmap = (Bitmap)this.imageCacheA.get(url);
      if (bitmap != null)
      {
        this.imageCacheA.remove(url);
        this.imageCacheA.put(url, bitmap);
        return bitmap;
      }
      Log.i("KHT", "imageCacheA--->" + this.imageCacheA.size() + ";" + this.imageCacheA.isEmpty());
    }
    
    SoftReference<Bitmap> bitmapReference = (SoftReference<Bitmap>)this.imagecacheB.get(url);
    if (bitmapReference != null)
    {
      Bitmap bitmap = (Bitmap)bitmapReference.get();
      if (bitmap != null)
      {
        return bitmap;
      }
      this.imagecacheB.remove(url);
    }

    return null;
  }
}