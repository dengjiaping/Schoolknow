package com.pw.schoolknow.activity;

import java.util.List;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.TermHelper;
import com.pw.schoolknow.utils.EncodeUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.ClassSelect;
import com.pw.schoolknow.widgets.ClassSelect.ClassSelectInterface;
import com.pw.schoolknow.widgets.MyProgressBar;

public class ScoreClass extends BaseActivity {
	
	private Spinner term;
	private TextView classname;
	private Spinner sub;
	
	private List<CharSequence> termList=null;
	private String selectTerm;
	private String classid;
	private String subjectName;
	private ClassSelect cs;
	private MyProgressBar mpb;
	
	private Button search;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_score_class);
		setTitle("班级成绩查询");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"个人");
		
		term=(Spinner) super.findViewById(R.id.class_score_search_term);
		sub=(Spinner) super.findViewById(R.id.class_score_search_sub);
		//设置学期
		this.termList=TermHelper.getAllTerm();
		term.setPrompt("请选择你要查询的学期");
		 ArrayAdapter<CharSequence> termAda= new ArrayAdapter<CharSequence>(this,
					android.R.layout.simple_spinner_item,termList);
		 termAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		term.setAdapter(termAda);
		
		//选择学期
		term.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				selectTerm=parent.getItemAtPosition(position).toString();
				
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		//选择班级
		classname=(TextView) super.findViewById(R.id.class_score_search_edit);
		classname.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				cs=new ClassSelect(ScoreClass.this);
				cs.setOnclickListener(new ClassSelectInterface() {
					public void onClick(View view) {
						classname.setText(cs.getClassN());
						classid=cs.getClassId();
						cs.dismiss();
						mpb=new MyProgressBar(ScoreClass.this);
						mpb.setMessage("正在加载课程信息...");
						new AsncLoadSub().execute();
					}
				});
			}
		});
		
		//选择课程
		sub.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View arg1,
					int position, long arg3) {
				subjectName=parent.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		search=(Button) super.findViewById(R.id.score_search_sumbit);
		search.setOnClickListener(new searchClassScore());
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			Intent it=new Intent(ScoreClass.this,Score.class);
			startActivity(it);
			finish();
			break;
		default:
			break;
		}
	}
	
	public class searchClassScore implements OnClickListener{
		public void onClick(View arg0) {
			if(classid==null||classid.equals("")){
				T.showShort(ScoreClass.this,"请选择班级");
			}else if(subjectName==null||subjectName.equals("")){
				T.showShort(ScoreClass.this,"请选择科目");
			}else{
				mpb.show();
				mpb.setMessage("正在查询中...");
				new AsyncGetClassScore().execute("");
			}
		}
		
	}
	
	
	public class AsncLoadSub extends AsyncTask<Void,Void,String>{
		protected String doInBackground(Void... params) {
			String t=TermHelper.getNumTerm(selectTerm);
			String url=ServerConfig.HOST+"/schoolknow/api/getExam.php?term="+t+"&classid="+classid;
			return GetUtil.getRes(url);
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			String[] data=result.split("\\|");
			sub.setPrompt("请选择课程");
			 ArrayAdapter<CharSequence> subAda= new ArrayAdapter<CharSequence>(ScoreClass.this,
						android.R.layout.simple_spinner_item,data);
			 subAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 sub.setAdapter(subAda);
			 mpb.dismiss();
		}
		
		
	}
	
	public class AsyncGetClassScore extends AsyncTask<String,Void,String>{
		protected String doInBackground(String... str) {
			String result=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/api/classscore.php?"
					+"term="+TermHelper.getNumTerm(selectTerm)+"&classId="+classid+"&subject="
					+EncodeUtil.ToUtf8(subjectName));
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mpb.dismiss();
			Intent it=new Intent(ScoreClass.this,ScoreClassContent.class);
			it.putExtra("data",subjectName+"|"+result);
			startActivity(it);
		}	
	}

}
