package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.XListView;

public class Jwc extends BaseActivity {
	
	private XListView lv=null;
	private List<Map<String,Object>> list=null;
	
	private Object[][] fun={{"成绩查询",R.drawable.demo},{"图书馆查询",R.drawable.demo},
			{"四六级查询",R.drawable.demo},{"网上选修",R.drawable.demo},{"考试安排",R.drawable.demo},
			{"补考安排",R.drawable.demo},{"空教室查询",R.drawable.demo},{"校友查询",R.drawable.demo},
			{"第二课堂学分查询",R.drawable.demo},{"网上评教",R.drawable.demo}};
	
	public Context mcontext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_jwc);
		setTitle("教务查询");
		setTitleBar(0,0);
		
		mcontext=this;
		
		lv=(XListView) super.findViewById(R.id.jwc_lv);
		lv.setPullLoadEnable(false);
		lv.setPullRefreshEnable(false);
		this.list=new ArrayList<Map<String,Object>>();
		for(int i=0;i<fun.length;i++){
			Map<String, Object> map=new HashMap<String,Object>();
			//map.put("img", String.valueOf(fun[i][1]));
			String tempStr=fun[i][0].toString();
			map.put("img", tempStr.substring(0,1));
			map.put("title",tempStr);
			this.list.add(map);
		}
		this.lv.setAdapter(new SimpleAdapter(this, list, R.layout.item_list_style2,
				new String[]{"img","title"}, new int[]{R.id.list_item_img,R.id.list_item_title}));
		
		
		
		this.lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent it=null;
				switch(position-1){
				case 0:
					it=new Intent(Jwc.this,Score.class);
					break;
				case 1:
					it=new Intent(Jwc.this,Library.class);
					break;
				case 2:
					it=new Intent(Jwc.this,Cet.class);
					break;
				case 3:
					it=new Intent(Jwc.this,OnlienCourseLogin.class);
					break;
				case 4:
					it=new Intent(Jwc.this,ExamArrange.class);
					break;
				case 5:
					it=new Intent(Jwc.this,BukaoInput.class);
					break;
				case 6:
					it=new Intent(Jwc.this,FreeClassroom.class);
					break;
				case 7:
					it=new Intent(Jwc.this,Who.class);
					break;
				case 8:
					it=new Intent(Jwc.this,SecondClass.class);
					break;
				case 9:
					//it=new Intent(Jwc.this,AccessContent.class);
					T.showShort(mcontext, "网上评教功能将在以后版本推出");
					break;
				default:
					it=new Intent(Jwc.this,Score.class);
				}
				if(it!=null){
					startActivity(it);
				}
			}
		});
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

}
