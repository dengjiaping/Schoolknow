package com.pw.schoolknow.widgets;


import com.pw.schoolknow.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 自定义组件
 * 用于显示More中的更多选项
 * @author wei8888go
 *
 */

public class MoreStyle2Item extends LinearLayout {
	
	public TextView tv;
	public NoScrollGridView gv;
	
	public interface onItemClickImp{
		public void onItemClick(int position);
	}
	
	public onItemClickImp callBack = null; 

	public MoreStyle2Item(Context context) {
		super(context);
		Init(context);
		
	}
	@SuppressLint("NewApi")
	public MoreStyle2Item(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs, defStyle);
		Init(context);
	}


	public MoreStyle2Item(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init(context);
	}
	
	public void Init(Context context){
		View convertView = LayoutInflater.from(context).inflate(R.layout.item_more_style2, null,false);
		tv=(TextView) convertView.findViewById(R.id.more_style2_title);
		gv=(NoScrollGridView) convertView.findViewById(R.id.more_style2_gv);
		gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				callBack.onItemClick(position);
			}
		});
		this.addView(convertView);
	}


	public void setTitle(String title){
		if(title.trim().length()!=0){
			tv.setText("-  "+title+"  -");
			//tv.setText(title);
		}
	}
	public void setAdapter(BaseAdapter adapter){
		if(adapter!=null){
			gv.setAdapter(adapter);
		}
	}
	public void setOnItemClick(onItemClickImp imp){
		if(imp!=null){
			callBack=imp;
		}
	}

}
