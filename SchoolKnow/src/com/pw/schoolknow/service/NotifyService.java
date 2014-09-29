package com.pw.schoolknow.service;



import com.pw.schoolknow.R;
import com.pw.schoolknow.activity.ChatRecords;
import com.pw.schoolknow.activity.FriendsList;
import com.pw.schoolknow.activity.Inform;
import com.pw.schoolknow.helper.NotifyHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class NotifyService extends Service {
	
	public static final int NOTIFY_INFROM=1;
	public static final int NOTIFY_CHAT=2;
	public static final int NOTIFY_ADDFRIEND_SUCCESS=3;
	
	private static final int NORMAL_NOTIFY_ID = 1;  
	
	public int notifyType;
	public NotifyHelper nh;
	
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		nh=new NotifyHelper(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	


	public int onStartCommand(Intent intent, int flags, int startId) {
		notifyType=intent.getIntExtra("type", NOTIFY_INFROM);
		new Thread(new Runnable() {
			public void run() {
			  try {   
                 Thread.sleep(1000);
                 normalNotify();
                 stopSelf();
              } catch (Exception e) {}   
			}
		}).start();
		return START_NOT_STICKY;
		
		//返回值说明:http://blog.csdn.net/lizzy115/article/details/7001731
	}
	
	
	 public void normalNotify() {  
	        Context context = this;  
	        
	        NotifyHelper nh=new NotifyHelper(context);
	  
	        NotificationManager mNotificationManager =
	        		(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
	  
	        int icon = R.drawable.ic_launcher;  
	        CharSequence tickerText = nh.getTicker();  
	        long when = System.currentTimeMillis();  
	        Notification notification = new Notification(icon, tickerText, when);  
	  
	        // 设定声音  
	        notification.defaults |= Notification.DEFAULT_SOUND;  
	          
	        //设定震动(需加VIBRATE权限)  
	        notification.defaults |= Notification.DEFAULT_VIBRATE;  
	          
	        // 设定LED灯提醒  
	        notification.defaults |= Notification.DEFAULT_LIGHTS;  
	          
	        // 设置点击此通知后自动清除  
	        notification.flags |= Notification.FLAG_AUTO_CANCEL;  
	  
	        CharSequence contentTitle = nh.getTitle();  
	        CharSequence contentText = nh.getContent(); 
	        Intent intent=null;
	       switch(nh.getType()){
	       case NOTIFY_INFROM:
	    	   intent = new Intent(context, Inform.class);  
	       	   break;
	       case NOTIFY_CHAT:
	    	   intent = new Intent(context, ChatRecords.class);  
	    	   break;
	       case NOTIFY_ADDFRIEND_SUCCESS:
	    	   intent = new Intent(context, FriendsList.class);  
	    	   break;
	       default:
	    	   intent = new Intent(context, Inform.class);  
	           break;
	       } 
	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);  
	  
	        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);  
	  
	        mNotificationManager.notify(NORMAL_NOTIFY_ID, notification);  
	        
	        nh.clear();
	    }  
	

}
