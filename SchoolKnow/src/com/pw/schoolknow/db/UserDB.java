package com.pw.schoolknow.db;

import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.base.UserBase;
import com.pw.schoolknow.config.ServerConfig;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDB {
	
	public static final String MSG_DBNAME = "friend.db";
	private SQLiteDatabase db;
	private String tableName="_user";
	
	public static UserDB obj;
	public static UserDB getInstance(Context context){
		if(obj==null){
			obj=new UserDB(context);
		}
		return obj;
	}
	
	private UserDB(Context context){
		db = context.openOrCreateDatabase(MSG_DBNAME, Context.MODE_PRIVATE,
				null);
		db.execSQL("CREATE table IF NOT EXISTS "+tableName
				+ " (uid varchar(50) PRIMARY KEY,sex varchar(2),stuid varchar(15) null,nick varchar(20),"+
				"client varchar(20),college varchar(2),lastime varchar(20) null)");
	}
	
	//保存用户信息
	public void save(UserBase item){
		if(exist(item)){
			db.execSQL("update "+tableName +" set sex=?,stuid=?,nick=?,client=?,college=? where uid='"+item.getEmail()+"'",
					new Object[]{item.getSex(),item.getStuid(),item.getNick(),
				item.getClient(),item.getCollege()});
		}else{
			db.execSQL("insert into "+tableName+" (uid,sex,stuid,nick,client,college) values (?,?,?,?,?,?)",
					new Object[]{item.getEmail(),item.getSex(),item.getStuid(),item.getNick(),
					item.getClient(),item.getCollege()});
		}
	}
	
	public UserBase getUserInfo(String uid){
		Cursor c = db.rawQuery("SELECT * from "+tableName+" where uid=? limit 1",new String[]{uid});
		UserBase item=null;
		while (c.moveToNext()) {
			item=new UserBase(c.getString(c.getColumnIndex("uid")),c.getString(c.getColumnIndex("sex")),
					c.getString(c.getColumnIndex("stuid")),c.getString(c.getColumnIndex("nick")),
					c.getString(c.getColumnIndex("client")),c.getString(c.getColumnIndex("college")));
		}
		return item;
	}
	
	//是否存在该记录
	public boolean exist(UserBase item){
		Cursor c = db.rawQuery("select * from "+tableName+" where uid='"+item.getEmail()+"' limit 1",null);
		int count = c.getCount();
		c.close();
		return count==0?false:true;
	}
	
	//获取用户名
	public String getUserName(String uid){
		Cursor c = db.rawQuery("select * from "+tableName+" where uid='"+uid+"' limit 1",null);
		String userName="";
		while (c.moveToNext()) {
			userName=c.getString(c.getColumnIndex("nick"));
		}
		return userName;
	}
	
	/**
	 * 获取推送ID
	 * @param uid 用户Id
	 * @return
	 */
	public void getUser(final String uid,final getUserCallBack callback){
		UserBase user=getUserInfo(uid);
		
		HttpUtils http=new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid",uid);
		String requestPath=ServerConfig.HOST+"/schoolknow/Friend/api/getUserInfo.php";
		
		if(user==null){
			http.send(HttpMethod.POST, requestPath,params,
					new RequestCallBack<String>() {
						public void onFailure(HttpException arg0, String arg1) {}
						public void onSuccess(ResponseInfo<String> info) {
							UserBase base=ParseUserInfo(info.result,uid);
							if(base!=null){
								 callback.getUserInfo(base);
								 callback.getPushId(base.getClient());
							}
						}
					});
		}else{
			callback.getUserInfo(user);
			if(user.getClient().trim().length()==0){
				http.send(HttpMethod.POST,requestPath,params,
						new RequestCallBack<String>() {
							public void onFailure(HttpException arg0, String arg1) {}
							public void onSuccess(ResponseInfo<String> info) {
								UserBase base=ParseUserInfo(info.result,uid);
								if(base!=null){
									callback.getPushId(base.getClient());
								}
							}
						});
			}else{
				callback.getPushId(user.getClient());
			}
		}
	}
	
	/**
	 * 获取用户信息处理接口
	 * @author peng
	 *
	 */
	public interface getUserCallBack{
		/**
		 * 获取用户基本信息
		 * @param base
		 */
		void getUserInfo(UserBase base);
		/**
		 * 获取用户推送PushId
		 * @param PushId
		 */
		void getPushId(String PushId);
	}
	
	/**
	 * 从json中解析用户信息
	 * @param json
	 * @param uid
	 */
	public UserBase ParseUserInfo(String json,String uid){
		JSONObject JsonData;
		try {
			JsonData = new JSONObject(json);
			String client=JsonData.getString("client");
			UserBase base=new UserBase(uid, JsonData.getString("sex"),
		    		"", JsonData.getString("nickname"), client, JsonData.getString("college"));
		    save(base);
		    return base;
		} catch (JSONException e) {
			return null;
		}
		
	}
	

}
