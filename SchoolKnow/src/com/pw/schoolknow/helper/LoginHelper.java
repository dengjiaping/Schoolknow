package com.pw.schoolknow.helper;

import org.json.JSONException;
import org.json.JSONObject;

import com.pw.schoolknow.activity.Main;
import com.pw.schoolknow.utils.Sha1Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


public class LoginHelper{
	
	public SharedPreferences share;
	public SharedPreferences.Editor edit=null;
	private Context context;
	private static final String FILENAME="login";
	
	@SuppressLint("CommitPrefEdits")
	public LoginHelper(Context context){
		this.context=context;
		this.share=this.context.getSharedPreferences(FILENAME,
				Context.MODE_PRIVATE);
		this.edit=share.edit();
	}
	
	
	public boolean login(String jsonData){
		try {
			JSONObject JsonData = new JSONObject(jsonData);
			edit.putString("id",JsonData.getString("id"));
			edit.putString("token",JsonData.getString("token"));
			edit.putString("unique",JsonData.getString("unique"));
			edit.putString("headImg", JsonData.getString("head"));
			edit.putString("nickname",JsonData.getString("nick"));
			edit.putString("stuid", JsonData.getString("stuid"));
			edit.putString("im",JsonData.getString("im"));
			edit.putString("sex",JsonData.getString("sex"));
			edit.putString("last",System.currentTimeMillis()+"");
			String collegeId=JsonData.getString("college");
			edit.putString("college",CollegeHelper.getCollegeName(collegeId));
			edit.commit();
			return true;
			
		}catch (JSONException e) {
			e.printStackTrace();
		}
		return false;		
	}
	
	public boolean hasLogin(){
		if(share.contains("token")){
			String token=share.getString("token","");
			String id=share.getString("id","");
			String unique=share.getString("unique","");
			String im=share.getString("im","");
			if(token!=null&&!token.equals("")){
				String param1=id.substring(0,id.length()-5);
				String param2=unique.substring(3,unique.length());
				String param3=im.substring(2,im.length()-3+2);
				String param4="sk";
				
				String newData=param4+param2+param3+param1;
				String sha1Data=new Sha1Util().getDigestOfString(newData.getBytes());
				if(sha1Data.equals(token)){
					return true;
				}
			}
		}
		return false;		
	}
	
	//强制跳转登陆页面
	public void ToLogin(){
		Intent it=new Intent(context,Main.class);
		it.putExtra("param","2");
		context.startActivity(it);
	}
	
	public Boolean logout(){
		if(share.contains("token")){
			this.clear();
			return true;
		}
		return false;
	}
	
	public void clear(){
		edit.clear().commit();
	}
	
	public String getUid(){
		return share.getString("id","");
	}
	
	public String getNickname(){
		return share.getString("nickname","");
	}
	
	public void setNickname(String nick){
		edit.putString("nickname",nick);
		edit.commit();
	}
	
	public String getHeadImg(){
		return share.getString("nickname","");
	}
	
	public  String getStuId(){
		return share.getString("stuid","");
	}
	public  void setStuId(String stuid){
		edit.putString("stuid",stuid);
		edit.commit();
	}
	
	public  Boolean isBoy(){
		return share.getString("sex","").equals("男");
	}
	
	public  String getToken(){
		return share.getString("token","");
	}
	
	public  String getSex(){
		return share.getString("sex","");
	}
	public  void setSex(String sex){
		edit.putString("sex",sex);
		edit.commit();
	}
	
	public String getCollege(){
		return share.getString("college",CollegeHelper.collegeName[0]);
	}
	
	public void setCollege(String college){
		edit.putString("college",college);
		edit.commit();
	}
	
	//是否已经绑定学号
	public boolean hasBindStuid(){
		String stuid=getStuId();
		if(stuid.length()==14&&!stuid.trim().equals("")){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 设置设备识别码
	 * @param client
	 */
	public void setClient(String client){
		edit.putString("clientId", client);
		edit.commit();
	}
	
	public String getClient(){
		return share.getString("clientId","");
	}
	
	public  void setUserid(String userid){
		edit.putString("userid", userid);
		edit.commit();
	}
	public String getUserid(){
		return share.getString("userid","");
	}
	

}
