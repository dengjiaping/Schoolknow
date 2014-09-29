package com.pw.schoolknow.activity;

import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.JsonHelper;

public class FreeClassroomContent extends BaseActivity {
	
	private ListView lv;
	private List<Map<String,Object>> list;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_free_classroom_content);
		setTitle("空教室查询");
		setTitleBar(0,0);
		
		String json=getIntent().getStringExtra("data");
		try {
			list=new JsonHelper(json).parseJson(
					new String[]{"1","2","3","4"},
					new String[]{"教室:","座位数:","教室类型","备注："});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		lv=(ListView) super.findViewById(R.id.free_classroom_content_lv);
		lv.setAdapter(new SimpleAdapter(this, list, R.layout.item_free_classroom_content,
				new String[]{"1","2","3","4"},
				new int[]{R.id.free_classroom_content_item_name,R.id.free_classroom_content_item_num,
				R.id.free_classroom_content_item_type,R.id.free_classroom_content_item_param}));
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub
		
	}

}
