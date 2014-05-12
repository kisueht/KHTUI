package com.kisueht.view.asyncimageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.kisueht.util.KUtil;

public class AsyncKImageView extends RelativeLayout
{
  private Context kContext;
  private ImageView imageView;
  private ProgressBar progressBar;
  private AsyncKImageMemoryBuffer akimb = null;
  private AsyncKImageTask akit = null;

  private String imgUrl = null;
//  private Bitmap iBitmap = null;

  public AsyncKImageView(Context context, AsyncKImageMemoryBuffer asyncKImageMemoryBuffer)
  {
    super(context);
    this.akimb = asyncKImageMemoryBuffer;
    initView();
  }

  public AsyncKImageView(Context context)
  {
    super(context);
    initView();
  }

  private void initView()
  {
    this.kContext = getContext();
    int pad = KUtil.dip2px(this.kContext, 1.0F);
    setPadding(pad, pad, pad, pad);

    this.imageView = new ImageView(this.kContext);
    this.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    RelativeLayout.LayoutParams ivlp = new RelativeLayout.LayoutParams(-2, -2);
    ivlp.addRule(13);
    addView(this.imageView, ivlp);

    this.progressBar = new ProgressBar(this.kContext, null, 16842873);
    this.progressBar.setVisibility(8);
    RelativeLayout.LayoutParams pblp = new RelativeLayout.LayoutParams(-2, -2);
    pblp.addRule(13);
    addView(this.progressBar, pblp);
  }

  public void setMaxWidth(int maxWidth)
  {
    this.imageView.setMaxWidth(maxWidth);
  }

  public void setMaxHeight(int maxHeight)
  {
    this.imageView.setMaxHeight(maxHeight);
  }

  public void setScaleType(ImageView.ScaleType scaleType)
  {
    this.imageView.setScaleType(scaleType);
  }

  public void setImageUrl(String url)
  {
    if (this.akimb != null)
    {
      this.akit = new AsyncKImageTask(this.imageView, this.akimb);
    }
    else this.akit = new AsyncKImageTask(this.imageView);

    this.imgUrl = url;
    loadBitmap(this.imgUrl);
  }

  public void setImageResouce(int arg)
  {
    this.imageView.setImageResource(arg);
  }

  public void setImageDrawable(Drawable arg)
  {
    this.imageView.setImageDrawable(arg);
  }

  public void isShowIndicator(boolean bl)
  {
    int isView = bl ? 0 : 8;
    this.progressBar.setVisibility(isView);
  }

  /*public Bitmap getImageBitmap()
  {
    Bitmap bitmap = this.iBitmap;
    return bitmap;
  }*/

  private void loadBitmap(String url)
  {
    Bitmap bitmap = null;

    if (this.akimb != null)
    {
      bitmap = this.akimb.getBitmapFromCache(url);
      if (bitmap != null)
      {
        this.imageView.setImageBitmap(bitmap);
        this.imageView.invalidate();
        Log.i("KHT", "get data from memory buffer!");
        return;
      }

    }

    getBitmapFromNetwork(url);
  }

  private void getBitmapFromNetwork(String url)
  {
    this.progressBar.setVisibility(0);
    this.akit.setIndicator(this.progressBar);
    Log.i("KHT", "add-->" + getCacheFilePath());
    this.akit.setCachePath(getCacheFilePath());
    this.akit.execute(new String[] { url });
  }

  private String getCacheFilePath()
  {
    if (KUtil.isHasSDcard())
    {
      return KUtil.getSDImgCachePath();
    }
    return this.kContext.getCacheDir().getAbsolutePath();
  }
}