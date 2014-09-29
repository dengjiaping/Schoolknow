package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pw.schoolknow.base.InformBase;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.utils.Sha1Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InformDB {
	
	public static final String MSG_DBNAME = "inform.db";
	private SQLiteDatabase db;
	private String uid;
	
	public static InformDB informDB;
	
	public static InformDB getInstance(Context context,String uid){
		if(informDB==null){
			informDB=new InformDB(context,uid);
		}
		return informDB;
	}
	
	public InformDB(Context context,String uid) {
		this.uid=Sha1Util.encode(uid).substring(5,20);
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS _"+ this.uid
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,type varchar(2),uid varchar(30),name varchar(20),"+
				"time varchar(15),read char(1),targetid varchar(15) null )");
	}
	
	//插入通知
	public void saveInform(InformBase item){
		db.execSQL("insert into _"+ uid
						+ " (type,uid,name,time,read,targetid) values (?,?,?,?,?,?)",
				new Object[] {item.getType(),item.getSendUid(),item.getSendName(),
								item.getTime(),'0',item.getTargetId()});
	}
    public void saveInform(InformBase item,String read){
			db.execSQL("insert into _"+ uid
							+ " (type,uid,name,time,read,targetid) values (?,?,?,?,?,?)",
					new Object[] {item.getType(),item.getSendUid(),item.getSendName(),
									item.getTime(),read,item.getTargetId()});
		}
	
	//获取未读的消息
	public List<InformBase> getList(){
		List<InformBase> list=new ArrayList<InformBase>();
		Cursor c = db.rawQuery("SELECT * from _" + uid
				+ " where read='0' ORDER BY _id DESC" , null);
		while (c.moveToNext()) {
			InformBase item=new InformBase(
					c.getInt(c.getColumnIndex("_id")),
					Integer.parseInt(c.getString(c.getColumnIndex("type"))),
					c.getString(c.getColumnIndex("uid")),
					c.getString(c.getColumnIndex("name")),
					"",
					c.getString(c.getColumnIndex("time")),
					c.getString(c.getColumnIndex("targetid"))
			);
			
			list.add(item);	
		}
		return list;
	}
	
	
	/**
	 * 获取所有添加好友请求
	 * @return
	 */
	public List<Map<String,Object>> getAddRequest(){
		List<Map<String,Object>>  list=new ArrayList<Map<String,Object>>();
		Cursor c = db.rawQuery("SELECT * from _" + uid
				+ " where type='"+InformConfig.PUSH_ADDREQUEST+"' ORDER BY _id DESC" , null);
		while (c.moveToNext()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("id",String.valueOf(c.getInt(c.getColumnIndex("_id"))));
			map.put("nick",c.getString(c.getColumnIndex("name")));
			map.put("uid",c.getString(c.getColumnIndex("uid")));
			map.put("state",c.getString(c.getColumnIndex("read")));
			map.put("time",c.getString(c.getColumnIndex("time")));
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 设置好友请求结果
	 * 0未读 1同意 2忽略 other拒绝
	 * @param requestCode
	 */
	public void setAddQeest(int requestCode,int id){
		db.execSQL("update _"+ uid
				+ "  set read='"+requestCode+"' where _id="+id);
	}
	
	
	//获取未读的消息
		public List<InformBase> getAllList(){
			List<InformBase> list=new ArrayList<InformBase>();
			Cursor c = db.rawQuery("SELECT * from _" + uid
					+ " ORDER BY _id DESC" , null);
			while (c.moveToNext()) {
				InformBase item=new InformBase(
						c.getInt(c.getColumnIndex("_id")),
						Integer.parseInt(c.getString(c.getColumnIndex("type"))),
						c.getString(c.getColumnIndex("uid")),
						c.getString(c.getColumnIndex("name")),
						"",
						c.getString(c.getColumnIndex("time")),
						c.getString(c.getColumnIndex("targetid"))
				);
				
				list.add(item);	
			}
			return list;
		}
	
	//获取未读的消息数目
	public int getNewCount() {
		Cursor c = db.rawQuery("SELECT * from _" + uid+ " where read='0'",
				null);
		int count = c.getCount();
		c.close();
		return count;
	}
	
	
	/**
	 * 设置消息已读
	 * @param id
	 */
	public void setRead(int id){
		db.execSQL("update _"+ uid
				+ "  set read='1' where _id="+id);
	}
	
	

}
