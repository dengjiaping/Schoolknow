package com.pw.schoolknow.widgets;

import com.pw.schoolknow.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyProgressBar {
	private Dialog mDialog;
	private TextView tip=null;
	private View layout;
	private RoundProgressBar pb;

	public MyProgressBar(Context context) {
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.show();
        
        layout = LayoutInflater.from(context).inflate(R.layout.wg_progress_bar, null);
        tip=(TextView) layout.findViewById(R.id.my_progress_bar_tip);
        
        //mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setContentView(layout);
	}
	
	public void dismiss(){
		mDialog.dismiss();
	}
	
	public void show(){
		mDialog.show();
	}
	
	public void setMessage(String str){
		tip.setText(str);
	}
	
	public boolean isShowing(){
		return mDialog.isShowing();
	}
	
	//设置进度条显示进度比例
	public void setScaleProgress(){
		ProgressBar mpb=(ProgressBar) layout.findViewById(R.id.my_progress_bar);
		mpb.setVisibility(View.GONE);
		ViewStub stub=(ViewStub)layout.findViewById(R.id.viewstub_progress_bar);
		stub.inflate();
		//stub.setVisibility(View.VISIBLE);
		pb=(RoundProgressBar) layout.findViewById(R.id.my_scale_progress_bar);
		
	}
	
	//设置最大进度
	public void setMax(int max){
		if(pb!=null){
			pb.setMax(max);
		}
	}
	
	//设置当前进度
	public void setProgress(int progress){
		if(pb!=null){
			pb.setProgress(progress);
		}
	}
}
