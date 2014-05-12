/**
 * 
 */
package com.kisueht.view.roundlist;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kisueht.util.KSystemUtil;
import com.kisueht.util.KUtil;
import com.kisueht.util.StateDrawable;

/**
 * @author kisueht
 * 
 */
public class RoundListView extends ListView {
	public static final String TAG = RoundListView.class.getSimpleName();

	protected int pad = 16;

	private RoundListAdapter mRoundListAdapter;
	protected RoundListViewAdapter mRoundListViewAdapter;

	private StateDrawable mStateDrawable;

	/**
	 * @param context
	 */
	public RoundListView(Context context) {
		this(context, null);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public RoundListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public RoundListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		this.init();

		mStateDrawable = new StateDrawable(context);
	}

	private void init() {
		int dp_pad = KUtil.dip2px(getContext(), pad);
		this.setPadding(dp_pad, dp_pad, dp_pad, dp_pad);
		this.setCacheColorHint(Color.TRANSPARENT);
		this.setVerticalScrollBarEnabled(false);
		this.setVerticalFadingEdgeEnabled(false);
		this.setDivider(null);
		this.setDividerHeight(0);

		ShapeDrawable shapeDrawable = new ShapeDrawable();
		shapeDrawable.getPaint().setColor(Color.TRANSPARENT);
		setSelector(shapeDrawable);
	}

	private Drawable getAssetsDrawable(String name) {
		return KUtil.getDrawableFromAssets(getContext(), name);
	}

	private Drawable getCellAllDrawable() {
		return mStateDrawable.getStateDrawable(new Drawable[] {
				getAssetsDrawable("tablecell_common.9.png"),
				getAssetsDrawable("tablecell_common_s.9.png") });
	}

	private Drawable getCellTopDrawable() {
		return mStateDrawable.getStateDrawable(new Drawable[] {
				getAssetsDrawable("tablecell_top.9.png"),
				getAssetsDrawable("tablecell_top_s.9.png") });
	}

	private Drawable getCellMiddleDrawable() {
		return mStateDrawable.getStateDrawable(new Drawable[] {
				getAssetsDrawable("tablecell_midle.9.png"),
				getAssetsDrawable("tablecell_midle_s.9.png") });
	}

	private Drawable getCellBottomDrawable() {
		return mStateDrawable.getStateDrawable(new Drawable[] {
				getAssetsDrawable("tablecell_bottom.9.png"),
				getAssetsDrawable("tablecell_bottom_s.9.png") });
	}

	public void setRoundListAdapter(RoundListAdapter roundListAdapter) {
		if (null != roundListAdapter) {
			this.mRoundListAdapter = roundListAdapter;

			mRoundListViewAdapter = new RoundListViewAdapter();
			this.setAdapter(mRoundListViewAdapter);
		}
	}
	
	public void reLoadData() {
		if (null!=mRoundListViewAdapter) {
			mRoundListViewAdapter.initIndexPathList();
			mRoundListViewAdapter.notifyDataSetChanged();
		}
	}

	private class IndexPath {
		public int section;
		public int row;

		private IndexPath(int section, int row) {
			this.section = section;
			this.row = row;
		}
	}

	protected class RoundListViewAdapter extends BaseAdapter {

		private ArrayList<IndexPath> mIndexPathList = new ArrayList<RoundListView.IndexPath>();

		private RoundListViewAdapter() {
			super();
			// TODO Auto-generated constructor stub

			this.initIndexPathList();
			
			if (null!=mRoundListAdapter) {
				
			}
		}

		private void initIndexPathList() {
			if (null != this.mIndexPathList) {
				this.mIndexPathList.clear();
			}

			if (null != mRoundListAdapter) {
				int sectionCount = mRoundListAdapter.getSectionCount();

				for (int i = 0; i < sectionCount; i++) {
					if (null != mIndexPathList) {
						IndexPath indexPath = new IndexPath(i, -1);
						mIndexPathList.add(indexPath);

						int rowCount = mRoundListAdapter.getRowCount(i);
						for (int j = 0; j < rowCount; j++) {
							indexPath = new IndexPath(i, j);
							mIndexPathList.add(indexPath);
						}
					}
				}
			}
		}

		private IndexPath getIndexPath(int position) {
			int count = mIndexPathList.size();

			if (0 == count) {
				initIndexPathList();
			}

			return mIndexPathList.get(position);
		}

		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			int sectionCount = mRoundListAdapter.getSectionCount();
			int total = sectionCount;
			for (int i = 0; i < sectionCount; i++) {
				total += mRoundListAdapter.getRowCount(i);
			}

			return total;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			IndexPath indexPath = getIndexPath(position);

			if (-1 == indexPath.row) {
				return getSectionView(position, null, null);
			}

			return getRowView(indexPath.section, indexPath.row, null, null);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			IndexPath indexPath = getIndexPath(position);

			if (-1 == indexPath.row) {
				return indexPath.section;
			}

			return indexPath.section * 10 + indexPath.row;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.i(TAG, "getview is " + position);
			View v = null;

			final IndexPath indexPath = getIndexPath(position);

			if (-1 == indexPath.row) {
				v = getSectionView(indexPath.section, convertView, parent);
				v.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mRoundListAdapter
								.didItemSectionClickEvent(indexPath.section);
					}
				});
				return v;
			}

			v = getRowView(indexPath.section, indexPath.row, convertView, parent);
			v.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mRoundListAdapter.didItemRowClickEvent(indexPath.section,
							indexPath.row);
				}
			});
			return v;
		}

		private View getSectionView(int position, View convertView,
				ViewGroup parent) {
			View v = null;

			if (null != convertView && null != convertView.getTag()
					&& convertView.getTag().equals("section" + position)) {
				v = convertView;
			} else {
				if (null != mRoundListAdapter.getSectionView(position)) {
					v = mRoundListAdapter.getSectionView(position);
				} else {
					TextView sectionTextView = new TextView(getContext());
					sectionTextView.setGravity(Gravity.BOTTOM);
					sectionTextView
							.setMinHeight(KUtil.dip2px(getContext(), pad, true));
					sectionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					sectionTextView.setTextColor(Color.GRAY);

					if (null != mRoundListAdapter.getSectionTitle(position)) {
						sectionTextView.setText(mRoundListAdapter
								.getSectionTitle(position));
					}

					if (0 < mRoundListAdapter.getSectionTitleSize(position)) {
						sectionTextView
								.setTextSize(TypedValue.COMPLEX_UNIT_SP,
										mRoundListAdapter
												.getSectionTitleSize(position));
					}

					v = sectionTextView;
				}
				
				if (0 != mRoundListAdapter.getSectionHeight(position)) {
					v.setLayoutParams(new AbsListView.LayoutParams(KUtil.FILL, KUtil.dip2px(getContext(),
							mRoundListAdapter.getSectionHeight(position), true)));
				}

				v.setTag("section" + position);
			}

			return v;
		}

		@SuppressWarnings("deprecation")
		private View getRowView(int secNo, int rowNo, View convertView,
				ViewGroup parent) {
			RoundListTableCell v = null;

			if (null != convertView && null != convertView.getTag()
					&& convertView.getTag().equals("row" + secNo + rowNo)) {
				v = (RoundListTableCell) convertView;
				Log.i(TAG, "section " + secNo + ";row " + rowNo
						+ " from cache!");
			} else {Log.i(TAG, "section " + secNo + ";row " + rowNo
					+ " from new");
				v = (RoundListTableCell) mRoundListAdapter.getRowView(secNo, rowNo);
				v.setTag("row" + secNo + rowNo);
				
				if (mRoundListAdapter.getRowHeight(secNo, rowNo) > 0) {
					v.setLayoutParams(new AbsListView.LayoutParams(KUtil.FILL, KUtil.dip2px(getContext(), mRoundListAdapter.getRowHeight(secNo, rowNo), true)));
//					v.setMinimumHeight(mRoundListAdapter.getRowHeight(secNo, rowNo));
				}else {
					v.setLayoutParams(new AbsListView.LayoutParams(KUtil.FILL, KUtil.WRAP));
				}

				if (null == v.getBackground()) {
					if (0 < mRoundListAdapter.getRowCount(secNo)) {
						Drawable mDrawable = null;
						if (1 == mRoundListAdapter.getRowCount(secNo)) {
							mDrawable = getCellAllDrawable();
						} else {
							if (0 == rowNo) {
								mDrawable = getCellTopDrawable();
							} else if (mRoundListAdapter.getRowCount(secNo) - 1 == rowNo) {
								mDrawable = getCellBottomDrawable();
							} else {
								mDrawable = getCellMiddleDrawable();
							}
						}
						
						if (16>=KSystemUtil.getVersionSDK()) {
							v.setBackground(mDrawable);
						}else {
							v.setBackgroundDrawable(mDrawable);
						}
						
						mDrawable = null;
					}
				}
				
				if (v.isHideBackground()) {
					if (16>=KSystemUtil.getVersionSDK()) {
						v.setBackground(null);
					}else {
						v.setBackgroundDrawable(null);
					}
					
				}
				
				if (!v.isEnabled()) {
					v.setClickable(false);
				}
			}
			
			return v;
		}
		
		protected class MyDataSetObserver extends DataSetObserver
	    {
	      protected MyDataSetObserver()
	      {
	      }

	      public void onChanged()
	      {
	        mIndexPathList.clear();

	        initIndexPathList();

	        notifyDataSetChanged();
	      }

	      public void onInvalidated()
	      {
	        mIndexPathList.clear();

	        initIndexPathList();

	        notifyDataSetInvalidated();
	      }
	    }

	}

}
