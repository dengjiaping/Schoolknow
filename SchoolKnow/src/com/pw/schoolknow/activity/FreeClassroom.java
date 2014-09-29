package com.pw.schoolknow.activity;

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
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.widgets.MyProgressBar;

@SuppressLint("HandlerLeak")
public class FreeClassroom extends BaseActivity implements OnItemSelectedListener {
	
	private Spinner p1,p2,p3,p4;
	private Button btn;
	
	private int s1;    //记录选择项序列号
	private int s2;
	private int s3;
	private int s4;
	
	private MyProgressBar mpb;
	private final static int Search=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_free_classroom);
		setTitle("空教室查询");
		setTitleBar(0,0);
		
		p1=(Spinner) super.findViewById(R.id.free_classroom_week);
		p2=(Spinner) super.findViewById(R.id.free_classroom_xingqi);
		p3=(Spinner) super.findViewById(R.id.free_classroom_jieci);
		p4=(Spinner) super.findViewById(R.id.free_classroom_address);
		btn=(Button) super.findViewById(R.id.free_classroom_sumbit);
		
		p1.setAdapter(getResAdapter(R.array.week));
		p2.setAdapter(getResAdapter(R.array.xingqi));
		p3.setAdapter(getResAdapter(R.array.jieci));
		p4.setAdapter(getResAdapter(R.array.where));
		
		p1.setOnItemSelectedListener(this);
		p2.setOnItemSelectedListener(this);
		p3.setOnItemSelectedListener(this);
		p4.setOnItemSelectedListener(this);
		
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mpb=new MyProgressBar(FreeClassroom.this);
				mpb.setMessage("正在查询中...");
				new Thread(new Runnable() {
					@Override
					public void run() {
						Message msg=new Message();
						msg.what=Search;
						String param="a="+s1+"&b="+s2+"&c="+s3+"&d="+s4;
						msg.obj=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/freeclassroom.php?"+param);
						handler.sendMessage(msg);
					}
				}).start();
			}
		});
	}
	
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case Search:
				Intent it=new Intent(FreeClassroom.this,FreeClassroomContent.class);
				it.putExtra("data",msg.obj.toString());
				startActivity(it);
				break;
			default:
				break;
			}
			mpb.dismiss();
		}
		
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {

	}
	
	
	/**
	 * 资源文件的Adapter
	 * @return
	 */
	public ArrayAdapter<CharSequence> getResAdapter(int res){
		ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(FreeClassroom.this,
				res,android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		return adapter;
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if(parent==p1){
			s1=position;
		}else if(parent==p2){
			s2=position;
		}else if(parent==p3){
			s3=position;
		}else if(parent==p4){
			s4=position;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}
