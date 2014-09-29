package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.ScheduleWeekAdapter;
import com.pw.schoolknow.db.ScheduleDB;
import com.pw.schoolknow.helper.ScheduleHelper;
import com.pw.schoolknow.widgets.MyTextView;

public class ScheduleWeek extends Activity {
	
	private List<Map<String,String>> list;
	private ListView lv;
	
	private LinearLayout titleLayput;
	public String titleData[]={"一","二","三","四","五","六","七"};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_schedule_week);
		
		lv=(ListView) super.findViewById(R.id.schedule_week_lv);
		
		titleLayput=(LinearLayout) super.findViewById(R.id.schedule_week_title_layout);
		LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		param.weight=1;
		param.gravity=Gravity.CENTER_HORIZONTAL;
		for(int i=0;i<7;i++){
			MyTextView title=new MyTextView(this);
			title.setText(titleData[i]);
			title.setTextColor(Color.parseColor("#959595"));
			title.setGravity(Gravity.CENTER_HORIZONTAL);
			if(i==0){
				title.setLeftBorder(false);
			}else{
				title.setLeftBorder(true);
			}
			title.setPadding(0, 3,0,3);
			titleLayput.addView(title,param);
		}
		
		ScheduleHelper sh=new ScheduleHelper(ScheduleWeek.this);;
		String t_classid=sh.getCurrentScheduleClassId();
		String t_term=sh.getCurrentScheduleTerm();
		list=new ScheduleDB(this, ScheduleDB.TB_SYLLABUS).getWeekSchedule(t_classid, t_term);
		if(list==null||list.size()==0){
			return;
		}
		lv.setAdapter(new ScheduleWeekAdapter(this, list));
		
		
		
	}


}
