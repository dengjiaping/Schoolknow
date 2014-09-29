package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pw.schoolknow.base.UserBase;
import com.pw.schoolknow.utils.PinyinUtils;
import com.pw.schoolknow.utils.Sha1Util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 好友关系数据库
 * @author peng
 *
 */
public class FriendDB {
	
	public static final String MSG_DBNAME = "friend.db";
	private SQLiteDatabase db;
	private String uid;
	
	public static FriendDB obj;
	public static FriendDB getInstance(Context context, String uid){
		if(obj==null){
			obj=new FriendDB(context,uid);
		}
		return obj;
	}
	
	private  FriendDB(Context context, String uid) {
		this.uid=Sha1Util.encode(uid).substring(5,20);
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS _"+ this.uid
				+ " (frienduid varchar(50) PRIMARY KEY,nick varchar(20) null,remark varchar(10) null)");
	}
	
	//添加为好友
	public void add(String uid,String nick){
		if(!exist(uid)){
			db.execSQL("insert into _"+this.uid+" (frienduid,nick,remark) values (?,?,?)",new Object[]{uid,nick,""});
		}
	}
	
	public void delete(String uid){
		if(exist(uid)){
			db.execSQL("delete from _"+this.uid+" where frienduid='"+uid+"'");
		}
	}
	
	
	/**
	 * 获取好友列表
	 * @return
	 */
	public List<Map<String,Object>> getFriendList(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Cursor c = db.rawQuery("SELECT * from _"+this.uid,null);
		while (c.moveToNext()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name", c.getString(c.getColumnIndex("remark")));
			map.put("nickname", c.getString(c.getColumnIndex("nick")));
			map.put("mood",c.getString(c.getColumnIndex("frienduid")));
			map.put("uid",c.getString(c.getColumnIndex("frienduid")));
			map.put("py", PinyinUtils.getAlpha(c.getString(c.getColumnIndex("nick"))));
			list.add(map);
		}
		return list;
	}
	
	public UserBase[] getFriendArray(){
		Cursor c = db.rawQuery("SELECT * from _"+this.uid,null);
		UserBase[] res=new UserBase[c.getCount()];
		int i=0;
		while (c.moveToNext()) {
			UserBase item=new UserBase(c.getString(c.getColumnIndex("frienduid")), "1",
					"", c.getString(c.getColumnIndex("nick")),"", "");
			res[i]=item;
			i++;
		}
		return res;
	}
	
	
	//是否已经为好友
	public boolean exist(String uid){
		Cursor c = db.rawQuery("select * from _"+this.uid+" where frienduid='"+uid+"' limit 1",null);
		int count = c.getCount();
		c.close();
		return count==0?false:true;
	}
	
	

}
