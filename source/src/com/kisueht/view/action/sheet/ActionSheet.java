/**
 * 
 */
package com.kisueht.view.action.sheet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kisueht.util.KSystemUtil;
import com.kisueht.util.KUtil;

/**
 * @author kisueht
 *
 */
public class ActionSheet extends RelativeLayout{
	
	private Context context;
	private Activity mActivity;
	
	private View overLayView;

	private LinearLayout bgLayout;
	private LinearLayout cLayout;
	public TextView titleView;
	
	private int itemHeight;

	/**
	 * @param context
	 */
	protected ActionSheet(Context context) {
		super(context);
		
		this.context = context;
		mActivity = (Activity) this.context;
		
		itemHeight = KUtil.dip2px(this.context, 40.0F, true);
		this.init();
	}
	
	@SuppressWarnings("deprecation")
	private void init() {
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(KUtil.FILL, KUtil.FILL);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		this.setLayoutParams(params);
		
		overLayView = new View(context);
		overLayView.setBackgroundColor(Color.parseColor("#30000000"));
		overLayView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		this.addView(overLayView, new RelativeLayout.LayoutParams(KUtil.FILL, KUtil.FILL));
		
		bgLayout = new LinearLayout(context);
		bgLayout.setClickable(false);
		bgLayout.setOrientation(LinearLayout.VERTICAL);
		if (16>=KSystemUtil.getVersionSDK()) {
			bgLayout.setBackground(KUtil.getDrawableFromAssets(context, "actionsheet_bg.9.png"));
		}else {
			bgLayout.setBackgroundDrawable(KUtil.getDrawableFromAssets(context, "actionsheet_bg.9.png"));
		}
		RelativeLayout.LayoutParams cllp = new RelativeLayout.LayoutParams(KUtil.FILL, KUtil.WRAP);
		cllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		this.addView(bgLayout, cllp);
		
		this.addTitleView();
		
		cLayout = new LinearLayout(context);
		cLayout.setClickable(false);
		cLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(KUtil.FILL, KUtil.WRAP);
		int margin = KUtil.dip2px(context, 5);
		clp.setMargins(margin, margin, margin, margin);
		bgLayout.addView(cLayout, clp);
		
		ActionSheetButton cancelButton = new ActionSheetButton(context, ActionSheetButton.CANCEL);
		cancelButton.setText("取消");
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		LinearLayout.LayoutParams cancellp = new LinearLayout.LayoutParams(KUtil.FILL, itemHeight);
		cancellp.setMargins(margin, margin, margin, margin);
		bgLayout.addView(cancelButton, cancellp);
	}

	private void addTitleView() {
		titleView = new TextView(context);
		titleView.setGravity(Gravity.CENTER);
		titleView.setTextColor(Color.WHITE);
		titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
		titleView.setVisibility(View.GONE);
		LinearLayout.LayoutParams ttlp = new LinearLayout.LayoutParams(KUtil.FILL, itemHeight);
		/*int margin = KUtil.dip2px(context, 5);
		ttlp.setMargins(margin, margin, margin, margin);*/
		bgLayout.addView(titleView, ttlp);
	}
	
	public void setTitle(String text) {
		titleView.setVisibility(View.VISIBLE);
		titleView.setText(text);
	}
	
	public void addItems(ActionSheetButton[] actionSheetButtons) {
		for (int i = 0; i < actionSheetButtons.length; i++) {
			cLayout.addView(actionSheetButtons[i]);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (KeyEvent.KEYCODE_BACK==keyCode) {
			dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void show() {
		mActivity.getWindow().addContentView(this, new ViewGroup.LayoutParams(KUtil.FILL, KUtil.FILL));
		
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.RELATIVE_TO_SELF,1.0f,Animation.RELATIVE_TO_SELF,0.0f);
		translateAnimation.setInterpolator(new AccelerateInterpolator());
		translateAnimation.setDuration(200);
		bgLayout.setAnimation(translateAnimation);
		translateAnimation.startNow();
	}

	public void dismiss() {
		ViewGroup parentGroup = (ViewGroup) this.getParent();
		parentGroup.removeView(this);
	}

}
