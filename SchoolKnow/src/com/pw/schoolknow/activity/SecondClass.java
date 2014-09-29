package com.pw.schoolknow.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.ClassSelect;
import com.pw.schoolknow.widgets.ClassSelect.ClassSelectInterface;
import com.pw.schoolknow.widgets.MyProgressBar;

@SuppressLint("HandlerLeak")
public class SecondClass extends BaseActivity {
	
	private Button selectClass;
	private Button searchSubmit;
	
	public ClassSelect newClass=null;
	public String classId="";
	
	private MyProgressBar mpb;
	private static final int GetInfo=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_second_class);
		setTitle("第二课堂学分查询");
		setTitleBar(0,0);
		
		selectClass=(Button) super.findViewById(R.id.second_class_select_btn);
		searchSubmit=(Button) super.findViewById(R.id.second_class_select_sumbit);
		
		selectClass.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				newClass=new ClassSelect(SecondClass.this);
				newClass.setOnclickListener(new ClassSelectInterface() {
					@Override
					public void onClick(View view) {
						newClass.dismiss();
						classId=newClass.getClassId();
						selectClass.setText(newClass.getClassN());
					}
				});
			}
		});
		
		searchSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(classId.equals("")||classId==null){
					T.showShort(SecondClass.this,"请先选择班级");
				}else{
					mpb=new MyProgressBar(SecondClass.this);
					mpb.setMessage("正在查询中...");
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg=new Message();
							msg.what=GetInfo;
							msg.obj=GetUtil.getRes(ServerConfig.HOST+
									"/schoolknow/secondclass.php?classid="+classId);
							handler.sendMessage(msg);
							
						}
					}).start();
				}
			}
		});
	}
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case GetInfo:
				Intent it=new Intent(SecondClass.this,SecondClassContent.class);
				it.putExtra("data", msg.obj.toString());
				startActivity(it);
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
