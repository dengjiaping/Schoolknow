package com.pw.schoolknow.widgets;

import com.pw.schoolknow.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BottomBtn implements OnClickListener {
	
	private Activity act;
	public Context context;
	
	private Button left;
	private Button right;
	
	public interface BottomBtnOnclickListener{
		void onClick(View v,int position);
	}
	
	public BottomBtnOnclickListener leftImp;
	public BottomBtnOnclickListener rightImp;
	
	public BottomBtn(Activity act){
		this.act=act;
		context=this.act.getApplication();
		
		left=(Button) act.findViewById(R.id.bottom_btn_left);
		right=(Button) act.findViewById(R.id.bottom_btn_right);
		left.setOnClickListener(this);
		right.setOnClickListener(this);
	}
	
	public void setBtnVal(String leftBtn,String rightBtn){
		left.setText(leftBtn);
		right.setText(rightBtn);
	}
	
	
	public void setOnclickBtn(BottomBtnOnclickListener Imp){
		this.leftImp=Imp;
		this.rightImp=Imp;
	}
	

	@Override
	public void onClick(View v) {
		if(v==left){
			leftImp.onClick(v,0);
		}else if(v==right){
			rightImp.onClick(v,1);
		}
	}

}
