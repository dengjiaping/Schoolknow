package com.pw.schoolknow.adapter;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * ViewPager适配器
 * 
 */
public class ViewPagerAdapter extends PagerAdapter {
	private List<View> list;

	public ViewPagerAdapter(Context context, List<View> list) {
		if (list != null) {
			this.list = list;
		} else {
			this.list = new ArrayList<View>();
		}
	}

	// 销毁position位置的界面
	@Override
	public void destroyItem(View view, int position, Object obj) {
		((ViewPager) view).removeView(list.get(position));
	}

	// 获取当前窗体界面数
	@Override
	public int getCount() {
		return list.size();
	}

	// 初始化position位置的界面
	@Override
	public Object instantiateItem(View view, int position) {
		((ViewPager) view).addView(list.get(position));
		return list.get(position);
	}
	

	// 判断View和对象是否为同一个View
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

	@Override
	public void finishUpdate(View arg0) {

	}

}
