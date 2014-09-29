package com.pw.schoolknow.push;

import java.util.ArrayList;
import java.util.List;






import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.base.InformBase;
import com.pw.schoolknow.base.SendChatMessageItem;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.db.ChatMessageDB;
import com.pw.schoolknow.db.FriendDB;
import com.pw.schoolknow.db.InformDB;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NotifyHelper;
import com.pw.schoolknow.service.NotifyService;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;

public class PushMessageReceiver extends FrontiaPushMessageReceiver {
	
	public LoginHelper lh;
	public NotifyHelper nh;
	
	public static ArrayList<EventHandler> ehList = new ArrayList<EventHandler>();
	
	public static abstract interface EventHandler {
		public abstract void onMessage(String tm);
		public abstract void onBind(Context context, int errorCode, String appid, 
				String userId, String channelId, String requestId);
	}
	
	@Override
	public void onBind(Context context, int errorCode, String appid, 
			String userId, String channelId, String requestId) {
		for (int i = 0; i < ehList.size(); i++)
			ehList.get(i).onBind(context, errorCode,appid,userId,channelId,requestId);	
	}
	
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onSetTags(Context context, int errorCode, 
			List<String> sucessTags, List<String> failTags, String requestId) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDelTags(Context context, int errorCode, 
			List<String> sucessTags, List<String> failTags, String requestId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onListTags(Context context, int errorCode, 
			List<String> tags, String requestId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMessage(Context context, String message, String customContentString) {
		lh=new LoginHelper(context);
		nh=new NotifyHelper(context);
		
		//分发消息
		if (ehList.size() > 0) {
			for (int i = 0; i < ehList.size(); i++)
				ehList.get(i).onMessage(message);
		}
        if(lh.hasLogin()){
        	try{
        		 InformBase item=MyApplication.getInstance().getGson().fromJson(message,InformBase.class);
        		 switch(item.getType()){
        		 //花椒社区回复
        		 case InformConfig.PUSH_SFComment:
        			 if(item.getReceiveUid().equals(lh.getUid())){
            			 new InformDB(context, lh.getUid()).saveInform(item);
                         nh.save("您花椒社区有了新回复","消息通知", item.getSendName()+" 在花椒社区回复了你的动态");
                         nh.saveType(NotifyService.NOTIFY_INFROM);
                         //启动service提示消息
                         context.startService(new Intent(context,NotifyService.class));
                	 }
        			 break;
        	     //添加好友请求
        		 case InformConfig.PUSH_ADDREQUEST:
        			 if(item.getReceiveUid().equals(lh.getUid())){
        				 new InformDB(context, lh.getUid()).saveInform(item);
        				 nh.save("添加好友请求","添加好友通知", item.getSendName()+" 请求添加您为好友");
        				 nh.saveType(NotifyService.NOTIFY_INFROM);
                         context.startService(new Intent(context,NotifyService.class));
        			 }
        			 break;
        		 //添加好友反馈信息
        		 case InformConfig.PUSH_ADDCALLBACK:
        			 int back=Integer.parseInt(item.getContent());
        			 String backinfo="";
        			 if(back==1){
        				 backinfo=" 同意了你的好友请求";
        				 FriendDB.getInstance(context,lh.getUid()).
        				 add(item.getSendUid(), item.getSendName());
        				 new InformDB(context, lh.getUid()).saveInform(item,"1");
        			 }else{
        				 backinfo=" 拒绝了你的好友请求";
        				 new InformDB(context, lh.getUid()).saveInform(item,"3");
        			 }
        			 nh.save("好友请求","好友请求反馈信息", item.getSendName()+backinfo);
        			 nh.saveType(NotifyService.NOTIFY_ADDFRIEND_SUCCESS);
                     context.startService(new Intent(context,NotifyService.class));
        			 break;
        		 case InformConfig.PUSH_CHAT:
        			 ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE); 
        		     RunningTaskInfo info = manager.getRunningTasks(1).get(0); 
        		     String shortClassName = info.topActivity.getShortClassName();    //类名
        		     
        		     //不是Chat界面
        		     if(!shortClassName.equals(".activity.Chat")){
        		    	 nh.save("聊天信息","聊天消息通知", item.getSendName()+" 给您发送了消息");
        		    	 nh.saveType(NotifyService.NOTIFY_CHAT);
        		    	 Intent it=new Intent(context,NotifyService.class);
                         context.startService(it);
                         SendChatMessageItem messageItem=
    							 MyApplication.getInstance().getGson().fromJson(item.getContent(),SendChatMessageItem.class);
                         ChatMessageDB.getInstance(context, lh.getUid())
 						.insert(messageItem.getType(),item.getSendUid(),messageItem.getContent(),
 								messageItem.getTime(),"1","0");
        		     }
        			 
        			 break;
        		 default:
        			 break;
        		 }
            	 
        	}catch(Exception ex){
        		
        	}
        	
             
        }
	}

	@Override
	public void onNotificationClicked(Context context, String title, 
			String description, String customContentString) {
		// TODO Auto-generated method stub
		
	}

}
