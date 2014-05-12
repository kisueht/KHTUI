package com.kisueht.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

public abstract class HorizalScroller extends ViewGroup
{
  private Scroller mScroller;
  private VelocityTracker mVelocityTracker;
  public int mCurScreen;
  public int mDefaultScreen = 0;
  /*private static final int TOUCH_STATE_REST = 0;
  private static final int TOUCH_STATE_SCROLLING = 1;
  private static final int SNAP_VELOCITY = 600;*/
  private int mTouchState = 0;
  private int mTouchSlop;
  private float mLastMotionX;

  public HorizalScroller(Context context)
  {
    this(context, null);
  }

  public HorizalScroller(Context context, AttributeSet attrs)
  {
    this(context, attrs, 0);
  }

  public HorizalScroller(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);

    this.mScroller = new Scroller(context);
    this.mCurScreen = this.mDefaultScreen;
    this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();	//触发移动事件的最短距离
  }

  protected void onLayout(boolean changed, int l, int t, int r, int b)
  {
    if (changed)
    {
      int childLeft = 0;
      int childCount = getChildCount();

      for (int i = 0; i < childCount; i++)
      {
        View childView = getChildAt(i);
        if (childView.getVisibility() != 8)
        {
          int childWidth = childView.getMeasuredWidth();
          childView.layout(childLeft, 0, childLeft + childWidth, 
            childView.getMeasuredHeight());
          childLeft += childWidth;
        }
      }
    }
  }

  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
  {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int width = View.MeasureSpec.getSize(widthMeasureSpec);
    int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);

    int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);

    if ((widthMode != 1073741824) || 
      (heightMode != 1073741824))
    {
      throw new IllegalStateException(
        "HorizalScroller only can run at EXACTLY mode!");
    }

    int count = getChildCount();
    for (int i = 0; i < count; i++)
    {
      getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
    }

    scrollTo(this.mCurScreen * width, 0);
  }

  public void snapToDestination()
  {
    int screenWidth = getWidth();
    int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
    snapToScreen(destScreen);
  }

  public void snapToScreen(int whichScreen)
  {
    whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

    if (getScrollX() != whichScreen * getWidth())
    {
      int delta = whichScreen * getWidth() - getScrollX();
      this.mScroller.startScroll(getScrollX(), 0, delta, 0, 
        Math.abs(delta) * 2);

      setSnapEvent(this.mCurScreen, whichScreen);
      this.mCurScreen = whichScreen;
      invalidate();
    }
  }

  public abstract void setSnapEvent(int paramInt1, int paramInt2);

  public void setToScreen(int whichScreen)
  {
    whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));

    this.mCurScreen = whichScreen;
  }

  public void computeScroll()
  {
    if (this.mScroller.computeScrollOffset())
    {
      scrollTo(this.mScroller.getCurrX(), this.mScroller.getCurrY());
      postInvalidate();
    }
  }

  public boolean onInterceptTouchEvent(MotionEvent ev)
  {
    int action = ev.getAction();
    if ((action == 2) && 
      (this.mTouchState != 0))
    {
      return true;
    }

    float x = ev.getX();

    switch (action)
    {
    case 2:
      int xDiff = (int)Math.abs(this.mLastMotionX - x);
      if (xDiff > this.mTouchSlop)
      {
        this.mTouchState = 1;
      }
      break;
    case 0:
      this.mLastMotionX = x;
      this.mTouchState = (this.mScroller.isFinished() ? 0 : 
        1);
      break;
    case 1:
      this.mTouchState = 0;
    }

    return this.mTouchState != 0;
  }

  public boolean onTouchEvent(MotionEvent event)
  {
    if (this.mVelocityTracker == null)
    {
      this.mVelocityTracker = VelocityTracker.obtain();
    }
    this.mVelocityTracker.addMovement(event);

    int action = event.getAction();
    float x = event.getX();

    switch (action)
    {
    case 0:
      if (!this.mScroller.isFinished())
      {
        this.mScroller.abortAnimation();
      }
      this.mLastMotionX = x;
      break;
    case 2:
      int deltaX = (int)(this.mLastMotionX - x);
      this.mLastMotionX = x;

      scrollBy(deltaX, 0);
      break;
    case 1:
      VelocityTracker velocityTracker = this.mVelocityTracker;
      velocityTracker.computeCurrentVelocity(1000);
      int velocityX = (int)velocityTracker.getXVelocity();

      if ((velocityX > 600) && (this.mCurScreen > 0))
      {
        snapToScreen(this.mCurScreen - 1);
      } else if ((velocityX < -600) && 
        (this.mCurScreen < getChildCount() - 1))
      {
        snapToScreen(this.mCurScreen + 1);
      }
      else {
        snapToDestination();
      }

      if (this.mVelocityTracker != null)
      {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      }

      this.mTouchState = 0;
      break;
    case 3:
      this.mTouchState = 0;
    }

    return true;
  }
}