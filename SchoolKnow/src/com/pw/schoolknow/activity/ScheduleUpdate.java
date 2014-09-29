package com.pw.schoolknow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.db.ScheduleDB;
import com.pw.schoolknow.helper.ScheduleHelper;


/**
 * 修改课程表
 * @author wei8888go
 *
 */
public class ScheduleUpdate extends BaseActivity {
	
	@ViewInject(R.id.schedule_update_layout)  
	private LinearLayout layout;
	 
	 public int week=0;
	 public int day=0;
	 
	 public Context mcontext;
	 public ScheduleHelper sh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_schedule_update);
		setTitle("课程详情");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		mcontext=this;
		sh=new ScheduleHelper(this);
		
		ViewUtils.inject(this);
		
		Intent it=getIntent();
		week=it.getIntExtra("week", 0);
		day=it.getIntExtra("day", 0);
		
		String subject=ScheduleDB.getSYLLABUSObj(this).getSingleSchedule(
				sh.getCurrentScheduleClassId(), sh.getCurrentScheduleTerm(), week, day);
		
		String[] tempSubject=subject.split("\\|");
		switch(tempSubject.length){
		case 1:
			break;
		case 3:
			addManage(layout,new String[]{separateWeek(tempSubject[2]),tempSubject[0],tempSubject[1],tempSubject[2]});
			break;
		case 6:
			addManage(layout,new String[]{separateWeek(tempSubject[2]),tempSubject[0],tempSubject[1],tempSubject[2]});
			addManage(layout,new String[]{separateWeek(tempSubject[5]),tempSubject[3],tempSubject[4],tempSubject[5]});
			break;
		default:
			break;
		}
		
		//T.showShort(MyApplication.getInstance(), week+"#"+day);
		
	}
	
	
	/**
	 * 课程区分单双周
	 * @param str
	 * @return
	 */
	public String separateWeek(String str){
		if(str.indexOf("[双]")!=-1){
			return "双周";
		}else if(str.indexOf("[单]")!=-1){
			return "单周";
		}else{
            return "每周";
		}
	}

	public void addManage(LinearLayout layout,String[] param){
		View manageContent=null;
		LayoutInflater inflater=LayoutInflater.from(mcontext);
		manageContent=inflater.inflate(R.layout.part_schedule_update_item, null, false);
		TextView title=(TextView) manageContent.findViewById(R.id.schedule_update_title);
		TextView sub=(TextView) manageContent.findViewById(R.id.schedule_update_subject);
		TextView tea=(TextView) manageContent.findViewById(R.id.schedule_update_teacher);
		TextView room=(TextView) manageContent.findViewById(R.id.schedule_update_classroom);
		TextView week=(TextView) manageContent.findViewById(R.id.schedule_update_week);
		TextView jieci=(TextView) manageContent.findViewById(R.id.schedule_update_jieci);
		if(param.length==4){
			title.setText(param[0]);
			sub.setText("课程:"+param[1]);
			
			String[] temp_class=param[2].split(" ");
			switch(temp_class.length){
			case 0:
				break;
			case 1:
				tea.setText("老师:"+temp_class[0]);
				break;
			case 2:
				tea.setText("老师:"+temp_class[0]);
				week.setText("教室:"+temp_class[1]);
				break;
			default:
				tea.setText("教室:"+param[2]);
				break;
			}
			String[] temp_week=param[3].split(" ");
			switch(temp_week.length){
			case 0:
				break;
			case 1:
				room.setText("节次:"+param[3]);
				break;
			case 2:
				room.setText("星期:"+temp_week[0]);
				jieci.setText("节次:"+temp_week[1]);
				break;
			default:
				room.setText("节次:"+param[3]);
				break;
			}
			layout.addView(manageContent);
		}
		
		
	}
	
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		default:
			break;
		}
	}

}
