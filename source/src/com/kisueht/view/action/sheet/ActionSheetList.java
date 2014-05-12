/**
 * 
 */
package com.kisueht.view.action.sheet;

import com.kisueht.view.action.AbstractAction;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

/**
 * @author kisueht
 *
 */
public class ActionSheetList extends AbstractAction{
	
	private ListView mListView;
	private OnActionItemSelectListener mOnActionItemSelectListener;
	
	private String[] contents;

	/**
	 * @param context
	 */
	protected ActionSheetList(Context context) {
		super(context);
		
		initView();
	}
	
	private void initView() {
		mListView = new ListView(getContext());
	}
	
	public void setContents(String[] contents) {
		this.contents = contents;
		
		mListView.setAdapter(new ActionListAdapter());
	}
	
	public void setOnActionItemSelectListener(OnActionItemSelectListener onActionItemSelectListener) {
		this.mOnActionItemSelectListener = onActionItemSelectListener;
	}
	
	private class ActionListAdapter extends BaseAdapter implements OnItemClickListener{

		private ActionListAdapter() {
			super();
			// TODO Auto-generated constructor stub
			
			mListView.setOnItemClickListener(this);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contents.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return contents[arg0];
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			return null;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mOnActionItemSelectListener.onItemSelect(arg2);
		}
		
	}
	
	public interface OnActionItemSelectListener{
		public void onItemSelect(int position);
	}

}
