package com.pw.schoolknow.service;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.base.UserBase;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.FriendDB;
import com.pw.schoolknow.db.UserDB;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.LoginHelper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class UpdateFriendsService extends Service {
	
	public LoginHelper lh;
	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		lh=new LoginHelper(this);
		HttpUtils http=new HttpUtils();
		RequestParams param=new RequestParams();
		param.addBodyParameter("uid",lh.getUid());
		param.addBodyParameter("token", lh.getToken());
		http.send(HttpMethod.POST, ServerConfig.HOST+"/schoolknow/Friend/api/getFriendList.php",
				param,new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {}
			public void onSuccess(ResponseInfo<String> info) {
				try {
					List<Map<String,Object>> list=new JsonHelper(info.result).
							parseJson(new String[]{"frienduid","remark","nickname","sex","college"});
					for(Map<String,Object> map:list){
						FriendDB.getInstance(UpdateFriendsService.this, lh.getUid())
						.add(map.get("frienduid").toString(), map.get("nickname").toString());
						UserBase base=new UserBase(
								map.get("frienduid").toString(), 
								map.get("sex").toString(), 
								"", map.get("nickname").toString(), "", map.get("college").toString());
						UserDB.getInstance(UpdateFriendsService.this).save(base);
					}
					
				} catch (Exception e) {
					
				}
				stopSelf();
			}
		});
		return START_NOT_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
