package com.kisueht.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

public class StateDrawable extends View
{
	
	public StateDrawable(Context context)
	{
		super(context);
	}

	/**
	 * 
	 * @param drawables	drawables[0]:normal;drawables[1]:press;drawables[2]:selected
	 * @return
	 */
	public StateListDrawable getStateDrawable(Integer[] drawables)
	{
		StateListDrawable sld = new StateListDrawable();
		
		Drawable normal = null;
		Drawable press = null;
		Drawable selected = null;
		
		Drawable[] tDrawables = {normal,press,selected};
		if (drawables != null)
		{
			for (int i = 0; i < drawables.length; i++)
			{
				if (drawables[i] != null)
				{
					tDrawables[i] = this.getResources().getDrawable(drawables[i]);
				}
			}
		}

		//Note the order!
		sld.addState(View.PRESSED_ENABLED_STATE_SET, tDrawables[1]);
		sld.addState(View.SELECTED_STATE_SET, tDrawables[2]);
		sld.addState(View.EMPTY_STATE_SET, tDrawables[0]);
		sld.addState(View.ENABLED_STATE_SET, tDrawables[0]);
		
		return sld;
	}
	
	/**
	 * 
	 * @param drawables	drawables[0]:normal;drawables[1]:press;drawables[2]:selected
	 * @return
	 */
	public StateListDrawable getStateDrawable(Drawable[] drawables)
	{
		StateListDrawable sld = new StateListDrawable();
		
		Drawable normal = null;
		Drawable press = null;
		Drawable selected = null;
		
		Drawable[] tDrawables = {normal,press,selected};
		if (drawables != null)
		{
			for (int i = 0; i < drawables.length; i++)
			{
				if (drawables[i] != null)
				{
					tDrawables[i] = drawables[i];
				}
			}
		}

		//Note the order!
		sld.addState(View.PRESSED_ENABLED_STATE_SET, tDrawables[1]);
		sld.addState(View.SELECTED_STATE_SET, tDrawables[2]);
		sld.addState(View.EMPTY_STATE_SET, tDrawables[0]);
		sld.addState(View.ENABLED_STATE_SET, tDrawables[0]);
		
		return sld;
	}
	
	/**
	 * 
	 * @param colors	colors[0]:normal;colors[1]:selected
	 * @return
	 */
	public StateListDrawable getColorStateDrawable(Integer[] colors)
	{
		StateListDrawable sld = new StateListDrawable();
		
		ColorDrawable normal = new ColorDrawable(colors[0]);
		ColorDrawable selected = new ColorDrawable(colors[1]);

		//Note the order!
		sld.addState(View.PRESSED_ENABLED_STATE_SET, selected);
		sld.addState(View.FOCUSED_STATE_SET, selected);
		sld.addState(View.EMPTY_STATE_SET, normal);
		sld.addState(View.ENABLED_STATE_SET, normal);
		
		return sld;
	}
	
	/**
	 * 
	 * @param colors	colors[0]:selected;colors[1]:press;colors[2]:enable
	 * @return
	 */
	public ColorStateList getColorStateList(int[] colors)
	{
		int[][] states = {View.SELECTED_STATE_SET,View.PRESSED_ENABLED_STATE_SET,View.ENABLED_STATE_SET};
		
		ColorStateList csl = new ColorStateList(states, colors);
		
		return csl;
	}
}
