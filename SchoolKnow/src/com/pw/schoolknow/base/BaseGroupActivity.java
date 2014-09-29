package com.pw.schoolknow.base;

import com.pw.schoolknow.R;
import com.pw.schoolknow.impl.baseActivityImpl;

import android.app.ActivityGroup;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

@SuppressWarnings("deprecation")
public abstract class BaseGroupActivity extends ActivityGroup  implements baseActivityImpl{
	
	protected BaseLayout baseLy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 设置布局文件
	 */
	@Override
	public void setContentView(int layoutId) {
		baseLy = new BaseLayout(this, layoutId);
		setContentView(baseLy);
		baseLy.leftBtn.setOnClickListener(new OnTitleBarClickListener());
		baseLy.rightBtn.setOnClickListener(new OnTitleBarClickListener());
		baseLy.TitleText.setOnClickListener(new OnTitleBarClickListener());
		baseLy.leftBtn2.setOnClickListener(new OnTitleBarClickListener());
		baseLy.rightBtn2.setOnClickListener(new OnTitleBarClickListener());
		
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		if(baseLy!=null){
			baseLy.TitleText.setText(title);
		}
	}
	
	public void setTitle(String title,Drawable d){
		if(baseLy!=null){
			baseLy.TitleText.setText(title);
			d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
			baseLy.TitleText.setCompoundDrawables(null, null, d, null);
			baseLy.TitleText.setCompoundDrawablePadding(5);
		}
	}
	public void setTitle(String title,int drawRes){
		if(baseLy!=null){
			baseLy.TitleText.setText(title);
			Drawable d=getResources().getDrawable(drawRes);
			d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
			baseLy.TitleText.setCompoundDrawables(null, null, d, null);
			baseLy.TitleText.setCompoundDrawablePadding(5);
		}
	}
	public void setTitle(String title,int drawRes,int padding){
		if(baseLy!=null){
			baseLy.TitleText.setText(title);
			Drawable d=getResources().getDrawable(drawRes);
			d.setBounds(0, 0, d.getIntrinsicWidth(),d.getIntrinsicHeight());
			baseLy.TitleText.setCompoundDrawables(null, null, d, null);
			baseLy.TitleText.setCompoundDrawablePadding(padding);
		}
	}
	
	
	@Override
	public void setTitle(ViewGroup view) {
		if(baseLy!=null){
			baseLy.TitleText.setVisibility(View.GONE);
			baseLy.titleContent.setVisibility(View.VISIBLE);
			baseLy.titleContent.addView(view);
		}
	}
	
	/**
	 * 设置按钮
	 * @param leftBtnDraw
	 * @param rightBtnDraw
	 */
	public void setTitleBar(int leftBtnDraw,int rightBtnDraw){
		if(baseLy!=null){
			ImageButton left=baseLy.leftBtn;
			ImageButton right=baseLy.rightBtn;
			
			if(leftBtnDraw>0){
				left.setImageResource(leftBtnDraw);
			}else{
				left.setVisibility(View.INVISIBLE);
			}
			
			if(rightBtnDraw>0){
				right.setImageResource(rightBtnDraw);
			}else{
				right.setVisibility(View.INVISIBLE);
			}
			
		}
	}
	
	public void setTitleBar(int leftBtnDraw,String leftVal,int rightBtnDraw,String rightVal){
		if(baseLy!=null){
			if(leftBtnDraw>0){
				baseLy.leftBtn.setImageResource(leftBtnDraw);
			}else{
				baseLy.leftBtn.setVisibility(View.GONE);
			}
			if(rightBtnDraw>0){
				baseLy.rightBtn.setImageResource(rightBtnDraw);
			}else{
				baseLy.rightBtn.setVisibility(View.GONE);
			}
			if(leftVal.length()>0){
				baseLy.leftBtn2.setVisibility(View.VISIBLE);
				baseLy.leftBtn2.setBackgroundResource(R.drawable.navigationbar_button_selecter);
				baseLy.leftBtn2.setText(leftVal);
				baseLy.rightBtn2.setPadding(10,0,10,0);
			}
			if(rightVal.length()>0){
				baseLy.rightBtn2.setVisibility(View.VISIBLE);
				baseLy.rightBtn2.setText(rightVal);
				baseLy.rightBtn2.setBackgroundResource(R.drawable.navigationbar_button_selecter);
				baseLy.rightBtn2.setPadding(10,0,10,0);
			}
		}
	}
	
	
	public void setTitleBar(int leftBtnDraw,int leftbtnBg,int rightBtnDraw,int rightbtnBg){
		if(baseLy!=null){
			ImageButton left=baseLy.leftBtn;
			ImageButton right=baseLy.rightBtn;
			
			if(leftBtnDraw>0){
				left.setImageResource(leftBtnDraw);
				if(leftbtnBg>0){
					left.setBackgroundResource(leftbtnBg);
				}
			}else{
				left.setVisibility(View.INVISIBLE);
			}
			
			if(rightBtnDraw>0){
				right.setImageResource(rightBtnDraw);
				if(rightbtnBg>0){
					right.setBackgroundResource(rightbtnBg);
				}
			}else{
				right.setVisibility(View.INVISIBLE);
			}
			
		}
	}
	
	
	/**
	 * 处理titleBar事件
	 * @param buttonId
	 * @param v
	 */
	protected abstract void HandleTitleBarEvent(int buttonId,View v);
	
	//传递事件
	public class OnTitleBarClickListener implements OnClickListener{
		@Override
		public void onClick(View view) {
			if(view==baseLy.leftBtn||view==baseLy.leftBtn2){
				HandleTitleBarEvent(1,view);
			}else if(view==baseLy.rightBtn||view==baseLy.rightBtn2){
				HandleTitleBarEvent(2,view);
			}else if(view==baseLy.TitleText){
				HandleTitleBarEvent(3,view);
			}
		}
		
	}
	
	
	
}
