package com.pw.schoolknow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.VersionHelper;
import com.pw.schoolknow.utils.T;

public class Setting extends BaseActivity {
	
	private RelativeLayout versionLayout,about,suggest;
	private TextView version;
	private Button exit;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_setting);
		setTitle("设置");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		version=(TextView) super.findViewById(R.id.setting_check_version);
		version.setText(VersionHelper.getVerName(this));
		
		exit=(Button) super.findViewById(R.id.setting_exit_login_btn);
		exit.setOnClickListener(new ExitLogin());
		
		versionLayout=(RelativeLayout) super.findViewById(R.id.setting_check_version_layout);
		versionLayout.setOnClickListener(new LayoutOnclick());
		
		about=(RelativeLayout) super.findViewById(R.id.setting_check_about_layout);
		about.setOnClickListener(new LayoutOnclick());
		
		suggest=(RelativeLayout) super.findViewById(R.id.setting_check_suggest_layout);
		suggest.setOnClickListener(new LayoutOnclick());
	}

	@Override
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
	
	public class LayoutOnclick implements OnClickListener{
		public void onClick(View v) {
			if(v==versionLayout){
				VersionHelper vh=new VersionHelper(Setting.this);
				if(vh.checkUpdate()){
					vh.updateTip();
				}else{
					T.showShort(Setting.this, "已经是最新版本");
				}
			}else if(v==about){
				Intent it=new Intent(Setting.this,About.class);
				startActivity(it);
			}else if(v==suggest){
				Intent it=new Intent(Setting.this,Suggest.class);
				startActivity(it);
			}
		}
		
	}
	
	public class ExitLogin implements android.view.View.OnClickListener{
		public void onClick(View arg0) {
			new LoginHelper(Setting.this).logout();
			T.showShort(Setting.this,"成功退出当前账号");
			Intent it=null;
			it=new Intent(Setting.this,Main.class);
			it.putExtra("param","2");
			startActivity(it);
			finish();
		}
		
	}

}
