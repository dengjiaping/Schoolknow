package com.pw.schoolknow.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyProgressBar;

public class OnlienCourseLogin extends BaseActivity {
	
	private EditText uid;
	private EditText pwd;
	private Button btn;
	
	String stuid=null;
	String password=null;
	MyProgressBar pb=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_online_course_login);
		setTitle("选修登录");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		uid=(EditText) super.findViewById(R.id.login_online_course_id);
		pwd=(EditText) super.findViewById(R.id.login_online_course_pwd);
		btn=(Button) super.findViewById(R.id.login_online_course_btn);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				stuid=uid.getText().toString();
				password=pwd.getText().toString();
				if(stuid.equals("")||stuid==null){
					T.showShort(OnlienCourseLogin.this, "请输入你的学号!");
				}else if(password.equals("")||password==null){
					T.showShort(OnlienCourseLogin.this, "请输入密码!");
				}else{
					pb=new MyProgressBar(OnlienCourseLogin.this);
					pb.setMessage("正在登陆中...");
					new Thread(){
						@Override
						public void run(){
							Message msg = new Message();
							msg.what = 102;
							msg.obj=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/"+
							"onlineCourse.php?action=checkPwd&uid="+stuid+"&pwd="+password);
							hander.sendMessage(msg);
						}
					}.start();
				}
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 102:
				//教务处权限关闭
				String str=msg.obj.toString();
				if(str.equals("NoPermission")){
					T.showShort(OnlienCourseLogin.this, "教务处网上选修权限已经关闭");
				}else if(str.trim().equals("true123")){
					T.showShort(OnlienCourseLogin.this, "登录成功!");
					Intent it=new Intent(OnlienCourseLogin.this,OnlineCourse.class);
					it.putExtra("uid", stuid);
					it.putExtra("pwd", password);
					startActivity(it);
				}else{
					T.showShort(OnlienCourseLogin.this, "#"+str+"#用户名或密码错误，请检查账号密码后重新登陆!");
				}
				break;
			}
			if(pb.isShowing()){
				pb.dismiss();
			}
		}
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
