package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.FriendAddVerfiyAdapter;
import com.pw.schoolknow.adapter.FriendAddVerfiyAdapter.updateCallBack;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.db.InformDB;
import com.pw.schoolknow.helper.LoginHelper;

public class FriendAddVerify extends BaseActivity {
	
	@ViewInject(R.id.friend_add_verify_lv)
	private ListView lv;
	
	private List<Map<String,Object>> list;
	
	private FriendAddVerfiyAdapter adapter;
	public LoginHelper lh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_friend_add_verify);
		setTitle("验证消息");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		ViewUtils.inject(this);
		lh=new LoginHelper(this);
		
		list=InformDB.getInstance(this,lh.getUid()).getAddRequest();
		adapter=new FriendAddVerfiyAdapter(this, list);
		lv.setAdapter(adapter);
		adapter.setOnClick(new updateCallBack() {
			public void OnClick() {
				onCreate(null);
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

}
