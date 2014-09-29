package com.pw.schoolknow.adapter;

import java.util.List;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.CountdownItem;
import com.pw.schoolknow.utils.TimeUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CountDownAdapter extends BaseAdapter {
	
	private List<CountdownItem> list;
	private LayoutInflater inflater;
	

	public CountDownAdapter(Context context,List<CountdownItem> list) {
		super();
		this.list = list;
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
		CountdownItem item=list.get(position);
		ViewHolder holder = null;
		if (convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_count_down_lv, null, false);
			holder.title=(TextView) convertView.findViewById(R.id.countdown_title);
			holder.time=(TextView) convertView.findViewById(R.id.countdown_time);
			holder.address=(TextView) convertView.findViewById(R.id.countdown_address);
			holder.countdown=(TextView) convertView.findViewById(R.id.countdown_daynum);
			holder.tip=(TextView) convertView.findViewById(R.id.countdown_tip);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(item.getTitle());
		holder.time.setText(TimeUtil.getYmdHmW(item.getTime_samp()));
		holder.address.setText(item.getAddress());
		
		int countDownDay=TimeUtil.betweenDays(item.getTime_samp());
		if(countDownDay>0){
			holder.countdown.setText(countDownDay+"");
			holder.tip.setText("天");
			holder.tip.setTextSize(14);
		}else if(countDownDay==0){
			holder.countdown.setText("今天");
			holder.countdown.setTextSize(18);
			holder.tip.setVisibility(View.GONE);
		}else{
			holder.countdown.setText("");
			holder.tip.setText("完成");
			holder.tip.setTextSize(18);
		}
		return convertView;
	}
	
	static class ViewHolder {
		TextView title;
		TextView time;
		TextView address;
		TextView countdown;
		TextView tip;
	}
	
}
