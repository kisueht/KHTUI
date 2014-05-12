package com.kisueht.util;

import java.util.HashMap;

import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

public class AnimationUtil {

	public static final int TOP_IN = 0X1000;
	public static final int BOTTOM_IN = 0X1001;
	public static final int LEFT_IN = 0X1002;
	public static final int RIGHT_IN = 0X1003;
	
	private static HashMap<Integer, TranslateAnimation> transHashMap = new HashMap<Integer, TranslateAnimation>();
	
	static{
		TranslateAnimation topAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		transHashMap.put(TOP_IN, topAnimation);
		
		TranslateAnimation bottomAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		transHashMap.put(BOTTOM_IN, bottomAnimation);
		
		TranslateAnimation leftAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		transHashMap.put(LEFT_IN, leftAnimation);
		
		TranslateAnimation rightInAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		transHashMap.put(RIGHT_IN, rightInAnimation);
	}

	/**
	 * @see #getTranslateAnimation(int, int)
	 * @param tag	{@link #TOP_IN},{@link #BOTTOM_IN},{@link #LEFT_IN},{@link #RIGHT_IN}
	 * @return
	 */
	public static TranslateAnimation getTranslateAnimation(int tag) {
		return getTranslateAnimation(tag, 200);
	}
	
	/**
	 * 
	 * @param tag	{@link #TOP_IN},{@link #BOTTOM_IN},{@link #LEFT_IN},{@link #RIGHT_IN}
	 * @param durationMillis	动画的时间间隔
	 * @return
	 */
	public static TranslateAnimation getTranslateAnimation(int tag, int durationMillis) {
		TranslateAnimation translateAnimation = transHashMap.get(tag);
		if (null!=translateAnimation) {
			translateAnimation.setDuration(durationMillis);
		}
		
		return translateAnimation;
	}
}
