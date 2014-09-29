package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pw.schoolknow.base.MessageItem;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.Sha1Util;

public class ChatMessageDB {
	
	public static final String MSG_DBNAME = "chat.db";
	private SQLiteDatabase db;
	private String uid;
	
	public LoginHelper lh;
	public Context context;
	
	public static ChatMessageDB obj;
	public static ChatMessageDB getInstance(Context context, String uid){
		if(obj==null){
			obj=new ChatMessageDB(context,uid);
		}
		return obj;
	}
	
	private  ChatMessageDB(Context context, String uid) {
		this.uid=Sha1Util.encode(uid).substring(5,20);
		lh=new LoginHelper(context);
		this.context=context;
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS  _"+ this.uid
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,type varchar(1),uid varchar(50),"+
				"content varchar(1000),time varchar(15),iscome varchar(1),isread varchar(1))");
	}
	
	
	/**
	 * 插入聊天记录
	 * @param type
	 * @param uid
	 * @param content
	 * @param time
	 * @param iscome
	 * @param isread
	 */
	public void insert(int type,String uid,String content,long time,String iscome,String isread){
		db.execSQL("insert into _"+this.uid+" (type,uid,content,time,iscome,isread) values (?,?,?,?,?,?)",
				new Object[]{String.valueOf(type),
				uid,content,String.valueOf(time),iscome,isread});
	}
	
	
	/**
	 * 获取聊天记录
	 * @param uid
	 * @param num
	 * @return
	 */
	public List<MessageItem> getList(String uid,int num){
		List<MessageItem> list=new ArrayList<MessageItem>();
		Cursor c = db.rawQuery("select * from ( SELECT * from _"+this.uid+" where uid='"+uid+
				"' order by _id desc limit "+num+" )as a order by _id asc",null);
		while (c.moveToNext()) {
			boolean isCome=(c.getString(c.getColumnIndex("iscome"))).equals("1")?true:false;
			String userId=isCome?c.getString(c.getColumnIndex("uid")):lh.getUid();
			MessageItem item=new MessageItem(
					 c.getInt(c.getColumnIndex("_id")),
					Integer.parseInt(c.getString(c.getColumnIndex("type"))),
					Long.parseLong(c.getString(c.getColumnIndex("time"))), 
					c.getString(c.getColumnIndex("content")),userId, isCome);
			list.add(item);
		}
		return list;
	}
	
	/**
	 * 获取聊天记录
	 */
	public List<Map<String,Object>> getChatRecords(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Cursor c = db.rawQuery("select distinct uid from _"+this.uid,null);
		while (c.moveToNext()) {
			String userId=c.getString(c.getColumnIndex("uid"));
			String userName=UserDB.getInstance(context).getUserName(userId);
			if(userName.trim().length()!=0){
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("uid", userId);
				map.put("nick", userName);
				map.put("num",0);
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 获取未读消息数目
	 * @return
	 */
	public int getNOReadNum(){
		Cursor c = db.rawQuery("select * from _"+this.uid+" where isread='0'",null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	/**
	 * 设置消息已读
	 * @param uid
	 */
	public void setRead(String uid){
		db.execSQL("update  _"+this.uid+" set isread='1' where uid='"+uid+"'");
	}
	
	

}
