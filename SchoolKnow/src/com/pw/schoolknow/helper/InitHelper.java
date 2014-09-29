package com.pw.schoolknow.helper;

import com.pw.schoolknow.base.CountdownItem;
import com.pw.schoolknow.db.CountdownDB;
import com.pw.schoolknow.utils.TimeUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class InitHelper {
	public SharedPreferences share;
	public SharedPreferences.Editor edit=null;
	private Context context;
	private static final String FILENAME="Init";
	
	@SuppressLint("CommitPrefEdits")
	public InitHelper(Context context){
		this.context=context;
		this.share=this.context.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		this.edit=share.edit();
	}
	
	//初始化
	public void install(){
		if(!hasInit()){
			this.InitCountDown();
			setInit(true);
		}
		
	}
	
	//初始化倒数日
	public void InitCountDown(){
		CountdownItem[] data=new CountdownItem[]{
				new CountdownItem("四级",TimeUtil.createtimesamp(2014,6,21,9,0),"北区15#机房"),
				new CountdownItem("六级",TimeUtil.createtimesamp(2014,6,21,9,0),"北区15#机房"),
				new CountdownItem("2013年结束",TimeUtil.createtimesamp(2013,12,31,23,59),"北区15#机房"),
				new CountdownItem("考研",TimeUtil.createtimesamp(2014,1,4,8,30),"北区15#机房"),
				new CountdownItem("2014春节",TimeUtil.createtimesamp(2014,1,31,0,0),""),
				new CountdownItem("情人节",TimeUtil.createtimesamp(2014,2,14,0,0),""),
				new CountdownItem("计算机等级考试",TimeUtil.createtimesamp(2014,3,29,10,0),""),
				new CountdownItem("软考",TimeUtil.createtimesamp(2014,5,24,9,0),""),
				new CountdownItem("高考",TimeUtil.createtimesamp(2014,6,7,9,0),"")
		};
		for(int i=0;i<data.length;i++){
			new CountdownDB(context).insert(data[i]);
		}
	}
	
	public void setInit(boolean b){
		edit.putBoolean("init", b);
		edit.commit();
	}
	
	public boolean hasInit(){
		return share.getBoolean("init",false);
	}
	
	/**
	 * 更新之前的版本号
	 * @return
	 */
	public int getLastVersion(){
		return share.getInt("version", 0);
	}
	
	/**
	 * 设置更新后的版本号
	 */
	public void setLastVersion(){
		edit.putInt("version", VersionHelper.getVerCode(context));
		edit.commit();
	}
	
	/**
	 * 覆盖安装出现引导页面,不重置本机数据
	 * @return
	 */
	public boolean checkHasInit(){
		if(VersionHelper.getVerCode(context)!=getLastVersion()){
			install();
			setLastVersion();
			return false;
		}
		return true;
	}
	
	
}
