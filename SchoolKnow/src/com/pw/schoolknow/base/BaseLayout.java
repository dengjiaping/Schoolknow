package com.pw.schoolknow.base;

import com.pw.schoolknow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * titleBar布局基类文件
 * @author wei8888go
 *
 */
public class BaseLayout extends RelativeLayout {
	
	public TextView TitleText;
	public ImageButton leftBtn,rightBtn;
	public Button leftBtn2,rightBtn2;
	public LinearLayout titleContent;
	
	public View base;

	public BaseLayout(Context context) {
		super(context);
	}
	
	public BaseLayout(Context context, int layoutId) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		p.addRule(RelativeLayout.ALIGN_PARENT_TOP);p.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		View titleView = inflater.inflate(R.layout.part_titlebar, null);
		addView(titleView, p);
		
		RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		p1.addRule(RelativeLayout.BELOW, titleView.getId());
		View contView = inflater.inflate(layoutId, null, false);
		base=contView;
		addView(contView, p1);
		leftBtn=(ImageButton) findViewById(R.id.leftButton);
		rightBtn=(ImageButton) findViewById(R.id.rightButton);
		TitleText=(TextView) findViewById(R.id.titleText);
		leftBtn2=(Button) findViewById(R.id.leftButton2);
		rightBtn2=(Button) findViewById(R.id.rightButton2);
		titleContent=(LinearLayout) findViewById(R.id.titleContent);
	}

}
