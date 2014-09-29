package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.List;

import com.pw.schoolknow.base.SchoolFellowBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


/**
 * 缓存花椒社区数据
 * @author peng
 *
 */
public class SchoolfellowDB {
	
	public static final String MSG_DBNAME = "userdata.db";
	private SQLiteDatabase db;
	private String tableName="_schoolfellow";
	
	public static SchoolfellowDB obj;
	public static SchoolfellowDB getInstance(Context context){
		if(obj==null){
			obj=new SchoolfellowDB(context);
		}
		return obj;
	}
	
	private SchoolfellowDB(Context context){
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS "+tableName
				+ " (id INTEGER PRIMARY KEY, uid varchar(50),nick varchar(20),time varchar(20),content text,"+
				"commentNum varchar(5),likeNum varchar(5))");
	}
	
	
	//保存用户信息
	public void save(SchoolFellowBase item){
		if(exist(item)){
			db.execSQL("update "+tableName +" set nick=?,time=?,content=?,commentNum=?,likeNum=? where id="+item.getId()+"",
					new Object[]{item.getNickname(),item.getTime(),item.getContent(),item.getCommentNum(),
				item.getLikeNum()});
		}else{
			db.execSQL("insert into "+tableName+" (id,uid,nick,time,content,commentNum,likeNum) values (?,?,?,?,?,?,?)",
					new Object[]{item.getId(),item.getUid(),item.getNickname(),item.getTime(),
					item.getContent(),item.getCommentNum(),item.getLikeNum()});
		}
	}
	
	public List<SchoolFellowBase> getList(){
		List<SchoolFellowBase> list=new ArrayList<SchoolFellowBase>();
		Cursor c = db.rawQuery("SELECT * from "+tableName+" order by id desc limit 10",null);
		while (c.moveToNext()) {
			SchoolFellowBase item=new SchoolFellowBase(
					c.getInt(c.getColumnIndex("id")),
					c.getString(c.getColumnIndex("uid")),c.getString(c.getColumnIndex("nick")),
					c.getString(c.getColumnIndex("time")),c.getString(c.getColumnIndex("content")),
					c.getString(c.getColumnIndex("commentNum")),c.getString(c.getColumnIndex("likeNum")));
			list.add(item);
		}
		return list;
	}
	
	//是否存在该记录
	public boolean exist(SchoolFellowBase item){
		Cursor c = db.rawQuery("select * from "+tableName+" where id="+item.getId()+" limit 1",null);
		int count = c.getCount();
		c.close();
		return count==0?false:true;
	}
	
	
	

}
