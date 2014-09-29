package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScoreAdapter extends BaseAdapter {
	
	public static final String defaultColor="#8883C5";
	
	private List<Map<String,Object>> list;
	private Context context;
	private LayoutInflater inflater;
	
	public ScoreAdapter(List<Map<String, Object>> list, Context context) {
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
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_score_data, null, false);
			holder.subject=(TextView) convertView.findViewById(R.id.subject);
			holder.score=(TextView) convertView.findViewById(R.id.score);
			holder.extra_1=(TextView) convertView.findViewById(R.id.extra_1);
			holder.extra_2=(TextView) convertView.findViewById(R.id.extra_2);
			holder.demand=(TextView) convertView.findViewById(R.id.demand);
			holder.credit=(TextView) convertView.findViewById(R.id.credit);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String,Object> map=list.get(position);
		holder.subject.setText(""+map.get("subject"));
		
		try{
			int sc=Integer.parseInt(map.get("score").toString());
			if(sc<60){
				holder.score.setTextColor(Color.RED);
			}else{
				holder.score.setTextColor(Color.parseColor(defaultColor));
			}
		}catch(Exception e){
			if(map.get("score").toString().trim().equals("不及格")){
				holder.score.setTextColor(Color.RED);
			}else{
				holder.score.setTextColor(Color.parseColor(defaultColor));
			}
		}
		holder.score.setText("成绩:"+map.get("score"));
		
		if(map.get("extra_1").toString().trim().equals("")){
			holder.extra_1.setVisibility(View.GONE);
		}else{
			holder.extra_1.setText("重考一:"+map.get("extra_1"));
		}
		if(map.get("extra_2").toString().trim().equals("")){
			holder.extra_2.setVisibility(View.GONE);
		}else{
			holder.extra_2.setText("重考二:"+map.get("extra_2"));
		}
		
		holder.demand.setText("课程要求:"+map.get("demand"));
		holder.credit.setText("学分:"+map.get("credit"));
		
		return convertView;
	}
	
	
	static class ViewHolder {
		TextView subject;
		TextView score;
		TextView extra_1;
		TextView extra_2;
		TextView demand;
		TextView credit;
	}

}
