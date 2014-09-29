package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.JsonHelper;

public class Bukao extends BaseActivity {
	
	@ViewInject(R.id.bukao_lv)
	private ListView lv;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_bukao);
		setTitle("≤πøº≤È—Ø");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		ViewUtils.inject(this);
		
		String data=getIntent().getStringExtra("data");
		try {
			List<Map<String,Object>> list=new JsonHelper(data).parseJson(new String[]{"subject","bktime","bkroom"},
					new String[]{"≤πøºøŒ≥Ã: ","≤πøº ±º‰: ","≤πøºΩÃ “: "});
			if(list.size()>0){
				lv.setAdapter(new SimpleAdapter(Bukao.this, list, R.layout.item_bukao_lv, 
						new String[]{"subject","bktime","bkroom"},
						new int[]{R.id.item_bukao_sub,R.id.item_bukao_time,R.id.item_bukao_room}));
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
		default:
			break;
		}
	}

}
