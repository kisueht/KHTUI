/**
 * 
 */
package com.kisueht.view.roundlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kisueht.util.KSystemUtil;
import com.kisueht.util.KUtil;

/**
 * @author kisueht
 *
 */
public class RoundListTableCell extends RelativeLayout {
	private Context context;
	
	public ImageView iconImageView;
	public TextView titleView;
	public TextView subTitleView;
	private ImageView indicatorImageView;
	
	private boolean isHideBackground = false;

	/**
	 * @param context
	 */
	public RoundListTableCell(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RoundListTableCell(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RoundListTableCell(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.setMinimumHeight(KUtil.dip2px(context, 50, true));
		this.setClickable(true);
		this.setEnabled(true);
		this.initView();
	}
	
	private void initView() {
		
		LinearLayout titleLayout = new LinearLayout(context);
		titleLayout.setOrientation(LinearLayout.VERTICAL);
//		titleLayout.setGravity(Gravity.CENTER_VERTICAL);
		int w_pad = KUtil.dip2px(getContext(), 10);
		int h_pad = KUtil.dip2px(getContext(), 5, true);
		titleLayout.setPadding(w_pad, h_pad, w_pad*2, h_pad);
		RelativeLayout.LayoutParams tllp = new RelativeLayout.LayoutParams(KUtil.FILL, KUtil.FILL);
		tllp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		this.addView(titleLayout, tllp);
		
		titleView = new TextView(context);
		titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		titleView.setTextColor(Color.BLACK);
		titleView.setTypeface(Typeface.DEFAULT_BOLD, Typeface.BOLD);
		titleLayout.addView(titleView, new LinearLayout.LayoutParams(KUtil.FILL, KUtil.WRAP));
		subTitleView = new TextView(context);
		subTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		subTitleView.setTextColor(Color.GRAY);
		subTitleView.setVisibility(View.GONE);
		titleLayout.addView(subTitleView, new LinearLayout.LayoutParams(KUtil.FILL, KUtil.WRAP));
		
		
		indicatorImageView = new ImageView(getContext());
		indicatorImageView.setScaleType(ScaleType.CENTER_INSIDE);
		indicatorImageView.setImageDrawable(KUtil.getDrawableFromAssets(getContext(), "go.png"));
		RelativeLayout.LayoutParams iivlp = new RelativeLayout.LayoutParams(KUtil.WRAP, KUtil.WRAP);
		iivlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		iivlp.addRule(RelativeLayout.CENTER_VERTICAL);
		iivlp.rightMargin = KUtil.dip2px(getContext(), 10);
		this.addView(indicatorImageView, iivlp);
	}
	
	public void setTitle(String text) {
		titleView.setText(text);
	}
	
	public void setSubTitle(String subTitle) {
		subTitleView.setVisibility(View.VISIBLE);
		subTitleView.setText(subTitle);
	}
	
	public void setContentView(View v) {
		setContentView(v, new RelativeLayout.LayoutParams(KUtil.FILL, KUtil.FILL));
	}
	
	public void setContentView(View v, RelativeLayout.LayoutParams layoutParams) {
		this.removeAllViews();
		this.addView(v, layoutParams);
	}
	
	@SuppressWarnings("deprecation")
	public void hideBackground(boolean bl) {
		isHideBackground = bl;
		if (16>=KSystemUtil.getVersionSDK()) {
			this.setBackground(null);
		}else {
			this.setBackgroundDrawable(null);
		}
	}
	
	public void hideIndicatorView(boolean bl) {
		indicatorImageView.setVisibility(bl?View.GONE:View.VISIBLE);
	}
	
	public boolean isHideBackground() {
		return isHideBackground;
	}

}
