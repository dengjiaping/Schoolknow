package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.JsonHelper;


@SuppressLint("HandlerLeak")
public class SecondClassContent extends BaseActivity {
	
	private ListView lv=null;
	private List<Map<String,Object>> list;
	private String[] tip=new String[]{"学号:","姓名:",
			"第一学年科技创新学分:","第一学年公益劳动学分:","第一学年其他学分:",
			"第二学年科技创新学分:","第二学年公益劳动学分:","第二学年其他学分:",
			"第三学年科技创新学分:","第三学年公益劳动学分:","第三学年其他学分:",
			"第四学年科技创新学分:","第四学年公益劳动学分:","第四学年其他学分:",
			"第五学年科技创新学分:","第五学年公益劳动学分:","第五学年其他学分:",
			"合计科技创新学分:","合计公益劳动学分:","合计其他学分:",
			"总学分:"};
	private int[] res=new int[]{
			R.id.second_class_content_item_uid,R.id.second_class_content_item_name,
			R.id.second_class_content_item_1p1,R.id.second_class_content_item_1p2,
			R.id.second_class_content_item_1p3,R.id.second_class_content_item_2p1,
			R.id.second_class_content_item_2p2,R.id.second_class_content_item_2p3,
			R.id.second_class_content_item_3p1,R.id.second_class_content_item_3p2,
			R.id.second_class_content_item_3p3,R.id.second_class_content_item_4p1,
			R.id.second_class_content_item_4p2,R.id.second_class_content_item_4p3,
			R.id.second_class_content_item_5p1,R.id.second_class_content_item_5p2,
			R.id.second_class_content_item_5p3,R.id.second_class_content_item_6p1,
			R.id.second_class_content_item_6p2,R.id.second_class_content_item_6p3,
			R.id.second_class_content_item_add};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_second_class_content);
		setTitle("第二课堂学分查询");
		setTitleBar(0,0);
		
		lv=(ListView) super.findViewById(R.id.second_class_content_lv);
		String jsonData=getIntent().getStringExtra("data");
		
		list=new ArrayList<Map<String,Object>>();
		
		try {
			list=new JsonHelper(jsonData).parseJson(create1To21(),tip);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		lv.setAdapter(new SimpleAdapter(this, list,R.layout.item_second_class_content,
				create1To21(),res));
	}
	
	//生成包含1-21字符的数组，用于解析json数据
		public String[] create1To21(){
			String[] temp=new String[21];
			for(int i=0;i<temp.length;i++){
				temp[i]=String.valueOf(i+1);
			}
			return temp;		
		}
	
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
