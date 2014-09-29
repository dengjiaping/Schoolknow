package com.pw.schoolknow.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.UserAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.DecodeConfig;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.BitmapUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.widgets.RoundedImageView;

public class User extends Activity {
	
	private RoundedImageView userImg;
	private TextView userName;
	private ListView lv;
	
	private UserAdapter adapter;
	
	
	public View head;
	
	public LoginHelper lh;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_user);
		
		LayoutInflater inflater=LayoutInflater.from(this);
		head = inflater.inflate(R.layout.part_user_head, null,false);
		
		userImg=(RoundedImageView) head.findViewById(R.id.myzone_user_img);
		
		lh=new LoginHelper(User.this);
		String path=PathConfig.HEADPATH+DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
		Bitmap dBitmap=null;
		if(new File(path).exists()){
			dBitmap = BitmapFactory.decodeFile(path);
		}else{
			dBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
			new AsyncLoadHead().execute();
		}
	    userImg.setImageBitmap(dBitmap);
	    
	    //设置用户名和性别
	    userName=(TextView) head.findViewById(R.id.myzone_user_name);
	    userName.setText(lh.getNickname());
	    Drawable drawable=null;
	    if(lh.isBoy()){
	    	drawable = getResources().getDrawable(R.drawable.img_male); 	
	    }else{
	    	drawable = getResources().getDrawable(R.drawable.img_female);
	    }
	    drawable.setBounds(0, 0, 32, 32); 
    	userName.setCompoundDrawables(null, null, drawable, null);
	    
	    userImg.setOnClickListener(new userHeadClick());
	    
	    lv=(ListView) super.findViewById(R.id.user_lv);
		lv.addHeaderView(head);
		
		adapter=new UserAdapter(this, new String[]{"个人资料","我的消息","我的好友","聊天记录","设置"});
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new onItemClickImp());
	}
	
	
	protected void onRestart() {
		adapter.notifyDataSetChanged();
		userName.setText(lh.getNickname());
		String path=PathConfig.HEADPATH+DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
		Bitmap dBitmap=null;
		if(new File(path).exists()){
			dBitmap = BitmapFactory.decodeFile(path);
		}else{
			dBitmap=BitmapFactory.decodeResource(getResources(),R.drawable.default_head);
			new AsyncLoadHead().execute();
		}
		userImg.setImageBitmap(dBitmap);
		super.onRestart();
	}



	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	protected void onResume() {
		if(!lh.hasLogin()){
			finish();
		}
		super.onResume();
	}




	public class AsyncLoadHead extends AsyncTask<Void, Void, Bitmap> {
		protected Bitmap doInBackground(Void... params) {
			return GetUtil.getBitMap(ServerConfig.HOST+"/schoolknow/manage/head/"+
					DecodeConfig.decodeHeadImg(lh.getUid())+".pw");
		}

		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result!=null){
				final String path=PathConfig.HEADPATH;
				final String name=DecodeConfig.decodeHeadImg(lh.getUid())+".pw";
				try {
					BitmapUtil.saveImg(result,path,name);
				} catch (Exception e) {
					e.printStackTrace();
				}
				onCreate(null);
			}
		}
		
		
	}
	
	protected void HandleTitleBarEvent(int buttonId, View v) {

	}
	
	//个性设置
	public class onItemClickImp implements OnItemClickListener{

		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			switch(position){
			case 1:
				Intent it0=new Intent(User.this,UserInfo.class);
				startActivity(it0);
				break;
			case 2:
				Intent it=new Intent(User.this,Inform.class);
				startActivity(it);
				break;
			case 3:
				startActivity(new Intent(User.this,FriendsList.class));
				break;
			case 4:
				startActivity(new Intent(User.this,ChatRecords.class));
				break;
			case 5:
				Intent it2=new Intent(User.this,Setting.class);
				startActivity(it2);
				break;
			default:
				break;
			}
			
		}
		
	}
	
	public class userHeadClick implements OnClickListener{
		public void onClick(View v) {
			Intent it=new Intent(User.this,UserInfo.class);
			startActivity(it);
			finish();
		}
		
	}

	

}
