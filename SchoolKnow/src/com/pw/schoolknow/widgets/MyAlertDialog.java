package com.pw.schoolknow.widgets;


import com.pw.schoolknow.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public  class MyAlertDialog implements OnClickListener {
	
	private Dialog mDialog;
	private Button leftBtn,rightBtn;
	private TextView title,message;
	private LinearLayout content;
	
	public MyAlertDialog(Context context){
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.show();
		
		View layout = LayoutInflater.from(context).inflate(R.layout.wg_alertdialog, null);
		leftBtn=(Button) layout.findViewById(R.id.MyAlertDialogLeftBtn);
		rightBtn=(Button) layout.findViewById(R.id.MyAlertDialogRightBtn);
		content=(LinearLayout) layout.findViewById(R.id.MyAlertDialog_content);
		
		//为按钮设置事件监听
		leftBtn.setOnClickListener(this);
		rightBtn.setOnClickListener(this);
		
		//不设置按钮默认隐藏
		leftBtn.setVisibility(View.GONE);
		rightBtn.setVisibility(View.GONE);
		
		title=(TextView) layout.findViewById(R.id.MyAlertDialogTitle);
		message=(TextView) layout.findViewById(R.id.MyAlertDialogMessage);
		
        mDialog.setContentView(layout);
	}
	
	//事件传递接口
	public interface MyDialogInt{  
        public void onClick(View view);  
    } 
	
	MyDialogInt LeftcallBack = null; 
	MyDialogInt RightcallBack = null; 
	
	public void dismiss(){
		mDialog.dismiss();
	}
	
	public boolean isShowing(){
		return mDialog.isShowing();
	}
	
	//设置左按钮
	public void setLeftButton(String value,MyDialogInt iBack){
		if(value==null||value.equals("")){
			return;
		}
		leftBtn.setVisibility(View.VISIBLE);
		leftBtn.setText(value);
		LeftcallBack=iBack;
	}
	
	//设置右按钮
	public void setRightButton(String value,MyDialogInt iBack){
		if(value==null||value.equals("")){
			return;
		}
		rightBtn.setVisibility(View.VISIBLE);
		rightBtn.setText(value);
		RightcallBack=iBack;
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
	
	public void setMessage(String message){
		this.message.setText(message);
	}
	
	public void setContentView(View v){
		message.setVisibility(View.GONE);
		content.setVisibility(View.VISIBLE);
		content.addView(v);
	}
	
	public LinearLayout getContentView(){
		return content;
	}
	

	@Override
	public void onClick(View view) {
		if(view==leftBtn){
			LeftcallBack.onClick(view);
		}else if(view==rightBtn){
			RightcallBack.onClick(view);
		}
	}
	
}
