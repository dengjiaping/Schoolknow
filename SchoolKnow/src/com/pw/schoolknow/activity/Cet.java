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
import com.pw.schoolknow.utils.EncodeUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.widgets.MyProgressBar;

public class Cet extends BaseActivity {
	
	private EditText name=null;
	private EditText zkz=null;
	private Button btn=null;
	private MyProgressBar mpb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_cet);
		setTitle("四六级查询");
		setTitleBar(0,0);
		
		name=(EditText) super.findViewById(R.id.cet_edit_id);
		zkz=(EditText) super.findViewById(R.id.cet_edit_pwd);
		btn=(Button) super.findViewById(R.id.cet_login_btn);
		
		btn.setOnClickListener(new onclicklistener());
	}
	
	public class onclicklistener implements OnClickListener{
		@Override
		public void onClick(View v) {
			mpb=new MyProgressBar(Cet.this);
			mpb.setMessage("正在查询中...");
			new	 Thread(new Runnable() {
				@Override
				public void run() {
					String u=EncodeUtil.ToUtf8(name.getText().toString());
					String p=zkz.getText().toString();
					Message msg=new Message();
					msg.what=102;
					String param=ServerConfig.HOST+"/schoolknow/cet.php?name="+
							u+"&cetId="+p;
					msg.obj=GetUtil.getRes(param);
					handler.sendMessage(msg);					
				}
			}).start();			
		}
		
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==102){
				Intent it=new Intent(Cet.this,CetData.class);
				it.putExtra("cet",msg.obj.toString());
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
