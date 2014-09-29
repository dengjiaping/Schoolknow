package com.pw.schoolknow.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.helper.VersionHelper;
import com.pw.schoolknow.utils.GetUtil;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.pw.schoolknow.push.PushMessageReceiver;
import com.pw.schoolknow.push.RestApi;


/**
 * 检测是否更新
 * @author wei8888go
 *
 */
public class Init  extends Service implements PushMessageReceiver.EventHandler {
	
	public LoginHelper lh;
	public VersionHelper vh;
	@Override
	public void onCreate() {
		super.onCreate();
		
		lh=new LoginHelper(this);
		vh=new VersionHelper(this);
		
		//获取设备识别码
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, 
				RestApi.mApiKey);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg=new Message();
				msg.what=102;
				msg.obj=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/versionManage.php?current="
				+VersionHelper.getVerCode(Init.this));
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==102){
				String temp=msg.obj.toString().trim();
				if(!temp.equals("")&&temp.length()!=0){
					try {
						int version=VersionHelper.getVerCode(Init.this);
						JSONObject JsonData = new JSONObject(temp);
						
						String serverCode=JsonData.getString("code").trim();
						
						int serverVer=Integer.parseInt(serverCode);
						
						if(serverVer>version){
							if(serverVer>vh.getUpdateVersion()){
								if(serverVer>vh.getUpdateVersion()){
									vh.update(temp,true);
								}
							}
						}
					}catch (JSONException e) {
					}catch (Exception e) {
					}
				}
				stopSelf();
			}
		}
		
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void onStart(Intent intent, int startId) {
		PushMessageReceiver.ehList.add(this);// 监听推送的消息
		super.onStart(intent, startId);
	}

	@Override
	public void onTaskRemoved(Intent rootIntent) {
		PushMessageReceiver.ehList.remove(this);// 移除监听
		super.onTaskRemoved(rootIntent);
	}

	@Override
	public void onMessage(String tm) {
		// TODO Auto-generated method stub
		
	}

	

	//绑定userid
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		if(errorCode==0){
			final String userid=userId;
			if(userid.trim().length()!=0){
				new LoginHelper(this).setUserid(userid);
				if(NetHelper.isNetConnected(this)){
					if(lh.hasLogin()){
						new Thread(new Runnable() {
							public void run() {
								GetUtil.getRes(ServerConfig.HOST+"/schoolknow/manage/updatePush.php"
										+"?user="+lh.getUid()+"&userid="+userid+"&ver="+VersionHelper.getVerCode(Init.this));
							}
						}).start();
					}
				}
				
			}
		}
		
	}

}
