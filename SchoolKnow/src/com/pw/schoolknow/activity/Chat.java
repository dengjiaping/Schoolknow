package com.pw.schoolknow.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.MessageAdapter;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.InformBase;
import com.pw.schoolknow.base.MessageItem;
import com.pw.schoolknow.base.SendChatMessageItem;
import com.pw.schoolknow.base.UserBase;
import com.pw.schoolknow.config.DecodeConfig;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.ChatMessageDB;
import com.pw.schoolknow.db.UserDB;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.push.AsyncSend;
import com.pw.schoolknow.push.PushMessageReceiver;
import com.pw.schoolknow.utils.BitmapUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.EmotionBox;
import com.pw.schoolknow.widgets.EmotionBox.EmotionBoxClickListener;
import com.pw.schoolknow.widgets.XListView.IXListViewListener;
import com.pw.schoolknow.widgets.XListView;

public class Chat extends BaseActivity implements PushMessageReceiver.EventHandler {
	
	@ViewInject(R.id.chat_listview)
	private XListView lv;
	
	private EmotionBox emox;
	
	public UserBase user;
	public AsyncSend sendUtil;
	public LoginHelper lh;
	
	private MessageAdapter adapter;
	private List<MessageItem> list;
	
	public static Bitmap myHead,friendHead;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_chat);
		setTitle("联系人");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		ViewUtils.inject(this);
		lh=new LoginHelper(this);
		
		String uid=getIntent().getStringExtra("uid");
		user=UserDB.getInstance(this).getUserInfo(uid);
		if(user==null){
			T.showShort(this, "发生异常");
			return;
		}
		setTitle(user.getNick());
		
		//设置消息已读
		ChatMessageDB.getInstance(this, lh.getUid()).setRead(uid);
		
		//设置头像
		Chat.myHead=setHead(lh.getUid(),Chat.myHead);
		Chat.friendHead=setHead(user.getEmail(),Chat.friendHead);
		
		
		list=new ArrayList<MessageItem>();
		list.addAll(ChatMessageDB.getInstance(this, lh.getUid()).getList(user.getEmail(), 30));
		adapter=new MessageAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setSelection(list.size());
		lv.setXListViewListener( new IXListViewListenerImp());
		lv.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View arg0, MotionEvent event) {
				emox.HideEmotionBox();
				return false;
			}
		});
		
		emox=new EmotionBox(Chat.this);
		emox.setOnClick(new EmotionBoxClickListener() {
			public void onClick(String EditTextVal, View view) {
				emox.SetValue("");
				
				//封装发送消息
				SendChatMessageItem chatMessageItem=new SendChatMessageItem(MessageItem.MESSAGE_TYPE_TEXT, 
						TimeUtil.getCurrentTime(), EditTextVal);
				
				//封装通知消息
				InformBase inform=new InformBase(InformConfig.PUSH_CHAT,
						lh.getUid(), lh.getNickname(), 
						MyApplication.getInstance().getGson().toJson(chatMessageItem)
						, String.valueOf(TimeUtil.getCurrentTime()));
				sendUtil=new AsyncSend(MyApplication.getInstance().getGson().toJson(inform),
						user.getClient());
				sendUtil.send();
				
				//封装显示聊天
				long current_time=TimeUtil.getCurrentTime();
				MessageItem MySendMessage=new MessageItem(
						MessageItem.MESSAGE_TYPE_TEXT,current_time,
						EditTextVal,lh.getUid(), false);
				list.add(MySendMessage);
				
				ChatMessageDB.getInstance(Chat.this, lh.getUid())
				.insert(MessageItem.MESSAGE_TYPE_TEXT, user.getEmail(),EditTextVal,current_time,"0","1");
				
				adapter.notifyDataSetInvalidated();
				lv.setSelection(list.size()-1);
			}
		});
	}
	
	class IXListViewListenerImp implements IXListViewListener{
		public void onRefresh() {
			lv.stopRefresh();
			lv.stopLoadMore();
		}
		public void onLoadMore() {
			lv.stopRefresh();
			lv.stopLoadMore();
		}
		
	}
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}


	@Override
	public void onMessage(String tm) {
		try{
			 InformBase item=MyApplication.getInstance().getGson().fromJson(tm,InformBase.class);
			 if(item.getType()==InformConfig.PUSH_CHAT){
				 SendChatMessageItem messageItem=
						 MyApplication.getInstance().getGson().fromJson(item.getContent(),SendChatMessageItem.class);
				 if(item.getSendUid().equals(user.getEmail())){
					 MessageItem chatItem=new MessageItem
							 (messageItem.getType(), messageItem.getTime(),
									 messageItem.getContent(),user.getEmail(), true);
					 list.add(chatItem);
					 ChatMessageDB.getInstance(Chat.this, lh.getUid())
						.insert(messageItem.getType(), user.getEmail(),messageItem.getContent(),
								messageItem.getTime(),"1","1");
					 adapter.notifyDataSetInvalidated();
					 lv.setSelection(list.size()-1);
				 }else{
					 ChatMessageDB.getInstance(Chat.this, lh.getUid())
						.insert(messageItem.getType(), user.getEmail(),messageItem.getContent(),
								messageItem.getTime(),"1","0");
				 }
			 }
		}catch(Exception e){}
	}

	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		// TODO Auto-generated method stub
		
	}
	
	protected void onPause() {
		PushMessageReceiver.ehList.remove(this);// 移除监听
		super.onPause();
	}


	@Override
	protected void onResume() {
		PushMessageReceiver.ehList.add(this);// 监听推送的消息
		super.onResume();
	}
	
	
	//设置头像
	public Bitmap setHead(String uid,Bitmap headBm){
		Bitmap bm=null;
		String path=PathConfig.HEADPATH+DecodeConfig.decodeHeadImg(uid)+".pw";
		if(new File(path).exists()){
			bm = BitmapFactory.decodeFile(path);
		}else{
			bm=BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
			new AsyncLoadHead(headBm, uid).execute();
		}
		return bm;
		
	}
	
	
	/**
	 * 异步加载头像
	 * @author peng
	 *
	 */
	public class AsyncLoadHead extends AsyncTask<Void, Void, Bitmap> {
		public Bitmap bm;
		public 	String uid;
		public AsyncLoadHead(Bitmap bm, String uid) {
			this.bm = bm;
			this.uid = uid;
		}

		protected Bitmap doInBackground(Void... params) {
			return GetUtil.getBitMap(ServerConfig.HOST+"/schoolknow/manage/head/"+
					DecodeConfig.decodeHeadImg(uid)+".pw");
		}

		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result!=null){
				final String path=PathConfig.HEADPATH;
				final String name=DecodeConfig.decodeHeadImg(uid)+".pw";
				try {
					BitmapUtil.saveImg(result,path,name);
				} catch (Exception e) {
					e.printStackTrace();
				}
				bm=result;
			}
		}
		
		
	}
	

}
