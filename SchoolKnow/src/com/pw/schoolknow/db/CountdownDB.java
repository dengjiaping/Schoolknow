package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.List;

import com.pw.schoolknow.base.CountdownItem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CountdownDB {
	
	public static final String MSG_DBNAME = "countdown.db";
	private SQLiteDatabase db;
	
	public CountdownDB(Context context){
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS _countdown "
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,title varchar(30),time varchar(20),address varchar(30) )");
	}
	
	//插入内容
	public void insert(CountdownItem item){
		db.execSQL(
				"insert into _countdown "
						+ "(title,time,address) values(?,?,?)",
				new Object[] {item.getTitle(),item.getTime_samp(),item.getAddress()});
	}
	
	//更新
	public void update(CountdownItem item,int id){
		db.execSQL(
				"update _countdown "
						+ "set title='"+item.getTitle()+"',time="+item.getTime_samp()+",address='"
						+item.getAddress()+"' where _id="+id);
	}
	
	//删除
	public void delete(int id){
		db.execSQL(
				"delete from _countdown where _id="+id);
	}
	
	//获取所有列表
	public List<CountdownItem>  getList(){
		List<CountdownItem> list=new ArrayList<CountdownItem>();
		Cursor c = db.rawQuery("SELECT * from  _countdown",new String[]{});
		while (c.moveToNext()) {
			CountdownItem item=new CountdownItem(
					c.getInt(c.getColumnIndex("_id")),c.getString(c.getColumnIndex("title")),
					c.getLong(c.getColumnIndex("time")),c.getString(c.getColumnIndex("address")));
			list.add(item);
		}
		return list;
	}
	
	/**
	 * 显示在首页
	 * @return
	 */
	public CountdownItem getIndexTip(){
		long currentTime=System.currentTimeMillis();
		Cursor c = db.rawQuery("SELECT * from  _countdown where time>="+currentTime+" order by time asc limit 1"
				,new String[]{});
		while (c.moveToNext()){
			CountdownItem item=new CountdownItem(
					c.getInt(c.getColumnIndex("_id")),c.getString(c.getColumnIndex("title")),
					c.getLong(c.getColumnIndex("time")),c.getString(c.getColumnIndex("address")));
			return item;
		}
		return null;
	}
	
	
	
}
