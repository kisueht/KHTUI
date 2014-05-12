/*
 * Copyright 2011 woozzu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kisueht.view.roundlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kisueht.util.KUtil;

public class RefreshPullListView extends RoundListView {

    private LinearLayout mHeaderContainer = null;
    private RelativeLayout mHeaderView = null;
    private ImageView mArrow = null;
    private ProgressBar mProgress = null;
    private TextView mText = null;
    private float mY = 0;
    private float mHistoricalY = 0;
    private int mHistoricalTop = 0;
    private int mInitialHeight = 0;
    private boolean mFlag = false;
    private boolean mArrowUp = false;
    private boolean mIsRefreshing = false;
    private int mHeaderHeight = 0;
    private OnRefreshListener mListener = null;

    private static final int REFRESH = 0;
    private static final int NORMAL = 1;
    private static final int HEADER_HEIGHT_DP = 62;
    public static final String TAG = RefreshPullListView.class.getSimpleName();

    public RefreshPullListView(final Context context) {
        super(context);
        initialize();
    }

    public RefreshPullListView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public RefreshPullListView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public void setOnRefreshListener(final OnRefreshListener l) {
        mListener = l;
    }

    public void completeRefreshing() {
        mProgress.setVisibility(View.INVISIBLE);
        mArrow.setVisibility(View.VISIBLE);
        mHandler.sendMessage(mHandler.obtainMessage(NORMAL, mHeaderHeight, 0));
        mIsRefreshing = false;
        invalidateViews();
//        mRoundListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onInterceptTouchEvent(final MotionEvent ev) {
    	Log.i(TAG, "onInterceptTouchEvent");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHandler.removeMessages(REFRESH);
                mHandler.removeMessages(NORMAL);
                mY = mHistoricalY = ev.getY();
                if (mHeaderContainer.getLayoutParams() != null) {
                    mInitialHeight = mHeaderContainer.getLayoutParams().height;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent ev) {
    	Log.i(TAG, "onTouchEvent");
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mHistoricalTop = getChildAt(0).getTop();
                break;
            case MotionEvent.ACTION_UP:
                if (!mIsRefreshing) {
                    if (mArrowUp) {
                        startRefreshing();
                        mHandler.sendMessage(mHandler.obtainMessage(REFRESH, (int) (ev.getY() - mY)
                                / 2 + mInitialHeight, 0));
                    } else {
                        if (getChildAt(0).getTop() == KUtil.dip2px(getContext(), pad)) {
                            mHandler.sendMessage(mHandler.obtainMessage(NORMAL,
                                    (int) (ev.getY() - mY) / 2 + mInitialHeight, 0));
                        }
                    }
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(REFRESH, (int) (ev.getY() - mY) / 2
                            + mInitialHeight, 0));
                }
                mFlag = false;
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
    	Log.i(TAG, "dispatchTouchEvent");
        if (ev.getAction() == MotionEvent.ACTION_MOVE && getFirstVisiblePosition() == 0) {
            float direction = ev.getY() - mHistoricalY;
            int height = (int) (ev.getY() - mY) / 2 + mInitialHeight;
            if (height < 0) {
                height = 0;
            }

            float deltaY = Math.abs(mY - ev.getY());
            ViewConfiguration config = ViewConfiguration.get(getContext());
            if (deltaY > config.getScaledTouchSlop()) {

                // Scrolling downward
                if (direction > 0) {
                    // Refresh bar is extended if top pixel of the first item is
                    // visible
                    if (getChildAt(0).getTop() == KUtil.dip2px(getContext(), pad)) {
                        if (mHistoricalTop < 0) {

                            // mY = ev.getY(); // TODO works without
                            // this?mHistoricalTop = 0;
                        }

                        // Extends refresh bar
                        setHeaderHeight(height);

                        // Stop list scroll to prevent the list from
                        // overscrolling
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        mFlag = false;
                    }
                } else if (direction < 0) {
                    // Scrolling upward

                    // Refresh bar is shortened if top pixel of the first item
                    // is
                    // visible
                    if (getChildAt(0).getTop() == KUtil.dip2px(getContext(), pad)) {
                        setHeaderHeight(height);

                        // If scroll reaches top of the list, list scroll is
                        // enabled
                        if (getChildAt(1) != null && getChildAt(1).getTop() <= 1 && !mFlag) {
                            ev.setAction(MotionEvent.ACTION_DOWN);
                            mFlag = true;
                        }
                    }
                }
            }

            mHistoricalY = ev.getY();
        }
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean performItemClick(final View view, final int position, final long id) {
        if (position == 0) {
            // This is the refresh header element
            return true;
        } else {
            return super.performItemClick(view, position - 1, id);
        }
    }

    private void initialize() {
        addHeaderView(getHeaderContainerView());

        mHeaderHeight = (int) (HEADER_HEIGHT_DP * getContext().getResources().getDisplayMetrics().density);
        setHeaderHeight(0);
    }
    
    private View getHeaderContainerView() {
		mHeaderContainer = new LinearLayout(getContext());
		
		mHeaderView = new RelativeLayout(getContext());
		int pad = KUtil.dip2px(getContext(), 10);
		mHeaderView.setPadding(0, pad, 0, pad);
		mHeaderContainer.addView(mHeaderView, new LinearLayout.LayoutParams(KUtil.FILL, KUtil.WRAP));
		
		mArrow = new ImageView(getContext());
		mArrow.setImageDrawable(KUtil.getDrawableFromAssets(getContext(), "refreshable_listview_arrow.png"));
		RelativeLayout.LayoutParams malp = new RelativeLayout.LayoutParams(KUtil.WRAP, KUtil.WRAP);
		malp.leftMargin = KUtil.dip2px(getContext(), 25);
		mHeaderView.addView(mArrow, malp);
		
		mProgress = new ProgressBar(getContext());
		mProgress.setVisibility(View.INVISIBLE);
		int w_h = KUtil.dip2px(getContext(), 20);
		RelativeLayout.LayoutParams mplp = new RelativeLayout.LayoutParams(w_h, w_h);
		mplp.leftMargin = KUtil.dip2px(getContext(), 25);
		mplp.addRule(RelativeLayout.CENTER_VERTICAL);
		mHeaderView.addView(mProgress, mplp);
		
		mText = new TextView(getContext());
		mText.setText("下拉刷新");
		RelativeLayout.LayoutParams mtlp = new RelativeLayout.LayoutParams(KUtil.WRAP, KUtil.WRAP);
		mtlp.leftMargin = KUtil.dip2px(getContext(), 15);
		mtlp.addRule(RelativeLayout.CENTER_IN_PARENT);
		mHeaderView.addView(mText, mtlp);
		
		return mHeaderContainer;
	}

    private void setHeaderHeight(final int height) {
        if (height <= 1) {
            mHeaderView.setVisibility(View.GONE);
        } else {
            mHeaderView.setVisibility(View.VISIBLE);
        }

        // Extends refresh bar
        LayoutParams lp = (LayoutParams) mHeaderContainer.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(KUtil.FILL, KUtil.WRAP);
        }
        lp.height = height;
        mHeaderContainer.setLayoutParams(lp);

        // Refresh bar shows up from bottom to top
        LinearLayout.LayoutParams headerLp = (LinearLayout.LayoutParams) mHeaderView
                .getLayoutParams();
        if (headerLp == null) {
            headerLp = new LinearLayout.LayoutParams(KUtil.FILL,
                    KUtil.WRAP);
        }
        headerLp.topMargin = -mHeaderHeight + height;
        mHeaderView.setLayoutParams(headerLp);

        if (!mIsRefreshing) {
            // If scroll reaches the trigger line, start refreshing
            if (height > mHeaderHeight && !mArrowUp) {
                mArrow.startAnimation(getRotateAnimation());
                mText.setText("松开刷新");
                rotateArrow();
                mArrowUp = true;
            } else if (height < mHeaderHeight && mArrowUp) {
                mArrow.startAnimation(getRotateAnimation());
                mText.setText("下拉刷新");
                rotateArrow();
                mArrowUp = false;
            }
        }
    }
    
    private Animation getRotateAnimation() {
		RotateAnimation rotateAnimation = new RotateAnimation(10.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotateAnimation.setDuration(200);
		
		return rotateAnimation;
	}

    private void rotateArrow() {
        Drawable drawable = mArrow.getDrawable();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        canvas.rotate(180.0f, canvas.getWidth() / 2.0f, canvas.getHeight() / 2.0f);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        canvas.restore();
        mArrow.setImageBitmap(bitmap);
    }

    private void startRefreshing() {
        mArrow.setVisibility(View.INVISIBLE);
        mProgress.setVisibility(View.VISIBLE);
        mText.setText("加载中...");
        mIsRefreshing = true;

        if (mListener != null) {
            mListener.onRefresh(this);
        }
        
//        completeRefreshing();
    }

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            int limit = 0;
            switch (msg.what) {
                case REFRESH:
                    limit = mHeaderHeight;
                    break;
                case NORMAL:
                    limit = 0;
                    break;
            }

            // Elastic scrolling
            if (msg.arg1 >= limit) {
                setHeaderHeight(msg.arg1);
                int displacement = (msg.arg1 - limit) / 10;
                if (displacement == 0) {
                    mHandler.sendMessage(mHandler.obtainMessage(msg.what, msg.arg1 - 1, 0));
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage(msg.what, msg.arg1 - displacement,
                            0));
                }
            }
        }

    };

    public interface OnRefreshListener {
        public void onRefresh(RefreshPullListView listView);
    }

}
