package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.JsonHelper;

public class ScoreClassContent extends BaseActivity {
	
	private TableLayout layout;
	private String subject;
	
	@SuppressLint("ResourceAsColor")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_score_class_data);
		setTitle("∞‡º∂≥…º®≤È—Ø");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		layout=(TableLayout) super.findViewById(R.id.score_class_data_layout);
		
		String data=getIntent().getStringExtra("data");
		String[] param=data.split("\\|");
		subject=param[0];
		String jsonData=param[1];
		try {
			List<Map<String,Object>> list=new JsonHelper(jsonData).parseJson(new String[]{
					"name","score"
			});
			for(int i=0;i<list.size();i++){
				TableRow row=new TableRow(this);
				Map<String,Object> map=list.get(i);
				for(int k=0;k<4;k++){
					TextView view=new TextView(this);
					String val="";
					switch(k){
					case 0:
						val=String.valueOf(i+1)+"°¢";
						view.setPadding(10, 0,5, 0);
						break;
					case 1:
						val=subject;
						view.setPadding(0, 0, 10, 0);
						break;
					case 2:
						val=map.get("name").toString();
						view.setPadding(10, 0, 10, 0);
						break;
					case 3:
						val=map.get("score").toString();
						view.setPadding(10, 0, 10, 0);
						break;
					default:
						break;
					}
	        		view.setText(val);
	        		view.setTextColor(R.color.weibo_textcolor);
	        		view.setTextSize(16);
	        		view.setGravity(Gravity.CENTER_HORIZONTAL);
	        		view.setPadding(5, 5, 5, 5);
	        		row.addView(view,k);
				}
				row.setPadding(0,5,0,5);
				LinearLayout.LayoutParams param_=new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT);
	        	layout.addView(row,param_);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}

}
