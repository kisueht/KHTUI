package com.kisueht.view.asyncimageview;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.kisueht.util.KUtil;

public class AsyncKImageTask extends AsyncTask<String, Object, Bitmap>
{
  private String url;
  private ProgressBar pb = null;
  private String cachePath = null;

  private AsyncKImageMemoryBuffer akimb = null;

  private boolean isLocalBuffer = false;
  private ImageView imageView;
  private AsyncKimgeTaskEvent asyncKimgeTaskEvent = null;

  public AsyncKImageTask(ImageView iv)
  {
    this.imageView = iv;
  }

  public AsyncKImageTask(ImageView iv, AsyncKImageMemoryBuffer asyncKImageMemoryBuffer)
  {
    this.imageView = iv;
    this.akimb = asyncKImageMemoryBuffer;
  }

  public void setIndicator(View v)
  {
    this.pb = ((ProgressBar)v);
  }

  public void setCachePath(String path)
  {
    this.cachePath = path;
  }

  public void setAsyncKimageTaskEvent(AsyncKimgeTaskEvent event)
  {
    this.asyncKimgeTaskEvent = event;
  }

  protected Bitmap doInBackground(String... arg0)
  {
    Log.i("KHT", "cachePath-->" + this.cachePath);
    this.url = arg0[0];
    Log.i("KHT", "url--->" + this.url);

    if (this.url != null)
    {
      File imageFile = null;
      String bitmapName = this.url.substring(this.url.lastIndexOf("/") + 1);
      if (this.cachePath != null)
      {
        File file = new File(this.cachePath);
        FileFilter fileFilter = new FileFilter()
        {
          public boolean accept(File arg0)
          {
            if ((arg0.isFile()) && (arg0.getAbsolutePath().toLowerCase().endsWith(".jpg")))
            {
              return true;
            }
            return false;
          }
        };
        File[] cacheFiles = file.listFiles(fileFilter);
        if ((cacheFiles != null) && (cacheFiles.length > 0))
        {
          for (File f : cacheFiles)
          {
            if (bitmapName.equals(f.getName()))
            {
              imageFile = f;
              break;
            }
          }
        }
      }

      if (imageFile != null)
      {
        Bitmap bmp = BitmapFactory.decodeFile(this.cachePath + "/" + 
          bitmapName);

        Log.i("KHT", "get data from local buffer!");
        this.isLocalBuffer = true;
        updateFileTime(this.cachePath, bitmapName);
        return bmp;
      }
      URL imageUrl = null;
      Bitmap temp_bitmap = null;
      try
      {
        imageUrl = new URL(this.url);
        temp_bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }

      return temp_bitmap;
    }

    return null;
  }

  protected void onPostExecute(Bitmap result)
  {
    Bitmap bit = result;
    if (bit != null)
    {
      this.imageView.setImageBitmap(result);
      if (this.pb != null)
      {
        this.pb.setVisibility(8);
      }

      Log.i("KHT", "bitmap--->" + bit);

      if (this.akimb != null)
      {
        this.akimb.setBitmapToCache(this.url, bit);
      }

      if (!this.isLocalBuffer)
      {
        saveBitmapToSD(bit, this.url);
        new Thread()
        {
          public void run()
          {
            AsyncKImageTask.this.clearLocalBuffer();
          }
        };
      }

      if (this.asyncKimgeTaskEvent != null)
      {
        this.asyncKimgeTaskEvent.onSuccessEvent();
      }
    }
  }

  private void updateFileTime(String dir, String fileName)
  {
    File file = new File(dir, fileName);
    long newModifiedTime = System.currentTimeMillis();
    file.setLastModified(newModifiedTime);
  }

  private void removeExpiredCache(String dir, String fileName)
  {
    File file = new File(dir, fileName);
    if (System.currentTimeMillis() - file.lastModified() > 1000L)
      file.delete();
  }

  private void clearLocalBuffer()
  {
    File file = new File(this.cachePath);
    File[] fs = file.listFiles();
    for (int i = 0; i < fs.length; i++)
    {
      removeExpiredCache(this.cachePath, fs[i].getName());
    }
  }

  private void saveBitmapToSD(Bitmap bm, String url)
  {
    if (bm == null)
    {
      return;
    }

    if (20 > KUtil.getFreeSpaceOnSD())
    {
      return;
    }

    String fileName = url.substring(url.lastIndexOf("/") + 1);
    if (this.cachePath != null)
    {
      try
      {
        File fileDir = new File(this.cachePath);
        if (!fileDir.exists())
        {
          fileDir.mkdirs();
        }
        File outFile = new File(fileDir, fileName);
        outFile.createNewFile();
        OutputStream outStream = new FileOutputStream(outFile);
        bm.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        outStream.flush();
        outStream.close();
        Log.i("KHT", "save to local buffer!");
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static abstract interface AsyncKimgeTaskEvent
  {
    public abstract void onSuccessEvent();
  }
}