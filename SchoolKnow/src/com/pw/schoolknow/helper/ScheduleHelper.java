package com.pw.schoolknow.helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


/**
 * 保存配置信息
 * @author wei8888go
 *
 */
public class ScheduleHelper {
	
	public SharedPreferences share;
	public SharedPreferences.Editor edit=null;
	private Context context;
	private static final String FILENAME="Schedule";
	
	@SuppressLint("CommitPrefEdits")
	public ScheduleHelper(Context context){
		this.context=context;
		this.share=this.context.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		this.edit=share.edit();
	}
	
	/**
	 * 设置当前课程表班级号
	 */
	public void setCurrentScheduleClassId(String str){
		edit.putString("classId",str);
		edit.commit();
	}
	
	/**
	 * 获取当前课程表班级号
	 */
	
	public String getCurrentScheduleClassId(){
		return share.getString("classId","");
	}
	
	/**
	 * 设置当前课程表学期
	 */
	public void setCurrentScheduleTerm(String str){
		edit.putString("Term",str);
		edit.commit();
	}
	
	/**
	 * 获取当前课程表学期
	 */
	
	public String getCurrentScheduleTerm(){
		return share.getString("Term","");
	}
	
	//设置周视图
	public void setWeekView(Boolean b){
		edit.putBoolean("WeekView", b);
		edit.commit();
	}
	
	//是否为周视图
	public Boolean getWeekView(){
		return share.getBoolean("WeekView", false);
	}

}
