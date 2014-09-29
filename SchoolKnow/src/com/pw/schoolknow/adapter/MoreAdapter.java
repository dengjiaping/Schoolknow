package com.pw.schoolknow.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;


public class MoreAdapter extends BaseAdapter {
	
	private List<Map<String,String>> list;
	private Context context;
	private LayoutInflater inflater;
	
	public MoreAdapter(List<Map<String, String>> list, Context context) {
		this.list = list;
		this.context = context;
		this.inflater = LayoutInflater.from(this.context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_more_gv, null, false);
			holder.layout=(RelativeLayout) convertView.findViewById(R.id.more_item_layout);
			holder.title=(TextView) convertView.findViewById(R.id.more_item_text);
			holder.img=(ImageView) convertView.findViewById(R.id.more_item_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,String> map=list.get(position);
		holder.layout.setBackgroundColor(Color.parseColor(map.get("color")));
		holder.title.setText(map.get("text").toString());
		holder.img.setImageResource(Integer.parseInt(map.get("img").toString()));
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		ImageView img;
		RelativeLayout layout;
	}

}
