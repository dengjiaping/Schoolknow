package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.ChatRecordAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.db.ChatMessageDB;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.push.PushMessageReceiver;


/**
 * 聊天记录
 * @author peng
 *
 */
public class ChatRecords extends BaseActivity implements PushMessageReceiver.EventHandler{
	
	@ViewInject(R.id.inform_lv)
	private ListView lv;
	@ViewInject(R.id.inform_empty_failed)
	private ImageView img;
	private List<Map<String,Object>> list;
	
	public LoginHelper lh;
	public ChatRecordAdapter adapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_inform);
		setTitle("聊天记录");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		ViewUtils.inject(this);
		lh=new LoginHelper(this);
		
		list=ChatMessageDB.getInstance(this, lh.getUid()).getChatRecords();
		if(list.size()==0){
			img.setVisibility(View.VISIBLE);
		}
		
		adapter=new ChatRecordAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				Map<String,Object> map=(Map<String, Object>) lv.getItemAtPosition(position);
				Intent it=new Intent(ChatRecords.this,Chat.class);
				it.putExtra("uid", map.get("uid").toString());
				startActivity(it);
			}
		});
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}
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

	@Override
	public void onMessage(String tm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		// TODO Auto-generated method stub
		
	}

}
