package com.pw.schoolknow.helper;

import com.pw.schoolknow.service.NotifyService;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class NotifyHelper {
	public SharedPreferences share;
	public SharedPreferences.Editor edit=null;
	private Context context;
	private static final String FILENAME="Notify";
	
	@SuppressLint("CommitPrefEdits")
	public NotifyHelper(Context context){
		this.context=context;
		this.share=this.context.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		this.edit=share.edit();
	}
	
	public void save(String ticker,String title,String content){
		edit.putString("ticker",ticker);
		edit.putString("title",title);
		edit.putString("content",content);
		edit.commit();
	}
	
	public void saveType(int type){
		edit.putInt("type", type);
		edit.commit();
	}
	
	public int getType(){
		return share.getInt("type", NotifyService.NOTIFY_INFROM);
	}
	
	public String getTicker(){
		return share.getString("ticker","");
	}
	public String getTitle(){
		return share.getString("title","");
	}
	public String getContent(){
		return share.getString("content","");
	}
	
	public void clear(){
		edit.clear();
	}

}
