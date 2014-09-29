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
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyProgressBar;

public class Who extends BaseActivity {
	
	private Button btn=null;
	private EditText et=null;
	private String inputValue=null;
	private MyProgressBar mpb;
	private final static int SEARCH=1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_who);
		setTitle("校友查询");
		setTitleBar(0,0);
		
		btn=(Button) super.findViewById(R.id.who_search_btn);
		et=(EditText) super.findViewById(R.id.who_edit_id);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				inputValue=EncodeUtil.ToUtf8(et.getText().toString());
				if("".equals(inputValue)){
					T.showShort(Who.this,"请输入查询的姓名或者学号");
				}else if(inputValue.indexOf("_")!=-1){
					T.showShort(Who.this,"不能使用特殊符号查询");
				}else{
					mpb=new MyProgressBar(Who.this);
					mpb.setMessage("正在查询中...");
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg=new Message();
							msg.what=SEARCH;
							msg.obj=GetUtil.getRes(ServerConfig.HOST+
									"/schoolknow/who.php?param="+inputValue);
							handler.sendMessage(msg);
						}
					}).start();
				}
			}
		});
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			String data=msg.obj.toString();
			switch(msg.what){
			case SEARCH:				
				if(data.equals("[]")||data.equals("nothing")){
					T.showShort(Who.this,"找不到你需要的内容,可能是学号或者姓名输入有误!");
				}else{
					Intent it=new Intent(Who.this,WhoContent.class);
					it.putExtra("data", data);
					startActivity(it);
				}
				break;
			default:
				break;
			}
			mpb.dismiss();
		}
		
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
