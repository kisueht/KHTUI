package com.kisueht.view.roundlist;

import android.view.View;

public abstract class RoundListAdapter {

	/**
	 * 
	 * @param sectionNo
	 */
	public void didItemSectionClickEvent(int sectionNo){};
	/**
	 * 
	 * @param sectionNo
	 * @param rowNo
	 */
	public void didItemRowClickEvent(int sectionNo, int rowNo){};
	/**
	 * 
	 * @return
	 */
	public abstract int getSectionCount();
	/**
	 * 
	 * @param sectionNo
	 * @return
	 */
	public abstract int getSectionHeight(int sectionNo);
	/**
	 * 
	 * @param sectionNo
	 * @return
	 */
	public abstract String getSectionTitle(int sectionNo);
	/**
	 * 
	 * @param sectionNo
	 * @return
	 */
	public abstract int getSectionTitleSize(int sectionNo);
	/**
	 * 
	 * @param sectionNo
	 * @return
	 */
	public abstract View getSectionView(int sectionNo);
	/**
	 * 
	 * @return
	 */
	public abstract int getRowCount(int sectionNo);
	/**
	 * 
	 * @param rowNo
	 * @return
	 */
	public abstract int getRowHeight(int sectionNo, int rowNo);
	/**
	 * 
	 * @param rowNo
	 * @return
	 */
	public abstract View getRowView(int sectionNo, int rowNo);
}
