package com.pw.schoolknow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;

public class About extends BaseActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_about);
		setTitle("¹ØÓÚ");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"·´À¡");
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			Intent it=new Intent(About.this,Suggest.class);
			startActivity(it);
			break;
		default:
			break;
		}
	}

}
