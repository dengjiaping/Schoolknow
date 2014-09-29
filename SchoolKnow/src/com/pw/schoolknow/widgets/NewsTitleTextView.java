package com.pw.schoolknow.widgets;

import android.widget.TextView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;

public class NewsTitleTextView extends TextView {
	private boolean isVerticalLine = false;
	private boolean isHorizontaline = false;
	private int verticalLineColor = Color.LTGRAY;
	private int horizontalineColor = Color.RED;
	private float screen_density;
	
	public NewsTitleTextView(Context context) {
		super(context);
		init(context);
	}

	public NewsTitleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public NewsTitleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void init(Context context) {
		this.setGravity(Gravity.CENTER);
		this.setTextColor(Color.BLACK);
		this.setBackgroundColor(Color.WHITE);
	
		DisplayMetrics metrics = new DisplayMetrics();
		((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
		this.screen_density = metrics.density;
	}

	/**
	 * 设置控件底部是否需要横线
	 * @param is
	 */
	public void setIsHorizontaline(boolean is) {
		this.isHorizontaline = is;
		invalidate();
	}

	/**
	 * 设置控件左边是否需要竖线
	 * @param is
	 */
	public void setIsVerticalLine(boolean is) {
		this.isVerticalLine = is;
	}

	/**
	 * 设置横线颜色
	 * @param color 颜色资源
	 */
	public void setHorizontalineColor(int color) {
		this.horizontalineColor = color;
	}

	/**
	 * 设置数显颜色
	 * @param color 颜色资源
	 */
	public void setVerticalLineColor(int color) {
		this.verticalLineColor = color;
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		Paint paint;
		if (isVerticalLine) {
			paint = new Paint();
			paint.setColor(verticalLineColor);
			paint.setStyle(Style.FILL);
			canvas.drawRect(0, 14, 1 * screen_density,
					this.getHeight() -14 , paint);
		}
		if (isHorizontaline) {
			paint = new Paint();
			paint.setColor(horizontalineColor);
			paint.setStyle(Style.FILL);
			 canvas.drawRect(5, getHeight()-6, getWidth() * screen_density, getHeight()
			 * screen_density, paint);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
}
