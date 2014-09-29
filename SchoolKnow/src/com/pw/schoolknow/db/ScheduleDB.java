package com.pw.schoolknow.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pw.schoolknow.helper.TermHelper;
import com.pw.schoolknow.utils.JsonUtil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ScheduleDB {
	
	public static final String DBNAME = "schedule.db";
	public static final String TB_SYLLABUS = "syllabus";    //课程表
	public static final String TB_SYMANAGE = "syManage";    //课程表管理
	private SQLiteDatabase db;
	
	public Context context;
	
	private static ScheduleDB scheduleDB;
	
	public ScheduleDB(Context context,String tableName){
		db = context.openOrCreateDatabase(DBNAME, Context.MODE_PRIVATE,
				null);
		this.context=context;
		if(tableName.equals(TB_SYLLABUS)){
			db.execSQL("CREATE table IF NOT EXISTS _"+ tableName+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"classid varchar(15),term varchar(10),week char(1),s1 varchar(300),s2 varchar(300),"+
					"s3 varchar(300),s4 varchar(300),s5 varchar(300))");
		}else if(tableName.equals(TB_SYMANAGE)){
			db.execSQL("CREATE table IF NOT EXISTS _"+ tableName+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
					"classid VARCHAR(15),classname VARCHAR(30),term VARCHAR(20))");
		}
		
	}
	
	/**
	 * 获取操作课程表数据库对象
	 * @param context
	 * @return
	 */
	public static ScheduleDB getSYLLABUSObj(Context context){
		if(scheduleDB==null){
			scheduleDB=new ScheduleDB(context, ScheduleDB.TB_SYLLABUS);
		}
		return scheduleDB;
	}
	
	/**
	 * 插入课程表
	 * @param classId
	 * @param term
	 * @param week
	 * @param map
	 */
	public void insertSY(String classId,String term,String week,Map<String,Object> map){
		db.execSQL(
				"insert into _"
						+ TB_SYLLABUS
						+ " (classid,term,week,s1,s2,s3,s4,s5) values(?,?,?,?,?,?,?,?)",
				new Object[] {classId, term,week,
						map.get("s1"),map.get("s2"),map.get("s3"),map.get("s4"),map.get("s5")});
	}
	
	/**
	 * json插入课程表
	 * @param classId
	 * @param term
	 * @param json
	 */
	public void insertSY(String classId,String term,String json){
		List<Map<String,Object>> list=JsonUtil.parseJson(json,new String[]{"s1","s2","s3","s4","s5"},
				new String[]{"","","","",""});
		if(list.size()>0){
			for(int i=0;i<7;i++){
				Map<String,Object> map=list.get(i);
				insertSY(classId,term,String.valueOf(i),map);
			}
		}
	}
	
	
	/**
	 * 从教务处更新数据库
	 * @param classId
	 * @param term
	 * @param json
	 */
	public void updateSchedule(String classId,String term,String json){
		db.execSQL("delete from _" +TB_SYLLABUS+" where classid=? and term=?",new String[]{classId,term});
		insertSY(classId,term,json);
	}
	
	/**
	 * 从数据库读出课程表数据
	 * @param classId
	 * @param term
	 * @param week
	 * @return
	 */
	public List<Map<String,Object>> getSchedule(String classId,String term,String week){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Cursor c = db.rawQuery("SELECT * from _" + TB_SYLLABUS +" where classid=? and term=? and  week="+week,
				new String[]{classId,term});
		while (c.moveToNext()) {
			String[] s = {c.getString(c.getColumnIndex("s1")),c.getString(c.getColumnIndex("s2")),
					c.getString(c.getColumnIndex("s3")),c.getString(c.getColumnIndex("s4")),
					c.getString(c.getColumnIndex("s5"))};
			for(int i=0;i<s.length;i++){
				Map<String,Object> map=new HashMap<String, Object>();
				String[] temp=s[i].split("\\|");
				switch(temp.length){
				case 1:
					map.put("p1","");
					map.put("p2","");
					map.put("p3","");
					map.put("p4","");
					map.put("p5","");
					map.put("p6","");
					break;
				case 3:
					map.put("p1"," "+temp[0]);
					map.put("p2","  "+temp[1]);
					map.put("p3","  "+temp[2]);
					map.put("p4","");
					map.put("p5","");
					map.put("p6","");
					break;
				case 6:
					map.put("p1"," "+temp[0]);
					map.put("p2","  "+temp[1]);
					map.put("p3","  "+temp[2]);
					map.put("p4"," "+temp[3]);
					map.put("p5","  "+temp[4]);
					map.put("p6","  "+temp[5]);
					break;
				default:
					break;
				}
				map.put("p7",(i+1)*2-1+"-"+((i+1)*2));
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 周视图数据
	 * @param classId
	 * @param term
	 * @return
	 */
	public List<Map<String,String>>  getWeekSchedule(String classId,String term){
		if(getSyManage().size()==0){
			return null;
		}
		List<Map<String,String>> list=new ArrayList<Map<String,String>>();
		for(int i=0;i<5;i++){
			Cursor c = db.rawQuery("SELECT * from _" + TB_SYLLABUS +" where classid=? and term=?",
					new String[]{classId,term});
			int k=1;
			Map<String,String> map=new HashMap<String, String>();
			while (c.moveToNext()) {
				String[] s = {c.getString(c.getColumnIndex("s1")),c.getString(c.getColumnIndex("s2")),
						c.getString(c.getColumnIndex("s3")),c.getString(c.getColumnIndex("s4")),
						c.getString(c.getColumnIndex("s5"))};
				map.put("s"+k, s[i]);
				k++;
			}
			list.add(map);
		}
		return list;
		
	}
	
	/**
	 * 修改课程表
	 * @param classid
	 * @param term
	 * @param subject
	 * @param week
	 * @param s
	 */
	public void updateDaySchedule(String classId,String term,String subject,String week,String s){
		String weekDay=(Integer.parseInt(week)-1)+"";
		Cursor c = db.rawQuery("SELECT * from  _" + TB_SYLLABUS +" where classid=? and term=? and  week="+weekDay,
				new String[]{classId,term});
		String updateSubject="";
		while (c.moveToNext()) {
			String[] arr = {c.getString(c.getColumnIndex("s1")),c.getString(c.getColumnIndex("s2")),
					c.getString(c.getColumnIndex("s3")),c.getString(c.getColumnIndex("s4")),
					c.getString(c.getColumnIndex("s5"))};
			updateSubject=arr[Integer.parseInt(s)-1];
		}
		String temp_s="";
		switch(Integer.parseInt(s)){
		case 1:
			temp_s="s1";
			break;
		case 2:
			temp_s="s2";
			break;
		case 3:
			temp_s="s3";
			break;
		case 4:
			temp_s="s4";
			break;
		case 5:
			temp_s="s5";
			break;
		default:
			break;
		}
		String[] count=updateSubject.split("\\|");
		if(updateSubject.trim().equals("")){
			updateSubject=subject;
		}else if(updateSubject.indexOf(subject)!=-1||count.length>=6){
			return;
		}else{
			updateSubject=updateSubject+"|"+subject;
		}
		db.execSQL("update _"+TB_SYLLABUS+" set "+temp_s+"='"+updateSubject+"' where classid=? and term=? and week="
		+weekDay,new String[]{classId,term});
	}
	
	/**
	 * 获得单节课程表
	 * @param classId 
	 * @param term 学期
	 * @param week 第几周(0开头)
	 * @param day 第几节(0开头)
	 * @return
	 */
	public String getSingleSchedule(String classId,String term,int week,int day){
		Cursor c = db.rawQuery("SELECT * from _" + TB_SYLLABUS +" where classid=? and term=? and  week="+week,
				new String[]{classId,term});
		String subject=null;
		while (c.moveToNext()) {
			String[] s = {c.getString(c.getColumnIndex("s1")),c.getString(c.getColumnIndex("s2")),
					c.getString(c.getColumnIndex("s3")),c.getString(c.getColumnIndex("s4")),
					c.getString(c.getColumnIndex("s5"))};
			if(s[day]!=null){
				subject=s[day];
			}
		}
		return subject;
	}
	
	

	////////////////////////////课程表管理///////////////////////////////////
	
	/**
	 * 插入到课程表管理
	 * @param classid
	 * @param name
	 * @param term
	 */
	public void insertManage(String classid,String name,String term){
		db.execSQL(
				"insert into _"
						+ TB_SYMANAGE
						+ " (classid,classname,term) values(?,?,?)",
				new Object[] {classid,name,term});
	}
	/**
	 * 删除课程表管理
	 * @param classid
	 * @param term
	 */
	public void deleteManage(String classid,String term){
		db.execSQL("delete from _"+TB_SYMANAGE
				+"  where classid=? and term=?",
				new String[]{classid,term});
	}
	
	/**
	 * 删除课程表
	 * @param classid
	 * @param term
	 */
	public void deleteSchedule(String classid,String term){
		db.execSQL("delete from _"+TB_SYLLABUS
				+"  where classid=? and term=?",
				new String[]{classid,term});
	}
	
	/**
	 * 是否存在
	 * @param classid
	 * @param term
	 * @return
	 */
	public boolean existManage(String classid,String term){
		Cursor c = db.rawQuery("SELECT * from _" + TB_SYMANAGE +" where classid=? and term=? limit 1",
				new String[]{classid,term});
		return c.moveToNext()?true:false;
	}
	
	/**
	 * 获取本地的所有数据库
	 * @return
	 */
	public List<Map<String,Object>> getSyManage(){
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		Cursor c = db.rawQuery("SELECT * from _" + TB_SYMANAGE,null);
		while (c.moveToNext()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("classid",c.getString(c.getColumnIndex("classid")));
			map.put("classname",c.getString(c.getColumnIndex("classname")));
			map.put("term",TermHelper.getStringTerm3(c.getString(c.getColumnIndex("term"))));
			map.put("term_temp",c.getString(c.getColumnIndex("term")));
			list.add(map);
		}
		return list;	
	}
 
}
