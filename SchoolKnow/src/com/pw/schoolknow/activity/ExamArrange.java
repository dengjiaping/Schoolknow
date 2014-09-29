package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.TermHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyProgressBar;

public class ExamArrange extends BaseActivity implements OnItemSelectedListener {
	
	private Spinner grade;
	private Spinner Mclass;
	private Button btn;
	private MyProgressBar pd=null;
	
	private List<String> className=new ArrayList<String>();
	private List<String> classId=new ArrayList<String>();
	
	private String searchId=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_exam_arrange);
		setTitle("考试安排");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		this.grade=(Spinner) super.findViewById(R.id.exam_search_grade);
		this.Mclass=(Spinner) super.findViewById(R.id.exam_search_class);
		this.btn=(Button) super.findViewById(R.id.exam_search_btn);
		
		//选择年级
		this.grade.setPrompt("请选择你的年级");
		this.grade.setOnItemSelectedListener(this);
		ArrayAdapter<CharSequence> gradeAda=new ArrayAdapter<CharSequence>(this,
				android.R.layout.simple_spinner_item,TermHelper.getGrade());
		gradeAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.grade.setAdapter(gradeAda);
		
		//选择班级
		this.Mclass.setPrompt("请选择你的班级");
		this.Mclass.setOnItemSelectedListener(this);
		
		
		this.btn.setOnClickListener(new onclickListener());
	}
	

	@SuppressLint("HandlerLeak")
	Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 102:
				try {					
					List<Map<String,Object>> all=new JsonHelper(msg.obj.toString()).parseJson(
							new String[]{"id","name"},
							new String[]{"",""});
					Iterator<Map<String,Object>> iter=all.iterator();
					className.clear();
					classId.clear();
					while(iter.hasNext()){
						Map<String,Object> map=iter.next();
						className.add(map.get("name").toString());
						classId.add(map.get("id").toString());
					}
				} catch (Exception e1){
					e1.printStackTrace();
				}
				ArrayAdapter<String> classAdapter=new ArrayAdapter<String>(ExamArrange.this,
						android.R.layout.simple_spinner_item,className);
				classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				Mclass.setAdapter(classAdapter);
				break;
			case 101:
				String data=msg.obj.toString();
				Intent it=new Intent(ExamArrange.this,ExamArrangeContent.class);
				it.putExtra("data",data);
				startActivity(it);
				break;
			default:
				break;
			}
			pd.dismiss();
		}
		
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent==grade){
			final String temp=parent.getItemAtPosition(position).toString();
			pd=new MyProgressBar(this);
			pd.setMessage("正在加载班级数据...");
			new Thread(){
				@Override
				public void run(){
					Message msg = new Message();
					msg.what = 102;
					msg.obj=GetUtil.getRes(ServerConfig.HOST+
					"/schoolknow/examarrange.php?getClassInfo=1&term="+temp);
					hander.sendMessage(msg);
				}
			}.start();
		}else{
			searchId=classId.get(position);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public class onclickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			if(searchId!=null&&!searchId.equals("")){
				pd=new MyProgressBar(ExamArrange.this);
				pd.setMessage("正在加载考试安排...");
				new Thread(){
					@Override
					public void run(){
						Message msg = new Message();
						msg.what = 101;
						msg.obj=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/"+
								"examarrange.php?classId="+searchId);
						hander.sendMessage(msg);
					}
				}.start();
			}else{
				T.showShort(ExamArrange.this, "请先选择班级");
			}
			
		}
		
	}

}
