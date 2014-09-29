package com.pw.schoolknow.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.db.ScheduleDB;
import com.pw.schoolknow.helper.ScheduleHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;
import com.pw.schoolknow.widgets.MyAlertMenu;
import com.pw.schoolknow.widgets.MyAlertMenu.MyDialogMenuInt;

/**
 * 课程表管理
 * @author wei8888go
 *
 */
public class ScheduleManage extends BaseActivity {
	
	private GridView gridview=null;
	private List<Map<String,Object>> list=null;
	private MyAlertMenu mam=null;
	private MyAlertDialog mad=null;
	private ScheduleHelper sh;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_schedule_manage);
		setTitle("课程表管理");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		gridview=(GridView) super.findViewById(R.id.schedule_manage_gallery);
		list=new ScheduleDB(this,ScheduleDB.TB_SYMANAGE).getSyManage();
		gridview.setAdapter(new SimpleAdapter(this, list, R.layout.item_schedule_manage,
				new String[]{"classname","term","classid","term_temp"},
				new int[]{R.id.schedule_manage_listview_item_className,
				R.id.schedule_manage_listview_item_term,R.id.schedule_manage_listview_item_classid,
				R.id.schedule_manage_listview_item_term_temp}));
		gridview.setOnItemClickListener(new OnItemClickListenerimp());
		
		sh=new ScheduleHelper(this);
		
		
	}
	
	public class OnItemClickListenerimp implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			@SuppressWarnings("unchecked")
			HashMap<String,Object> map=(HashMap<String,Object>)gridview.
					getItemAtPosition(position);
			final String classid=map.get("classid").toString();
			final String className=map.get("classname").toString();
			final String classTerm=map.get("term").toString().replace("\n", "");
			final String Term_temp=map.get("term_temp").toString();
			mam=new MyAlertMenu(ScheduleManage.this,new String[]{"载入课表","删除课表","取消"});
			mam.setOnItemClickListener(new MyDialogMenuInt() {
				@Override
				public void onItemClick(int position) {
					switch(position){
					case 0:
						sh.setCurrentScheduleClassId(classid);
						sh.setCurrentScheduleTerm(Term_temp);
						Intent it=new Intent(ScheduleManage.this,Main.class);
						it.putExtra("param","3");
						startActivity(it);
						finish();
						break;
					case 1:
						mad=new MyAlertDialog(ScheduleManage.this);
						mad.setTitle("消息提示");
						mad.setMessage("你确定要删除\n\n"+className+"班\n("+classTerm+")\n\n课程表吗?");
						mad.setLeftButton("确定",new MyDialogInt() {
							@Override
							public void onClick(View view) {
								mad.dismiss();
								String currClassId=sh.getCurrentScheduleClassId();
								String currTerm=sh.getCurrentScheduleTerm();
								if(currClassId.equals(classid)&&currTerm.equals(Term_temp)){
									T.showShort(ScheduleManage.this,"当前正在使用的课程表不能被删除!");
								}else{
									try{
										new ScheduleDB(ScheduleManage.this,ScheduleDB.TB_SYMANAGE).
											deleteManage(classid,Term_temp);
										new ScheduleDB(ScheduleManage.this,ScheduleDB.TB_SYLLABUS).
											deleteSchedule(classid,Term_temp);
										T.showShort(ScheduleManage.this,"课程表删除成功！");
									}catch(Exception e){
										T.showShort(ScheduleManage.this,"删除出现异常！");
									}finally{
										onCreate(null);
									}
								}
							}
						});
						mad.setRightButton("取消",new MyDialogInt() {
							@Override
							public void onClick(View view) {
								mad.dismiss();
							}
						});
						break;
					case 2:
						break;
					default:
						break;
					}
				}
			});
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			this.finish();
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			break;
		}

	}

}
