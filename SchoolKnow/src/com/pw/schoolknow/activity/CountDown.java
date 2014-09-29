package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.CountDownAdapter;
import com.pw.schoolknow.app.MyApplication;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.CountdownItem;
import com.pw.schoolknow.db.CountdownDB;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertMenu;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;
import com.pw.schoolknow.widgets.MyAlertMenu.MyDialogMenuInt;

public  class CountDown extends BaseActivity {
	
	private MyAlertDialog mad;
	private ListView lv=null;
	private List<CountdownItem> list;
	public  final static String SER_KEY = "com.pw.schoolknow.ser";
	
	private MyAlertMenu mam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_count_down);
		setTitle("倒数日");
		setTitleBar(R.drawable.btn_titlebar_back,R.drawable.navigationbar_add);
		
		lv=(ListView) super.findViewById(R.id.countdown_lv);
		
		list=sortCountDown(new CountdownDB(this).getList());
		lv.setAdapter(new CountDownAdapter(this,list));
		
		//单击事件
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent it=new Intent(CountDown.this,CountDownUpdate.class);
				CountdownItem item=(CountdownItem) lv.getItemAtPosition(position);
				Bundle mBundle = new Bundle();  
			    mBundle.putSerializable(SER_KEY,item);  
			    it.putExtras(mBundle);  
				startActivity(it);
			}
		});
		
		//长按事件
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE); 
				vibrator.vibrate(100);
				final int real_position=position;
				mam=new MyAlertMenu(CountDown.this, new String[]{"编辑","删除","取消"});
				mam.setOnItemClickListener(new MyDialogMenuInt() {
					@Override
					public void onItemClick(int position) {
						switch(position){
						case 0:
							Intent it=new Intent(CountDown.this,CountDownUpdate.class);
							CountdownItem item=(CountdownItem) lv.getItemAtPosition(real_position);
							Bundle mBundle = new Bundle();  
						    mBundle.putSerializable(SER_KEY,item);  
						    it.putExtras(mBundle);  
							startActivity(it);
							break;
						case 1:
							mad=new MyAlertDialog(CountDown.this);
							mad.setTitle("消息提示");
							mad.setMessage("你确定要删除该条记录吗?");
							mad.setLeftButton("确定", new MyDialogInt() {
								@Override
								public void onClick(View view) {
									mad.dismiss();
									CountdownItem item=(CountdownItem) lv.getItemAtPosition(real_position);
									new CountdownDB(CountDown.this).delete(item.getId());
									T.showShort(MyApplication.getInstance(), "删除成功");
									onCreate(null);
								}
							});
							mad.setRightButton("取消", new MyDialogInt() {
								@Override
								public void onClick(View view) {
									mad.dismiss();
								}
							});
							break;
						case 2:
							break;
						}
					}
				});
				return false;
			}
		});
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			final MyAlertMenu mam=new MyAlertMenu(CountDown.this, new String[]{"本地添加","在线添加","取消"});
			mam.setOnItemClickListener(new MyDialogMenuInt() {
				@Override
				public void onItemClick(int position) {
					switch(position){
					case 0:
						Intent it=new Intent(CountDown.this,CountDownUpdate.class);
						CountdownItem item=null;
						Bundle mBundle = new Bundle();  
					    mBundle.putSerializable(SER_KEY,item);  
					    it.putExtras(mBundle);  
						startActivity(it);
					case 2:
						mam.dismiss();
						break;
					default:
						break;
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	/**
	 * 特定排序
	 * @param list
	 * @return
	 */
	public static List<CountdownItem> sortCountDown(List<CountdownItem> ls){
		List<CountdownItem> list=new ArrayList<CountdownItem>();
		List<CountdownItem> before=new ArrayList<CountdownItem>();
		List<CountdownItem> after=new ArrayList<CountdownItem>();
		for(CountdownItem item:ls){
			long currentTime= System.currentTimeMillis();
			if(item.getTime_samp()>=currentTime){
				after.add(item);
			}else{
				before.add(item);
			}
		}
		Collections.sort(after);
		Collections.sort(before);
		list.addAll(after);
		list.addAll(before);
		return list;
	}
	

}
