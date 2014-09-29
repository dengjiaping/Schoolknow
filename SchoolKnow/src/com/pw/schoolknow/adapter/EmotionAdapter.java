package com.pw.schoolknow.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;
import com.pw.schoolknow.config.EmotionConfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class EmotionAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private int currentPage = 0;
	private Map<String, Integer> mFaceMap;
	private List<Integer> faceList = new ArrayList<Integer>();
	
	public EmotionAdapter(Context context, int currentPage){
		this.inflater = LayoutInflater.from(context);
		this.currentPage = currentPage;
		mFaceMap = new EmotionConfig().getFaceMap();
		initData();
	}
	
	private void initData() {
		for(Map.Entry<String, Integer> entry:mFaceMap.entrySet()){
			faceList.add(entry.getValue());
		}
	}

	@Override
	public int getCount() {
		return EmotionConfig.NUM+1;
	}

	@Override
	public Object getItem(int position) {
		return faceList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_default_emotion, null, false);
			viewHolder.faceIV = (ImageView) convertView
					.findViewById(R.id.emotion_item);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (position == EmotionConfig.NUM) {
			viewHolder.faceIV.setImageResource(R.drawable.delete_button);
			viewHolder.faceIV.setBackgroundDrawable(null);
		} else {
			int count = EmotionConfig.NUM * currentPage + position;
			if (count < 107) {
				viewHolder.faceIV.setImageResource(faceList.get(count));
			} else {
				viewHolder.faceIV.setImageDrawable(null);
			}
		}
		return convertView;
	}
	
	public static class ViewHolder {
		ImageView faceIV;
	}

}
