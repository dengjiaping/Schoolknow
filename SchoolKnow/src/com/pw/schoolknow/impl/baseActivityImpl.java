package com.pw.schoolknow.impl;

import android.graphics.drawable.Drawable;
import android.view.ViewGroup;


/**
 * baseActivity需要实现的方法接口
 * @author wei8888go
 *
 */
public interface baseActivityImpl {
	
	abstract void setContentView(int layoutId);
	abstract void setTitle(String title);
	abstract void setTitle(String title,Drawable d);
	abstract void setTitle(String title,int drawRes);
	abstract void setTitle(String title,int drawRes,int padding);
	abstract void setTitle(ViewGroup view);
	abstract void setTitleBar(int leftBtnDraw,int rightBtnDraw);
	abstract void setTitleBar(int leftBtnDraw,String leftVal,int rightBtnDraw,String rightVal);
	abstract void setTitleBar(int leftBtnDraw,int leftbtnBg,int rightBtnDraw,int rightbtnBg);

}
