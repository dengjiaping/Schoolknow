package com.pw.schoolknow.widgets;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.utils.GetUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class ClassSelect implements OnItemSelectedListener {

	public Context context=null;
	private Dialog mDialog;
	private Spinner college,term,classname;
	private ArrayAdapter<CharSequence> colleageAda=null;
	private List<String> term_list=null;
	private ArrayAdapter<String> term_list_ada=null;
	
	String current_college,current_term,current_class;
	private MyProgressBar mpb=null; 
	
	private List<String> class_list=null;
	private List<String> classid=null;
	private int classid_position=0;
	private ArrayAdapter<String> class_list_ada=null;
	
	public ClassSelect(Context context){
		this.context=context;
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.show();
		
		View layout = LayoutInflater.from(context).inflate(R.layout.wg_class_select, null);
		college=(Spinner) layout.findViewById(R.id.class_select_college);
		term=(Spinner) layout.findViewById(R.id.class_select_term);
		classname=(Spinner) layout.findViewById(R.id.class_select_classname);
		Button btn=(Button) layout.findViewById(R.id.class_select_submit);
		Button cancel=(Button) layout.findViewById(R.id.class_select_cancel);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(class_list.size()==0){
					mDialog.dismiss();
					return;
				}
				classselect.onClick(view);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mDialog.dismiss();
			}
		});
		
		
		//选择学院
		colleageAda=ArrayAdapter.createFromResource(context,
        		R.array.college,android.R.layout.simple_spinner_item);
		colleageAda.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		college.setAdapter(colleageAda);
		
		//选择学期
		term_list=new ArrayList<String>();
		int year=Calendar.getInstance().get(Calendar.YEAR);
		int month=Calendar.getInstance().get(Calendar.MONTH)+ 1;
		int startYear=month>=8?year:year-1;
		for(int i=startYear; i>startYear-5; i--){
			term_list.add(String.valueOf(i));
		}
		term_list_ada=new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_item,term_list);
		term_list_ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		term.setAdapter(term_list_ada);
		
		//选择班级
		class_list=new ArrayList<String>();
		classid=new ArrayList<String>();
		
		//注册事件
		college.setOnItemSelectedListener(this);
		term.setOnItemSelectedListener(this);
		classname.setOnItemSelectedListener(this);
		
		
		mDialog.setContentView(layout);
		
	}
		
	
	public void getClassName(){
		mpb=new MyProgressBar(context);
		mpb.setMessage("正在加载班级数据...");
		new Thread(){
			@Override
			public void run(){
				try {
					String temp_college=URLEncoder.encode(current_college,"UTF-8");
					Message msg=new Message();
					msg.what=102;
					msg.obj=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/class_select.php"+
					"?college="+temp_college+"&term="+current_term);
					hander.sendMessage(msg);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	@SuppressLint("HandlerLeak")
	Handler hander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==102){
				try {
					List<Map<String, Object>> all ;
					all = new JsonHelper(msg.obj.toString()).parseJson(
							new String[]{"name","code"},
							new String[]{"",""});
					Iterator<Map<String,Object>> iter=all.iterator();
					class_list.clear();
					classid.clear();
					while(iter.hasNext()){
						Map<String,Object> map=iter.next();
						class_list.add(map.get("name").toString());
						classid.add(map.get("code").toString());
					}
					class_list_ada=new ArrayAdapter<String>(context,
							android.R.layout.simple_spinner_item,class_list);
					class_list_ada.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					classname.setAdapter(class_list_ada);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mpb.dismiss();
		}
	};
	
	//事件传递接口
	public interface ClassSelectInterface{  
        public void onClick(View view);  
    } 
	
	ClassSelectInterface classselect=null;
	
	public void setOnclickListener(ClassSelectInterface classselect){
		this.classselect=classselect;
	}
	
	public String getClassId(){
		return classid.get(classid_position);
		
	}
	
	public String getClassN(){
		return class_list.get(classid_position);
		
	}
	
	public void dismiss(){
		mDialog.dismiss();		
	}

	//事件处理
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long arg3) {
		if(parent==college){
			current_college=parent.getItemAtPosition(position).toString();
			if(current_term!=null&&!current_term.equals("")){
				getClassName();
			}
		}else if(parent==term){
			current_term=parent.getItemAtPosition(position).toString();
			if(current_college!=null&&!current_term.equals("")){
				getClassName();
			}
		}else if(parent==classname){
			current_class=parent.getItemAtPosition(position).toString();
			classid_position=position;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
