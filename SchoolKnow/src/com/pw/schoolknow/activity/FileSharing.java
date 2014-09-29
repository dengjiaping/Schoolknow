package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.widgets.MyPopMenu;
import com.pw.schoolknow.widgets.MyPopMenu.MyPopMenuImp;

public class FileSharing extends BaseActivity {
	
	private ListView lv=null;
	private String[][] data=new String[][]{
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"},
			{"歌月徘徊","一天前","华东交大高等数学期末考试试卷A","大小:20M","下载量:100"}
	};
	private List<Map<String,Object>> list;
	private MyPopMenu popmenu;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_file_sharing);
		setTitle("资料共享");
		setTitleBar(R.drawable.btn_titlebar_back,R.drawable.btn_titlebar_select);
		
		lv=(ListView) super.findViewById(R.id.sharing_lv);
		list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<data.length;i++){
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("name", data[i][0]);
			map.put("time", data[i][1]);
			map.put("title",data[i][2]);
			map.put("size", data[i][3]);
			map.put("down", data[i][4]);
			list.add(map);
		}
		lv.setAdapter(new SimpleAdapter(this, list,R.layout.item_file_sharing_lv,
				new String[]{"name","time","title","size","down"},
				new int[]{R.id.sharing_item_user_name,R.id.sharing_item_time,
				R.id.sharing_item_title,R.id.sharing_item_size,R.id.sharing_item_downloadNum}));
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch (buttonId) {
		case 1:
			finish();
			break;
		case 2:
			popmenu=new MyPopMenu(FileSharing.this);
			popmenu.addItems(new String[]{"上传","搜索"});
			popmenu.showAsDropDown(v);
			popmenu.setOnItemClickListener(new MyPopMenuImp() {
				@Override
				public void onItemClick(int index) {
					switch(index){
					case 0:
						Intent it=new Intent(FileSharing.this,FileSharingUpload.class);
						it.putExtra("action","false");
						startActivity(it);
						break;
					case 1:
						break;
					}
					
				}
			});
			break;
		default:
			break;
		}
	}

}
