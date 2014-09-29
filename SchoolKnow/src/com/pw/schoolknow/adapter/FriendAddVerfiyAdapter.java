package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.pw.schoolknow.R;
import com.pw.schoolknow.activity.FriendZone;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.base.InformBase;
import com.pw.schoolknow.base.UserBase;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.FriendDB;
import com.pw.schoolknow.db.InformDB;
import com.pw.schoolknow.db.UserDB;
import com.pw.schoolknow.db.UserDB.getUserCallBack;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.push.AsyncSend;
import com.pw.schoolknow.push.AsyncSend.OnSendScuessListener;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FriendAddVerfiyAdapter extends BaseAdapter{
	
	public Context context;
	private List<Map<String,Object>> list;
	
	public ImageView head;
	public BitmapUtils bitmapUtils;
	
	public LoginHelper lh;
	
	public interface updateCallBack{
		public void OnClick();
	}
	
	public updateCallBack back;
	
	public FriendAddVerfiyAdapter(Context context,
			List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		bitmapUtils=BitmapHelper.getHeadBitMap(context);
		
		lh=new LoginHelper(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.item_friend_add_verify_lv, 
	        		  parent, false);
	    }
		head = ViewHolder.get(convertView, R.id.item_friend_add_head);
		TextView state= ViewHolder.get(convertView, R.id.item_friend_add_text_state);
		TextView time= ViewHolder.get(convertView, R.id.item_friend_add_time);
		TextView nick= ViewHolder.get(convertView, R.id.item_friend_add_nick);
		
		LinearLayout layout=ViewHolder.get(convertView, R.id.item_friend_add_btn_layout);
		
		
		Map<String,Object> map=list.get(position);
		String stateCode=map.get("state").toString();
		if(stateCode.equals("0")){
			state.setText("待处理");
			layout.setVisibility(View.VISIBLE);
		}else if(stateCode.equals("1")){
			state.setText("已同意");
			layout.setVisibility(View.GONE);
		}else if(stateCode.equals("2")){
			state.setText("已忽略");
			layout.setVisibility(View.GONE);
		}else{
			state.setText("已拒绝");
			layout.setVisibility(View.GONE);
		}
		Button agree=ViewHolder.get(convertView, R.id.item_friend_add_btn_agree);
		Button refuse=ViewHolder.get(convertView, R.id.item_friend_add_btn_refuse);
		Button ignore=ViewHolder.get(convertView, R.id.item_friend_add_btn_ignore);
		
		head.setOnClickListener(new Onclick(0,map.get("uid").toString()));
		agree.setOnClickListener(new Onclick(1,map.get("id").toString(),
				map.get("uid").toString(),map.get("nick").toString()));
		refuse.setOnClickListener(new Onclick(2,map.get("id").toString(),map.get("uid").toString()));
		ignore.setOnClickListener(new Onclick(3,map.get("id").toString(),map.get("uid").toString()));
		
		time.setText(TimeUtil.getCommmentString(Long.parseLong(map.get("time").toString())));
		nick.setText(map.get("nick").toString());
		
		BitmapHelper.setHead(bitmapUtils, head, map.get("uid").toString());
		return convertView;
	}
	
	public void setOnClick(updateCallBack callBack){
		this.back=callBack;
	}
	
	
	//同意，忽略，拒绝好友请求
	public class Onclick implements OnClickListener{
		public int position;
		public String id;
		public String uid;
		public String nick;
		public Onclick(int position, String id) {
			this.position = position;
			this.id = id;
			this.uid="";
			nick="";
		}
		public Onclick(int position, String id,String uid) {
			this.position = position;
			this.id = id;
			this.uid=uid;
			nick="";
		}
		public Onclick(int position, String id,String uid,String nick) {
			this.position = position;
			this.id = id;
			this.uid=uid;
			this.nick=nick;
		}
		public void onClick(View v) {
			switch(position){
			case 0:
				if(id.length()!=0){
					Intent it=new Intent(context,FriendZone.class);
					it.putExtra("uid", id);
					context.startActivity(it);
				}
				break;
			case 1:
				if(id.length()!=0){
					//发送添加请求
					HttpUtils http=new HttpUtils();
					RequestParams params=new RequestParams();
					params.addBodyParameter("uid",lh.getUid());
					params.addBodyParameter("friendid",uid);
					params.addBodyParameter("token",lh.getToken());
					http.send(HttpMethod.POST,ServerConfig.HOST+"/schoolknow/Friend/addFriend.php",
							params,new RequestCallBack<String>() {
						public void onFailure(HttpException arg0, String arg1) {}
						public void onSuccess(ResponseInfo<String> info) {
							if(info.result.equals("success")){
								//数据库添加为好友
								FriendDB.getInstance(context,lh.getUid()).add(uid,nick);
								T.showShort(context,"添加好友成功");
								InformDB.getInstance(context,lh.getUid()).setAddQeest(1, Integer.parseInt(id));
								InformBase base=
										new InformBase(InformConfig.PUSH_ADDCALLBACK,
												lh.getUid(), lh.getNickname(), "1",
												String.valueOf(TimeUtil.getCurrentTime()));
								final String sendString_=MyApplication.getInstance().getGson().toJson(base);
								UserDB.getInstance(context).getUser(uid, new getUserCallBack() {
									public void getUserInfo(UserBase base) {
										
									}

									@Override
									public void getPushId(String PushId) {
										if(PushId.length()!=0){
											AsyncSend send=new AsyncSend(sendString_, PushId);
											send.send();
											send.setOnSendScuessListener(new OnSendScuessListener() {
												public void sendScuess() {
													T.showShort(context, "发送成功");
												}
											});
										}else{
											T.showShort(context, "该用户账号存在问题");
										}
									}
								});
								
							}else if(info.result.equals("exist")){
								InformDB.getInstance(context,lh.getUid()).setAddQeest(2, Integer.parseInt(id));
								T.showLong(context,"你们已经是好友关系,请勿重复添加");
							}else{
								InformDB.getInstance(context,lh.getUid()).setAddQeest(2, Integer.parseInt(id));
								T.showLong(context,"添加好友失败");
							}
							back.OnClick();
						}
						
					});
				}
				break;
			case 2:
				if(id.length()!=0){
					InformDB.getInstance(context,lh.getUid()).setAddQeest(3, Integer.parseInt(id));
					InformBase base=
							new InformBase(InformConfig.PUSH_ADDCALLBACK,
									lh.getUid(), lh.getNickname(), "0",
									String.valueOf(TimeUtil.getCurrentTime()));
					final String sendString=MyApplication.getInstance().getGson().toJson(base);
					UserDB.getInstance(context).getUser(uid, new getUserCallBack() {
						public void getUserInfo(UserBase base) {}
						public void getPushId(String PushId) {
							if(PushId.length()!=0){
								AsyncSend send=new AsyncSend(sendString, PushId);
								send.send();
								send.setOnSendScuessListener(new OnSendScuessListener() {
									public void sendScuess() {
										T.showShort(context, "发送成功");
									}
								});
							}else{
								T.showShort(context, "该用户账号存在问题");
							}
						}
					});
				}
				back.OnClick();
				break;
			case 3:
				if(id.length()!=0){
					InformDB.getInstance(context,lh.getUid()).setAddQeest(2, Integer.parseInt(id));
				}
				back.OnClick();
				break;
			default:
				break;
			}
		}
		
	}


}
