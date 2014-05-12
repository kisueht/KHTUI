package com.kisueht.view.picker;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.kisueht.util.KSystemUtil;
import com.kisueht.util.KUtil;
import com.kisueht.view.wheel.WheelView;
import com.kisueht.view.wheel.adapters.AbstractWheelTextAdapter;

public class UIPiker extends RelativeLayout {
	
	private WheelView mWheelView;

	public UIPiker(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.initView();
	}

	public UIPiker(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.initView();
	}

	public UIPiker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.initView();
	}
	
	@SuppressWarnings("deprecation")
	private void initView() {
		if (16>KSystemUtil.getVersionSDK()) {
			this.setBackground(new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{Color.parseColor("#FF000000"),Color.parseColor("#FF000000"),Color.parseColor("#FF777777")}));
		}else {
			this.setBackgroundDrawable(new GradientDrawable(Orientation.BOTTOM_TOP, new int[]{Color.parseColor("#FF000000"),Color.parseColor("#FF000000"),Color.parseColor("#FF777777")}));
		}
		
		this.setLayoutParams(new ViewGroup.LayoutParams(KUtil.FILL, KUtil.WRAP));
		
		mWheelView = new WheelView(getContext());
        RelativeLayout.LayoutParams mwvlp = new RelativeLayout.LayoutParams(KUtil.getWidthAndHeight(getContext())[0]/2, KUtil.dip2px(getContext(), 100, true));
        mwvlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mwvlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        int margin = KUtil.dip2px(getContext(), 20);
        mwvlp.topMargin = margin;//(margin*2, margin, margin*2, 0);
        this.addView(mWheelView, mwvlp);
	}
	
	public void setVisibleItems(int count) {
		mWheelView.setVisibleItems(count);
	}
	
	public void setViewAdapter(AbstractWheelTextAdapter wheelTextAdapter) {
		mWheelView.setViewAdapter(wheelTextAdapter);
	}

}
