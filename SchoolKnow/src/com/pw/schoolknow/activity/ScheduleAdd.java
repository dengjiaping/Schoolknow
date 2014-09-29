package com.pw.schoolknow.activity;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.pw.schoolknow.db.ScheduleDB;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.helper.ScheduleHelper;
import com.pw.schoolknow.helper.TermHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.ClassSelect;
import com.pw.schoolknow.widgets.ClassSelect.ClassSelectInterface;
import com.pw.schoolknow.widgets.MyProgressBar;

@SuppressLint("HandlerLeak")
public class ScheduleAdd extends BaseActivity implements OnClickListener {
	
	private Spinner selectTerm;
	private Button selectClass;
	private Button SubmitSelect;
	
	public ClassSelect newClass=null;
	public String classId="";
	public String term="";
	public String className="";
	
	public MyProgressBar mpb;
	private ScheduleHelper sh;
	public Context mcontext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_schedule_add);
		setTitle("添加课表");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		mcontext=this;
		
		sh=new ScheduleHelper(ScheduleAdd.this);
		
		selectTerm=(Spinner) super.findViewById(R.id.schedule_term_select_btn);
		selectClass=(Button) super.findViewById(R.id.schedule_class_select_btn);
		SubmitSelect=(Button) super.findViewById(R.id.schedule_class_select_sumbit);
		
		selectClass.setOnClickListener(this);
		SubmitSelect.setOnClickListener(this);
		
		//断网情况下提示
		if(!NetHelper.isNetConnected(mcontext)){
			T.showShort(mcontext,R.string.net_error_tip);
			return;
		}else{
			new Thread(new Runnable() {
				@Override
				public void run() {
					Message msg=new Message();
					msg.what=102;
					String param=ServerConfig.HOST+"/schoolknow/scheduleHelper.php?act=requestTerm";
					msg.obj=GetUtil.getRes(param);
					handler.sendMessage(msg);
				}
			}).start();
		}
		
		mpb=new MyProgressBar(ScheduleAdd.this);
		mpb.setMessage("正在加载数据...");
		
		selectTerm.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				term=TermHelper.getNumTerm(parent.getItemAtPosition(position).toString());
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		
	}
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==102){
				String data=String.valueOf(msg.obj);
				String[] term=data.split("\\|");
				selectTerm.setPrompt("请选择学期");
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(ScheduleAdd.this,
						android.R.layout.simple_spinner_item, TermHelper.getTermArr(term));
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				selectTerm.setAdapter(adapter);
			}else if(msg.what==103){
				if(msg.obj.toString()!="[]"){
					new ScheduleDB(ScheduleAdd.this,ScheduleDB.TB_SYLLABUS).insertSY(classId,
							TermHelper.getStringTerm2(term),msg.obj.toString());
					new ScheduleDB(ScheduleAdd.this,ScheduleDB.TB_SYMANAGE).insertManage(classId,
							className,TermHelper.getStringTerm2(term));
					sh.setCurrentScheduleClassId(classId);
					sh.setCurrentScheduleTerm(TermHelper.getStringTerm2(term));
					T.showShort(ScheduleAdd.this,"导入课表成功!");
					Intent it=new Intent(ScheduleAdd.this,Main.class);
					it.putExtra("param","3");
					startActivity(it);
					finish();
				}else{
					T.showShort(ScheduleAdd.this,"导入课表失败，请重新导入!%>_<%");
				}
			}
			mpb.dismiss();
		}
		
	};

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

	@Override
	public void onClick(View v) {
		if(v==selectClass){
			newClass=new ClassSelect(ScheduleAdd.this);
			newClass.setOnclickListener(new ClassSelectInterface() {
				@Override
				public void onClick(View view) {
					newClass.dismiss();
					classId=newClass.getClassId();
					className=newClass.getClassN();
					selectClass.setText(className);
				}
			});
			
		}else if(v==SubmitSelect){
			if(term.equals("")){
				T.showShort(ScheduleAdd.this,"请先选择学期");
			}else if(classId.equals("")){
				T.showShort(ScheduleAdd.this,"请先选择班级");
			}else if(new ScheduleDB(ScheduleAdd.this,ScheduleDB.TB_SYMANAGE).existManage(classId, TermHelper.getStringTerm2(term))){
				T.showShort(ScheduleAdd.this,"该课表已经下载到本地,请勿重复添加!");
				return;
			}else{
				mpb=new MyProgressBar(ScheduleAdd.this);
				mpb.setMessage("正在导入课表中...");
				new Thread(){
					@Override
					public void run(){
						Message msg=new Message();
						msg.what=103;
						msg.obj=GetUtil.getRes(ServerConfig.HOST+
								"/schoolknow/schedule.php?classid="+classId+"&term="+term);
						handler.sendMessage(msg);
						
					}
				}.start();
			}
		}
	}

}
