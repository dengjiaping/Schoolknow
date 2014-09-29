package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 缓存跳蚤市场数据
 * @author peng
 *
 */
public class MarketTzDB {
	
	public static final String MSG_DBNAME = "userdata.db";
	private SQLiteDatabase db;
	private String tableName="_market_list";
	
	public static MarketTzDB obj;
	public static MarketTzDB getInstance(Context context){
		if(obj==null){
			obj=new MarketTzDB(context);
		}
		return obj;
	}
	
	private MarketTzDB(Context context){
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS "+tableName
				+ " (id INTEGER PRIMARY KEY,title varchar(50),time varchar(20),imgurl varchar(100))");
	}
	
	
	//保存用户信息
	public void save(Map<String,Object> map){
		if(exist(map)){
			db.execSQL("update "+tableName +" set title=?,time=?,imgurl=? where id="+
					Integer.parseInt(map.get("id").toString())+"",
					new Object[]{map.get("title"),map.get("time"),map.get("imgurl")});
		}else{
			db.execSQL("insert into "+tableName+" (id,title,time,imgurl) values (?,?,?,?)",
					new Object[]{map.get("id"),map.get("title"),map.get("time"),map.get("imgurl")});
		}
	}
	
	public void save(List<Map<String,Object>> list){
		for(Map<String,Object> map:list){
			save(map);
		}
	}
	
	public List<Map<String,Object>> getList(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Cursor c = db.rawQuery("SELECT * from "+tableName+" order by id desc limit 10",null);
		while (c.moveToNext()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("id", c.getInt(c.getColumnIndex("id")));
			map.put("title", c.getString(c.getColumnIndex("title")));
			map.put("time", c.getString(c.getColumnIndex("time")));
			map.put("imgurl", c.getString(c.getColumnIndex("imgurl")));
			list.add(map);
		}
		return list;
	}
	
	//是否存在该记录
	public boolean exist(Map<String,Object> map){
		Cursor c = db.rawQuery("select * from "+tableName+" where id="
	+Integer.parseInt(map.get("id").toString())+" limit 1",null);
		int count = c.getCount();
		c.close();
		return count==0?false:true;
	}
	
	
	

}
