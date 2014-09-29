package com.pw.schoolknow.adapter;

import java.util.ArrayList;

import com.pw.schoolknow.R;
import com.pw.schoolknow.utils.ImageManager2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ToggleButton;
@SuppressLint("ResourceAsColor")
public class AlbumGridViewAdapter extends BaseAdapter implements
		OnClickListener {

	private Context mContext;
	private ArrayList<String> dataList;
	private ArrayList<String> selectedDataList;
	private DisplayMetrics dm;

	public AlbumGridViewAdapter(Context c, ArrayList<String> dataList,
			ArrayList<String> selectedDataList) {

		mContext = c;
		this.dataList = dataList;
		this.selectedDataList = selectedDataList;
		dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 存放列表项控件句�?	 */
	private class ViewHolder {
		public ImageView imageView,check;
		public ToggleButton toggleButton;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_select_imageview, parent, false);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image_view);
			viewHolder.toggleButton = (ToggleButton) convertView
					.findViewById(R.id.toggle_button);
			viewHolder.check = (ImageView) convertView
					.findViewById(R.id.check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String path;
		if (dataList != null && dataList.size() > position)
			path = dataList.get(position);
		else
			path = "camera_default";
		if (path.contains("default")) {
			viewHolder.imageView.setImageResource(R.drawable.empty_photo);
		} else {
			ImageManager2.from(mContext).displayImage(viewHolder.imageView,
					path, R.drawable.empty_photo, 100, 100);
		}
		viewHolder.toggleButton.setTag(position);
		viewHolder.toggleButton.setOnClickListener(this);
		if (isInSelectedDataList(path)) {
			viewHolder.toggleButton.setChecked(true);
			viewHolder.toggleButton.setBackgroundColor(Color.TRANSPARENT) ;
			viewHolder.check.setVisibility(View.VISIBLE) ;
		} else {
			viewHolder.toggleButton.setChecked(false);
			viewHolder.toggleButton.setBackgroundColor(Color.TRANSPARENT) ;
			viewHolder.check.setVisibility(View.GONE) ;
		}

		return convertView;
	}

	private boolean isInSelectedDataList(String selectedString) {
		for (int i = 0; i < selectedDataList.size(); i++) {
			if (selectedDataList.get(i).equals(selectedString)) {
				return true;
			}
		}
		return false;
	}

	public int dipToPx(int dip) {
		return (int) (dip * dm.density + 0.5f);
	}

	@Override
	public void onClick(View view) {
		if (view instanceof ToggleButton) {
			ToggleButton toggleButton = (ToggleButton) view;
			int position = (Integer) toggleButton.getTag();
			if (dataList != null && mOnItemClickListener != null
					&& position < dataList.size()) {
				mOnItemClickListener.onItemClick(toggleButton, position,
						dataList.get(position), toggleButton.isChecked());
			}
		}
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(ToggleButton view, int position, String path,
				boolean isChecked);
	}

}
