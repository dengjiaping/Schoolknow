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
//import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.widgets.MyProgressBar;

@SuppressLint("HandlerLeak")
public class Library extends BaseActivity {
	
	private EditText uid=null;
	private EditText pwd=null;
	private Button btn=null;
	private MyProgressBar mpb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_library);
		setTitle("图书馆");
		setTitleBar(0,0);
		
		uid=(EditText) super.findViewById(R.id.library_edit_id);
		pwd=(EditText) super.findViewById(R.id.library_edit_pwd);
		btn=(Button) super.findViewById(R.id.library_login_btn);
		
		btn.setOnClickListener(new onclicklistenerimp());
	}
	
	public class onclicklistenerimp implements OnClickListener{
		@Override
		public void onClick(View v) {
			
			mpb=new MyProgressBar(Library.this);
			mpb.setMessage("正在登录中...");
			new	 Thread(new Runnable() {
				@Override
				public void run() {
					String u=uid.getText().toString();
					String p=pwd.getText().toString();
					Message msg=new Message();
					msg.what=102;
					//String param=ServerConfig.HOST+"/schoolknow/library.php?uid="+
					//		u+"&pwd="+p;
					String param="http://schoolknow.sturgeon.mopaas.com/library.php?uid="+u+"&pwd="+p;
					msg.obj=GetUtil.getRes(param);
					handler.sendMessage(msg);					
				}
			}).start();
			
		}
		
	}

	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==102){
				Intent it=new Intent(Library.this,LibraryData.class);
				it.putExtra("jsondata",msg.obj.toString());
				it.putExtra("uid",uid.getText().toString());
				it.putExtra("pwd",pwd.getText().toString());
				startActivity(it);
			}
			mpb.dismiss();
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
