package com.kisueht.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 
 * @author kisueht
 *
 */
public class KImageUtil
{
  public static Drawable getImageDrawable(Drawable drawableOrg)
  {
    Drawable dOrg = drawableOrg;
    dOrg.setColorFilter(new ColorMatrixColorFilter(
      new float[] { 1.0F, 0.0F, 0.0F, 0.0F, -50.0F, 0.0F, 1.0F, 0.0F, 0.0F, -50.0F, 0.0F, 0.0F, 1.0F, 0.0F, -50.0F, 0.0F, 0.0F, 0.0F, 1.0F, 0.0F }));
    return dOrg;
  }

  public static Bitmap returnBitMap(String url)
  {
    URL myURL = null;
    Bitmap bitmap = null;
    try
    {
      myURL = new URL(url);
      
      HttpURLConnection conn = (HttpURLConnection)myURL.openConnection();
      conn.setDoInput(true);
      conn.connect();
      InputStream is = conn.getInputStream();
      bitmap = BitmapFactory.decodeStream(is);
      is.close();
    }
    catch (MalformedURLException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
        e.printStackTrace();
    }

    return bitmap;
  }

public static Drawable resizeImage(Context context, Bitmap bitmap, int w, int h)
  {
    Bitmap bitmapOrg = bitmap;

    int imgWidth = bitmapOrg.getHeight();
    int imgHeight = bitmapOrg.getHeight();
    int newWidth = w;
    int newHeight = h;

    float scaleWidth = newWidth / imgWidth;
    float scaleHeight = newHeight / imgHeight;

    Matrix matrix = new Matrix();
    matrix.postScale(scaleWidth, scaleHeight);

    Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, imgWidth, 
      imgHeight, matrix, true);

    return new BitmapDrawable(context.getResources(), resizedBitmap);
  }
}