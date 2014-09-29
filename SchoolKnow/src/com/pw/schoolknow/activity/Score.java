package com.pw.schoolknow.activity;

import java.util.List;

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
import android.widget.EditText;
import android.widget.Spinner;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.InputHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.TermHelper;
import com.pw.schoolknow.utils.EncodeUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TextUtils;
import com.pw.schoolknow.widgets.BottomBtn;
import com.pw.schoolknow.widgets.BottomBtn.BottomBtnOnclickListener;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.StuidSelect;
import com.pw.schoolknow.widgets.StuidSelect.StuidSelectInterface;

public class Score extends BaseActivity {
	
	private Spinner term;
	private EditText edit;
	private Button submit;
	private List<CharSequence> termList=null;
	private MyProgressBar mpb;
	private String stuid;
	private String selectTerm;
	public MyAlertDialog mad;
	public Context mcontext;
	
	public BottomBtn bb;
	public LoginHelper lh;
	public Handler handler;
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_score);
		setTitle("成绩查询");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"班级");
		
		mcontext=this;
		lh=new LoginHelper(mcontext);
		
		
		term=(Spinner) super.findViewById(R.id.score_search_term);
		edit=(EditText) super.findViewById(R.id.score_search_edit);
		submit=(Button) super.findViewById(R.id.score_search_sumbit);
		
		submit.setOnClickListener(new submitClickListener());
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
		
		//设置学期
		this.termList=TermHelper.getAllTerm();
		term.setPrompt("请选择你要查询的学期");
		 ArrayAdapter<CharSequence> termAda= new ArrayAdapter<CharSequence>(this,
					android.R.layout.simple_spinner_item,termList);
		 termAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		term.setAdapter(termAda);
		
		
		//底部2个按钮
		bb=new BottomBtn(Score.this);
		bb.setBtnVal("继续上次查询", "查询我的成绩");
		bb.setOnclickBtn(new BottomBtnOnclickListener() {
			public void onClick(View v, int position) {
				if(position==0){
					T.showShort(mcontext, "该功能在以后版本推出");
				}else if(position==1){
					if(lh.hasLogin()){
						stuid=lh.getStuId();
						if(lh.hasBindStuid()){
							if(stuid.trim().length()==14){
								edit.setText(stuid);
								getScoreInfo();
							}else{
								T.showShort(mcontext, "学号有误，请重新登录");
								lh.logout();
							}
						}else{
							T.showShort(mcontext, "请先绑定学号");
						}
					}else{
						T.showShort(mcontext, "请先登录");
					}
				}
			}
		});
		
		
		//消息处理
		handler=new Handler(){
			public void handleMessage(Message msg) {
				if(msg.what==102){
					Intent it=new Intent(Score.this,ScoreData.class);
					it.putExtra("jsondata",msg.obj.toString());
					startActivity(it);
				}
				mpb.dismiss();
				submit.setClickable(true);
				super.handleMessage(msg);
			}
			
		};
	}
	
	public class submitClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			InputHelper.Hide(Score.this);
			stuid=edit.getText().toString();
			if(stuid.length()==14&&TextUtils.isAllNum(stuid)){
				getScoreInfo();
			}else if(TextUtils.isAllNum(stuid)&&stuid.length()!=14){
				T.showShort(mcontext, "输入的学号有误");
			}else if(stuid.length()<2||stuid.length()>4){
				T.showShort(mcontext, "姓名限制2-4个字");
			}else if(stuid.length()<2||stuid.length()>4){
				T.showShort(mcontext, "姓名限制2-4个字");
			}else if(TextUtils.contain(stuid,"%")||TextUtils.contain(stuid,"_")){
				T.showLong(mcontext, "同学使坏是不对的哟(*^__^*)");
			}else{
				String stuName=EncodeUtil.ToUtf8(stuid);
				StuidSelect stuidSelect=new StuidSelect(mcontext);
				stuidSelect.execute(stuName);
				stuidSelect.getSelected(new StuidSelectInterface() {
					public void onSelect(String stuid) {
						Score.this.stuid=stuid;
						getScoreInfo();
					}
				});
			}
			
		}
		
	}
	
	//获取成绩信息
	public void getScoreInfo(){
		if(mpb!=null){
			mpb.show();
		}else{
			mpb=new MyProgressBar(mcontext);
			mpb.setMessage("正在查询中...");
		}
		//设置按钮不可点击
		submit.setClickable(false);
		new Thread(new Runnable() {
			public void run() {
				String t=TermHelper.getNumTerm(selectTerm);
				Message msg=new Message();
				msg.what=102;
				String param=ServerConfig.HOST+"/schoolknow/api/getscore.php?stuid="+
						stuid+"&term="+t;
				msg.obj=GetUtil.getRes(param);
				handler.sendMessage(msg);
			}
		}).start();
		
	}
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			this.finish();
			break;
		case 2:
			Intent it=new Intent(Score.this,ScoreClass.class);
			startActivity(it);
			finish();
			break;
		case 3:
			break;
		default:
			break;
		}
	}

}
