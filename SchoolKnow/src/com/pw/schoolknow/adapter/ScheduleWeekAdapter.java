package com.pw.schoolknow.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;
import com.pw.schoolknow.widgets.MyTextView;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ScheduleWeekAdapter extends BaseAdapter {
	
	private List<Map<String ,String>> list;
	private LayoutInflater inflater;
	
	public List<String> subject;
	public List<Integer> subBgColor;
	
	public int colors[] = {    
	        Color.parseColor("#339933"),
	        Color.parseColor("#1BA1E2"),
	        Color.parseColor("#F09609"),
	        Color.parseColor("#8CBF26"),
	        Color.parseColor("#00ABA9"),    
	        Color.parseColor("#FF0097"),
	        Color.parseColor("#E671B8"),
	        Color.parseColor("#996600"),
	        Color.parseColor("#A200FF"),
	        Color.parseColor("#38D3A9"),
	        Color.parseColor("#34CED9"),
	        Color.parseColor("#F895D7"),
	        Color.parseColor("#E51400")
	 }; 
	
	public ScheduleWeekAdapter(Context context,List<Map<String,String>> list){
		this.list=list;
		this.inflater = LayoutInflater.from(context);
		subject=new ArrayList<String>();
		subBgColor=new ArrayList<Integer>();
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
			convertView = inflater.inflate(R.layout.item_schedule_week_lv, null, false);
			holder.title=(MyTextView) convertView.findViewById(R.id.schedule_week_title);
			holder.s1=(TextView) convertView.findViewById(R.id.schedule_week_s1);
			holder.s2=(TextView) convertView.findViewById(R.id.schedule_week_s2);
			holder.s3=(TextView) convertView.findViewById(R.id.schedule_week_s3);
			holder.s4=(TextView) convertView.findViewById(R.id.schedule_week_s4);
			holder.s5=(TextView) convertView.findViewById(R.id.schedule_week_s5);
			holder.s6=(TextView) convertView.findViewById(R.id.schedule_week_s6);
			holder.s7=(TextView) convertView.findViewById(R.id.schedule_week_s7);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.title.setText(position*2+1+"\n-\n"+(position*2+2));
		holder.title.setBottomBorder(true);
		Map<String,String> map=list.get(position);
		holder.s1.setText(getWeekClass(map.get("s1").toString()));
		holder.s2.setText(getWeekClass(map.get("s2").toString()));
		holder.s3.setText(getWeekClass(map.get("s3").toString()));
		holder.s4.setText(getWeekClass(map.get("s4").toString()));
		holder.s5.setText(getWeekClass(map.get("s5").toString()));
		holder.s6.setText(getWeekClass(map.get("s6").toString()));
		holder.s7.setText(getWeekClass(map.get("s7").toString()));
		setBgColor(holder,map);
		return convertView;
	}
	
	public int getCurrentBg(){
		int currentColor=colors[0];
		if(subBgColor.size()<=colors.length){
			currentColor=colors[subBgColor.size()];
		}else{
			currentColor=colors[subBgColor.size()%colors.length];
		}
		return currentColor;
	}
	
	public String separateSub(String str){
		String[] temp=str.split("-");
		return temp[0];
	}
	
	public void setBgColor(ViewHolder holder,Map<String,String> map){
		if(!map.get("s1").equals("")){
			int index=subject.indexOf(separateSub(map.get("s1").toString()));
			if(index!=-1){
				holder.s1.setBackgroundColor(subBgColor.get(index));
			}else{
				int currentBg=getCurrentBg();
				holder.s1.setBackgroundColor(currentBg);
				subject.add(separateSub(map.get("s1").toString()));
				subBgColor.add(currentBg);
			}
		}else{
			holder.s1.setBackgroundColor(color.transparent);
		}
		if(!map.get("s2").equals("")){
			int index=subject.indexOf(separateSub(map.get("s2").toString()));
			if(index!=-1){
				holder.s2.setBackgroundColor(subBgColor.get(index));
			}else{
				int currentBg=getCurrentBg();
				holder.s2.setBackgroundColor(currentBg);
				subject.add(separateSub(map.get("s2").toString()));
				subBgColor.add(currentBg);
			}
		}else{
			holder.s2.setBackgroundColor(color.transparent);
		}
		if(!map.get("s3").equals("")){
			int index=subject.indexOf(separateSub(map.get("s3").toString()));
			if(index!=-1){
				holder.s3.setBackgroundColor(subBgColor.get(index));
			}else{
				int currentBg=getCurrentBg();
				holder.s3.setBackgroundColor(currentBg);
				subject.add(separateSub(map.get("s3").toString()));
				subBgColor.add(currentBg);
			}
		}else{
			holder.s3.setBackgroundColor(color.transparent);
		}
		if(!map.get("s4").equals("")){
			int index=subject.indexOf(separateSub(map.get("s4").toString()));
			if(index!=-1){
				holder.s4.setBackgroundColor(subBgColor.get(index));
			}else{
				int currentBg=getCurrentBg();
				holder.s4.setBackgroundColor(currentBg);
				subject.add(separateSub(map.get("s4").toString()));
				subBgColor.add(currentBg);
			}
		}else{
			holder.s4.setBackgroundColor(color.transparent);
		}
		if(!map.get("s5").equals("")){
			int index=subject.indexOf(separateSub(map.get("s5").toString()));
			if(index!=-1){
				holder.s5.setBackgroundColor(subBgColor.get(index));
			}else{
				int currentBg=getCurrentBg();
				holder.s5.setBackgroundColor(currentBg);
				subject.add(separateSub(map.get("s5").toString()));
				subBgColor.add(currentBg);
			}
		}else{
			holder.s5.setBackgroundColor(color.transparent);
		}
		if(!map.get("s6").equals("")){
			int index=subject.indexOf(separateSub(map.get("s6").toString()));
			if(index!=-1){
				holder.s6.setBackgroundColor(subBgColor.get(index));
			}else{
				int currentBg=getCurrentBg();
				holder.s6.setBackgroundColor(currentBg);
				subject.add(separateSub(map.get("s6").toString()));
				subBgColor.add(currentBg);
			}
		}else{
			holder.s6.setBackgroundColor(color.transparent);
		}
		if(!map.get("s7").equals("")){
			int index=subject.indexOf(map.get("s7").toString());
			if(index!=-1){
				holder.s7.setBackgroundColor(subBgColor.get(index));
			}else{
				int currentBg=getCurrentBg();
				holder.s7.setBackgroundColor(currentBg);
				subject.add(separateSub(map.get("s7").toString()));
				subBgColor.add(currentBg);
			}
		}else{
			holder.s7.setBackgroundColor(color.transparent);
		}
	}
	
	//分离课程和教室
	public String getWeekClass(String param){
		if(param.length()>10){
			String[] data=param.split("\\|");
			return data[0]+"-"+data[1];
		}else{
			return "";
		}
	}
	
	
	static class ViewHolder{
		MyTextView title;
		TextView s1;
		TextView s2;
		TextView s3;
		TextView s4;
		TextView s5;
		TextView s6;
		TextView s7;
	}
	
	


}
