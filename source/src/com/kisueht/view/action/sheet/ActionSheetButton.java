package com.kisueht.view.action.sheet;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kisueht.util.KSystemUtil;
import com.kisueht.util.KUtil;
import com.kisueht.util.StateDrawable;

public class ActionSheetButton extends LinearLayout{
	
	public static final int CANCEL = 0;
	public static final int NORMAL = 1;
	
	private int buttonStyle;
	
	private Button mButton;

	private ActionSheetButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ActionSheetButton(Context context, int style) {
		super(context);
		// TODO Auto-generated constructor stub
		
		this.buttonStyle = style;
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(KUtil.FILL, KUtil.dip2px(context, 40, true));
		params.topMargin = KUtil.dip2px(context, 5);
		params.bottomMargin = KUtil.dip2px(context, 5);
		params.leftMargin = KUtil.dip2px(context, 10);
		params.rightMargin = KUtil.dip2px(context, 10);
		setLayoutParams(params);
		
		this.initView();
	}
	
	@SuppressWarnings("deprecation")
	private void initView() {
		mButton = new Button(getContext());
		mButton.setTypeface(null, Typeface.BOLD);
		mButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		this.addView(mButton, new LinearLayout.LayoutParams(KUtil.FILL, KUtil.FILL));
		
		Drawable[] drawables = new Drawable[2];
		drawables[1] = KUtil.getDrawableFromAssets(getContext(), "actionsheet_blue_button_bg.9.png");
		if (CANCEL==buttonStyle) {
			mButton.setTextColor(Color.BLACK);
			drawables[0] = KUtil.getDrawableFromAssets(getContext(), "actionsheet_gray_button_bg.9.png");
		}else {
			mButton.setTextColor(Color.WHITE);
			drawables[0] = KUtil.getDrawableFromAssets(getContext(), "actionsheet_black_button_bg.9.png");
		}
		if (16>=KSystemUtil.getVersionSDK()) {
			mButton.setBackground(new StateDrawable(getContext()).getStateDrawable(drawables));
		}else {
			mButton.setBackgroundDrawable(new StateDrawable(getContext()).getStateDrawable(drawables));
		}
		
	}
	
	public void setText(String text) {
		mButton.setText(text);
	}
	
	public void setTextColor(int color) {
		mButton.setTextColor(color);
	}
	
	public String getText() {
		return mButton.getText().toString();
	}
	
	public ActionSheet getActionSheet() {
		if (CANCEL==buttonStyle) {
			return (ActionSheet) getParent().getParent();
		}else {
			return (ActionSheet) getParent().getParent().getParent();
		}
	}

	@Override
	public void setOnClickListener(final OnClickListener l) {
		// TODO Auto-generated method stub
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null!=l) {
					l.onClick(v);
				}
				
				if (null!=getActionSheet()) {
					getActionSheet().dismiss();
				}
			}
		});
	}
	
}
