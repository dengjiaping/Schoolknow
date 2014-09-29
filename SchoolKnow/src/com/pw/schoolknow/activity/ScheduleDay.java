package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.ScheduleAdapter;
import com.pw.schoolknow.adapter.ViewPagerAdapter;
import com.pw.schoolknow.db.ScheduleDB;
import com.pw.schoolknow.helper.ScheduleHelper;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.ClassSelect;
import com.pw.schoolknow.widgets.MyAlertMenu;

public class ScheduleDay extends Activity {
	
	private LinearLayout pageNav;
	private String[] week={"一","二","三","四","五","六","七"};
	private TextView[] tv=new TextView[week.length];
	private List<View> list;
	private ViewPager viewpager;
	private int current=TimeUtil.getDayOfWeek()-1;
	private String default_color="#2895DB";
	private String select_color="#ffffff";
	
	private ListView listv;
	List<Map<String,Object>> subList;
	
	
	String select_class_id="";
	String select_class_name="";
	String select_term="";
	
	public ClassSelect newClass=null;
	private ScheduleHelper sh;
	
	public MyAlertMenu scheduleUpdateMenu;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_schedule_day);
		sh=new ScheduleHelper(ScheduleDay.this);
		this.initPageNav();
		this.initPage();
		setPageNav(current);	
	}
	

	
	


	/**
	 * 分页导航
	 */
	public void initPageNav(){
		
		pageNav=(LinearLayout) super.findViewById(R.id.schedule_pagenav_layout);
		LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		param.weight=1;
		param.setMargins(15,0,15,0);
		for(int i=0;i<week.length;i++){
			tv[i]=new TextView(this);
			tv[i].setText(week[i]);
			tv[i].setTextColor(Color.WHITE);
			tv[i].setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
			pageNav.addView(tv[i],param);
			tv[i].setOnClickListener(new OnPageNavClick());
			tv[i].setTag(i);
		}
	}
	
	/**
	 * 载入内容
	 */
	public void initPage(){
		 viewpager=(ViewPager) super.findViewById(R.id.schedule_viewpager);
	     list=new ArrayList<View>();
	     for(int i=0;i<7;i++){
	        	list.add(scheduleView(i));
	     }
	     viewpager.setAdapter(new ViewPagerAdapter(this, list));
	     viewpager.setCurrentItem(current);
	     viewpager.setOnPageChangeListener(new OnPageChangeListenerImp());
	}
	
	//设置选择的星期
	public void setPageNav(int index){
		for(int i=0;i<week.length;i++){
			tv[i].setTextColor(Color.parseColor(default_color));
			tv[i].setBackgroundResource(0);
		}
		tv[index].setTextColor(Color.parseColor(select_color));
		tv[index].setBackgroundResource(R.drawable.pic_schedule_weeknav_select);
	}
	
	//星期切换事件
	public class OnPageNavClick implements OnClickListener{
		@Override
		public void onClick(View view) {
			current=(Integer) view.getTag();
			viewpager.setCurrentItem(current);
			setPageNav(current);
		}
		
	}
	
	
	/**
	 * 生成ListView
	 */
	public void createListView(){
		listv=new ListView(this);
		LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		listv.setLayoutParams(param);
		listv.setDivider(null);
		listv.setVerticalScrollBarEnabled(false);
		subList=new ArrayList<Map<String,Object>>();
		listv.setOnItemLongClickListener(new ScheduleItemClick());
	}
	
	/**
	 * 编辑课表
	 * @author wei8888go
	 *
	 */
	public class ScheduleItemClick implements OnItemLongClickListener{
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int position, long arg3) {
			Intent it=new Intent(ScheduleDay.this,ScheduleUpdate.class);
			it.putExtra("week", current);
			it.putExtra("day", position);
			startActivity(it);
			return false;
		}
		
	}
	
	/**
	 * 星期的课程
	 * @param weekday
	 * @return
	 */
	private View scheduleView(int weekday) {
		this.createListView();
		String t_classid=sh.getCurrentScheduleClassId();
		String t_term=sh.getCurrentScheduleTerm();
		subList=new ScheduleDB(this,ScheduleDB.TB_SYLLABUS).getSchedule(t_classid,t_term,String.valueOf(weekday));
		ScheduleAdapter adap=new ScheduleAdapter(ScheduleDay.this, subList,R.layout.item_shcedule,
				new String[]{"p1","p2","p3","p4","p5","p6","p7"},
				new int []{R.id.schedule_oneclass_subject,R.id.schedule_oneclass_teacher,
				R.id.schedule_oneclass_address,R.id.schedule_oneclass_subject_2,
				R.id.schedule_oneclass_teacher_2,R.id.schedule_oneclass_address_2,
				R.id.schedule_oneclass_time});
		listv.setAdapter(adap);
		return listv;		
	}
	
	/**
	 * 滑动事件处理
	 * @author wei8888go
	 *
	 */
	public class OnPageChangeListenerImp implements  OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int position) {
			setPageNav(position);
			current=position;
		}
		
	}
	
}
