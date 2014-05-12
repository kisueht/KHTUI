/**
 * 
 */
package com.kisueht.view.action;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.kisueht.util.KSystemUtil;
import com.kisueht.util.KUtil;

/**
 * @author kisueht
 *
 */
public class AbstractAction extends RelativeLayout{
	
	private Context context;
	private Activity mActivity;
	
	protected View overLayView;
	protected LinearLayout bgLayout;

	/**
	 * @param context
	 */
	protected AbstractAction(Context context) {
		super(context);
		
		this.context = context;
		mActivity = (Activity) this.context;
		
		this.init();
	}
	
	@SuppressWarnings("deprecation")
	protected void init() {
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
//				dismiss();
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
