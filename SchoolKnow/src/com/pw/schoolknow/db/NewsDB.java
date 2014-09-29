package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.List;

import com.pw.schoolknow.base.IndexItemBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NewsDB {
	
	public static final String MSG_DBNAME = "news.db";
	private SQLiteDatabase db;
	
	public NewsDB(Context context){
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS _news "
				+ "(id INTEGER,type varchar(1),title varchar(30),intro varchar(100),"+
				"address varchar(30),time long,param varchar(30) )");
	}
	
	//插入新闻
	public void insert(IndexItemBase item){
		db.execSQL(
				"insert into _news "
						+ "(id,type,title,intro,address,time,param) values(?,?,?,?,?,?,?)",
				new Object[] {item.getId(),item.getType(),item.getTitle(),
								item.getIntro(),item.getAddress(),item.getTimesamp(),item.getParam()});
	}
	
	/**
	 * 
	 * @return
	 */
	public List<IndexItemBase> getList(){
		List<IndexItemBase> list=new ArrayList<IndexItemBase>();
		Cursor c = db.rawQuery("SELECT * from  _news order by id desc limit 20",new String[]{});
		while (c.moveToNext()) {
			IndexItemBase item=new IndexItemBase(
					c.getInt(c.getColumnIndex("id")),Integer.parseInt(c.getString(c.getColumnIndex("type"))),
					c.getString(c.getColumnIndex("title")),
					c.getString(c.getColumnIndex("intro")),c.getString(c.getColumnIndex("address")),
					c.getLong(c.getColumnIndex("time")),c.getString(c.getColumnIndex("param")));
			list.add(item);
		}
		return list;
	}
	
	//是否已经存在id的新闻
	public boolean hasExistNews(int id){
		Cursor c = db.rawQuery("SELECT * from  _news where id='"+id+"'",null);
		int count = c.getCount();
		return count>0?true:false;
	}
	
	

}
