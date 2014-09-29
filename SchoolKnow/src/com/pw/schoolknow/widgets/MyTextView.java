package com.pw.schoolknow.widgets;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class MyTextView extends TextView {
	
	private float screen_density;
	private int borderColor;
	private Boolean showLeftBorder;
	private Boolean showBottomBorder;
	
	public MyTextView(Context context) {
		super(context);
		init(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void init(Context context) {
		borderColor=Color.parseColor("#D1D0CA");
		showLeftBorder=false;
		showBottomBorder=false;
	
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		this.screen_density = metrics.density;
	}
	
	public void setBorderColor(int color){
		borderColor=color;
	}
	
	public void setLeftBorder(Boolean b){
		showLeftBorder=b;
	}
	public void setBottomBorder(Boolean b){
		showBottomBorder=b;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		Paint p=null;
		if(showLeftBorder){
			p = new Paint();
			p.setColor(borderColor);
			p.setStyle(Style.FILL);
			canvas.drawRect(0, 0, 1 * screen_density,
					this.getHeight() , p);
		}
		if(showBottomBorder){
			p = new Paint();
			p.setColor(borderColor);
			p.setStyle(Style.FILL);
			canvas.drawRect(0, getHeight()-1,this.getWidth() * screen_density,
					this.getHeight() * screen_density, p);
		}
	}
	
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	

}
