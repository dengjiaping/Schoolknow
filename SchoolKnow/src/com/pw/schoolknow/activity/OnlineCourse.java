package com.pw.schoolknow.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;
import com.pw.schoolknow.widgets.MyProgressBar;

public class OnlineCourse extends BaseActivity {
	
	private ListView lv;
	private MyProgressBar mpb;
	private MyAlertDialog mad;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_online_course);
		setTitle("公共选修课");
		setTitleBar(R.drawable.btn_titlebar_back,R.drawable.toolbar_refresh_icon_nor);
		
		lv=(ListView) super.findViewById(R.id.online_course_lv);
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载选课信息");
		
		new AsyncLoad().execute(ServerConfig.HOST+"/schoolknow/onlineCourse.php?action=getCourse");
		
		lv.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				@SuppressWarnings("unchecked")
				HashMap<String,Object> map=(HashMap<String,Object>)lv.
						getItemAtPosition(position); 
				//String courseId=map.get("option").toString();
				String courseName=map.get("subject").toString();
				String courseParam=map.get("param").toString();
				
				mad=new MyAlertDialog(OnlineCourse.this);
				mad.setTitle("消息提示");
				mad.setMessage("你确定要选修《 "+courseName+"("+courseParam.split("\\-")[0]+") 》课程吗?");
				mad.setLeftButton("确定",new MyDialogInt() {
					@Override
					public void onClick(View view) {
						mad.dismiss();
						T.showLong(OnlineCourse.this,"= =!!技术问题尚未实现");
					}
				});
				mad.setRightButton("取消",new MyDialogInt() {
					@Override
					public void onClick(View view) {
						mad.dismiss();
					}
				});
			}
		});
		
	}
	public class AsyncLoad extends AsyncTask<String, Integer,String>{

		@Override
		protected String doInBackground(String... params) {
			String json=GetUtil.getRes(params[0]);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				List<Map<String,Object>> list=new JsonHelper(result).
						parseJson(new String[]{"subject","param","option","plan","reality","credit"},
								new String[]{"","","主讲老师:","计划选修人数:","已选人数:","学分:"});
				lv.setAdapter(new SimpleAdapter(OnlineCourse.this, list, R.layout.item_online_course_lv,
						new String[]{"subject","param","option","plan","reality","credit"},
						new int[]{R.id.online_course_subject,R.id.online_course_param,
						R.id.online_course_option,R.id.online_course_plan,
						R.id.online_course_reality,R.id.online_course_credit}));
				mpb.dismiss();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			onCreate(null);
			break;
		default:
			break;
		}
	}

}
