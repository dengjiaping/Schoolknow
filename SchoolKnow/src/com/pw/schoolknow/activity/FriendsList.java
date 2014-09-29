package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.FriendListAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.UserBase;
import com.pw.schoolknow.db.FriendDB;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.PinyinComparator;
import com.pw.schoolknow.utils.PinyinUtils;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MySideBar;
import com.pw.schoolknow.widgets.MySideBar.OnTouchingLetterChangedListener;
import com.pw.schoolknow.widgets.XListView;

@SuppressLint("DefaultLocale")
public class FriendsList extends Activity{
	
	@ViewInject(R.id.friend_list_lv)
	private XListView lv=null;
	@ViewInject(R.id.friend_list_rightSlideBar)
	private MySideBar myslidebar;
	@ViewInject(R.id.friend_list_MySideTip)
	private TextView slideBarTip=null;
	
	List<Map<String,Object>> list=null;
	private FriendListAdapter adapter;
	
	public UserBase[] userData;
	public LoginHelper lh;
	public Context mcontext;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_friend_list);
		ViewUtils.inject(this);
		mcontext=this;
		lh=new LoginHelper(this);
		lv.setPullLoadEnable(false);
		lv.setPullRefreshEnable(false);
		
		
		userData=FriendDB.getInstance(this,lh.getUid()).getFriendArray();
		Arrays.sort(userData, new PinyinComparator());
		list=new ArrayList<Map<String,Object>>();
		for(UserBase u:userData){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name", "");
			map.put("nickname", u.getNick());
			map.put("mood",u.getEmail());
			map.put("uid",u.getEmail());
			map.put("py", PinyinUtils.getAlpha(u.getNick()));
			list.add(map);
		}
		
		
		//加入头部搜索框
		LayoutInflater inflater=LayoutInflater.from(this);
		View headSearch = inflater.inflate(R.layout.part_friendlist_head, null);
		lv.addHeaderView(headSearch);
		
		adapter=new FriendListAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClick()); 
		
		myslidebar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			public void onTouchingLetterChanged(String s) {
				slideBarTip.setText(s);
				slideBarTip.setVisibility(View.VISIBLE);
				_handler.removeCallbacks(letterThread);
				_handler.postDelayed(letterThread, 1000);
				if (alphaIndexer(s) > 0) {
					int position = alphaIndexer(s);
					lv.setSelection(position);
				}
				
			}
		});
	}
	
	public int alphaIndexer(String s) {
		int position = 0;
		for (int i = 0; i < list.size(); i++) {
			String py = (String) list.get(i).get("py");
			if (py.startsWith(s)) {
				position = i;
				break;
			}
		}
		return position;
	}
	
	private Handler _handler = new Handler();
	private Runnable letterThread = new Runnable() {
		@Override
		public void run() {
			slideBarTip.setVisibility(View.GONE);
		}
	};
	
	//跳转到好友简介
	public class OnItemClick implements OnItemClickListener{
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			try{
				if(position>=FriendListAdapter.FriendListTool.length+2){
					Intent it=new Intent(FriendsList.this,FriendZone.class);
					Map<String,Object> map=new HashMap<String, Object>();
					map=(Map<String, Object>) lv.getItemAtPosition(position);
					it.putExtra("uid", map.get("uid").toString());
					startActivity(it);
				}else{
					switch(position){
					case 2:
						T.showShort(mcontext, "暂不支持推荐好友");
						break;
					case 3:
						T.showShort(mcontext, "暂不支持添加好友");
						break;
					case 4:
						T.showShort(mcontext, "暂不支持添加通讯录好友");
						break;
					default:
						break;
					}
				}
			}catch(Exception e){
				//T.showShort(mcontext, "SS:"+position);
			}
		}
	}

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


}
