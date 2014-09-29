package com.pw.schoolknow.adapter;

import java.util.List;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.IndexItemBase;
import com.pw.schoolknow.utils.TimeUtil;



import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IndexAdapter extends BaseAdapter {
	
	private List<IndexItemBase> list;
	private LayoutInflater inflater;
	
	public IndexAdapter(Context context,List<IndexItemBase> list){
		this.list=list;
		this.inflater = LayoutInflater.from(context);
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
		IndexItemBase item=list.get(position);
		if (convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_index_lv, null, false);
			holder.title=(TextView) convertView.findViewById(R.id.index_item_event_title);
			holder.intro=(TextView) convertView.findViewById(R.id.index_item_event_intro);
			holder.time=(TextView) convertView.findViewById(R.id.index_item_event_time);
			holder.address=(TextView) convertView.findViewById(R.id.index_item_event_address);
			holder.type=(TextView) convertView.findViewById(R.id.index_lv_item_type);
			holder.catalog=(TextView) convertView.findViewById(R.id.index_lv_item_catalog);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(item.getTitle());
		holder.catalog.setText(TimeUtil.getYmdW(item.getTimesamp()));
		//活动
		if(item.getType()==IndexItemBase.INFO_EVENT){
			holder.intro.setVisibility(View.GONE);
			holder.time.setVisibility(View.VISIBLE);
			holder.address.setVisibility(View.VISIBLE);
			
			holder.address.setText(item.getAddress());
			holder.time.setText(item.getParam());
			holder.type.setText("活动");
			holder.type.setBackgroundColor(Color.parseColor("#87CEEA"));
			
		//新闻
		}else if(item.getType()==IndexItemBase.INFO_NEWS){
			holder.time.setVisibility(View.GONE);
			holder.address.setVisibility(View.GONE);
			holder.intro.setVisibility(View.VISIBLE);
			
			holder.intro.setText(item.getIntro());
			holder.type.setText("新闻");
			holder.type.setBackgroundColor(Color.parseColor("#9A99FF"));
		//阅读
		}else if(item.getType()==IndexItemBase.INFO_READ){
			holder.time.setVisibility(View.GONE);
			holder.address.setVisibility(View.GONE);
			holder.intro.setVisibility(View.VISIBLE);
			
			holder.intro.setText(item.getIntro());
			holder.type.setText("阅读");
			holder.type.setBackgroundColor(Color.parseColor("#FEB1DF"));
		//通知
		}else if(item.getType()==IndexItemBase.INFO_INFROM){
			holder.time.setVisibility(View.GONE);
			holder.address.setVisibility(View.GONE);
			holder.intro.setVisibility(View.VISIBLE);
			
			holder.intro.setText(item.getIntro());
			holder.type.setText("通知");
			holder.type.setBackgroundColor(Color.parseColor("#F9AF7E"));
		//默认显示阅读
		}else{
			holder.time.setVisibility(View.GONE);
			holder.address.setVisibility(View.GONE);
			holder.intro.setVisibility(View.VISIBLE);
			
			holder.intro.setText(item.getIntro());
			holder.type.setText("阅读");
			holder.type.setBackgroundColor(Color.parseColor("#FEB1DF"));
		}
		//与上一条信息的时间差
		if(position>=1){
			int countTime=TimeUtil.newBetweenDays(list.get(position-1).getTimesamp(), item.getTimesamp());
			if(countTime==0){
				holder.catalog.setVisibility(View.GONE);
			}else{
				holder.catalog.setVisibility(View.VISIBLE);
			}
		}else{
			holder.catalog.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView intro;
		TextView time;
		TextView address;
		TextView type;
		TextView catalog;
	}

}
