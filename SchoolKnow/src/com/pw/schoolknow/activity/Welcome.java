package com.pw.schoolknow.activity;



import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.InitHelper;
import com.pw.schoolknow.service.Init;
import com.pw.schoolknow.utils.SmartBarUtils;
import com.pw.schoolknow.utils.TimeUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Welcome extends Activity{
	
	public RelativeLayout layout;
	public ImageView logo;
	public TextView appName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_welcome);
		
		if(SmartBarUtils.hasSmartBar()){
			View decorView = getWindow().getDecorView();
			SmartBarUtils.hide(decorView);
		}
		
		layout=(RelativeLayout) super.findViewById(R.id.welcome_bg);
		logo=(ImageView) super.findViewById(R.id.img_welcome_logo);
		appName=(TextView) super.findViewById(R.id.welcome_appname);
		
		
		if(System.currentTimeMillis()<TimeUtil.createtimesamp(2014, 5, 18, 0, 0)){
			logo.setVisibility(View.GONE);
			appName.setVisibility(View.GONE);
			layout.setBackgroundResource(R.drawable.loading);
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				//启动检测更新服务
				Intent updateIntent = new Intent(Welcome.this, Init.class); 
		        startService(updateIntent);
		       
				Intent it=null;
				if(new InitHelper(Welcome.this).checkHasInit()){
					it = new Intent(Welcome.this, Main.class);
				}else{
					it = new Intent(Welcome.this, VersionIntro.class);
				}
				startActivity(it);
				finish();
			}
		},2000*1);
	}
	
}
