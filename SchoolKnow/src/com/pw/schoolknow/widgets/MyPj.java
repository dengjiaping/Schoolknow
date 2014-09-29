package com.pw.schoolknow.widgets;

import com.pw.schoolknow.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 评教选择评分组件
 * @author wei8888go
 *
 */

public class MyPj extends LinearLayout{
	
	private RadioGroup group;

	public MyPj(Context context) {
		super(context);
	}
	
	public MyPj(Context context,String tip){
		super(context);
		View layout = LayoutInflater.from(context).inflate(R.layout.part_access_selector, null);
		TextView tv_tip=(TextView) layout.findViewById(R.id.pj_lv_tip);
		tv_tip.setText(tip);
		
		group=(RadioGroup) layout.findViewById(R.id.pj_lv_radio);
		
		addView(layout, new LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
	}
	
	//获取选择按钮
	public int getSelectItem(){
		int k=0;
		switch(group.getCheckedRadioButtonId()){
		case R.id.pj_lv_you:
			k=1;
			break;
		case R.id.pj_lv_liang:
			k=2;
			break;
		case R.id.pj_lv_zhong:
			k=3;
			break;
		case R.id.pj_lv_cha:
			k=4;
			break;
		default:
			break;
		}
		return k;		
	}
}
