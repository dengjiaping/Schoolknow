package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.LibraryShowAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.JsonHelper;

public class LibraryData extends BaseActivity {
	
	private ListView listview;
	private List<Map<String,Object>> list;
	private String params;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_library_data);
		setTitle("Õº Èπ›");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		Intent it=getIntent();
		listview=(ListView) super.findViewById(R.id.library_data_lv);
		String json=it.getStringExtra("jsondata");
		try {
			list=new JsonHelper(json).parseJson(
					new String[]{"book","borrow","return","num","id","code"}, 
					new String[]{"","","","","",""});
		} catch (Exception e) {
			e.printStackTrace();
		}
		params="uid="+it.getStringExtra("uid")+"&pwd="+it.getStringExtra("pwd");
		listview.setAdapter(new LibraryShowAdapter(this, list,params));
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
