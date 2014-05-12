/**
 * 
 */
package com.kisueht.view.action.sheet;

import com.kisueht.view.action.AbstractActionBuilder;

import android.content.Context;


/**
 * @author kisueht
 *
 */
public class ActionSheetBuilder extends AbstractActionBuilder<ActionSheet> {
	
	private ActionSheet mActionSheet;

	/**
	 * @param context
	 */
	public ActionSheetBuilder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		mActionSheet = new ActionSheet(context);
	}

	/* (non-Javadoc)
	 * @see com.kisueht.view.builder.AbstractBuilder#show()
	 */
	@Override
	public ActionSheet build() {
		// TODO Auto-generated method stub
		return mActionSheet;
	}

}
