package com.pw.schoolknow.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.SudiHistoryAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.bean.SudiBean;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyDialogMenu;
import com.pw.schoolknow.widgets.MyDialogMenu.OnItemClickCallBack;

public class SudiHistory extends BaseActivity {
	
	@ViewInject(R.id.sudi_history_lv)
	private ListView lv;
	
	private List<SudiBean> dbList;
	private SudiHistoryAdapter adapter;
	
	private MyDialogMenu mdm;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudi_history_act);
		setTitle("查询记录");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		ViewUtils.inject(this);
		
		DbUtils db = DbUtils.create(this);
		try {
			dbList=db.findAll(
					Selector.from(SudiBean.class).orderBy("id",true).limit(50));
			adapter=new SudiHistoryAdapter(dbList, this);
			lv.setAdapter(adapter);
		} catch (DbException e) {
			//e.printStackTrace();
		}
		
		
	}
	@OnItemClick(R.id.sudi_history_lv)
	public void itemClick(AdapterView<?> parent, View view, int position,long id) {  
		
		mdm=new MyDialogMenu(this, new String[]{"取消","查看","删除","删除全部"});
		mdm.setOnItemClick(new OnItemClickCallBack() {
			public void OnItemClick(View view, int position) {
				SudiBean item=dbList.get(position);
				DbUtils db = DbUtils.create(SudiHistory.this);
				switch(position){
				case 1:
					Intent it=new Intent(SudiHistory.this,SudiContent.class);
					it.putExtra("sudiCode", item.getSudiCode());
					it.putExtra("sudiName", item.getSudiName());
					it.putExtra("bookCode",item.getBookid());
					startActivity(it);
					finish();
					break;
				case 2:
					int itemId=item.getId();
					try {
						db.deleteById(SudiBean.class, WhereBuilder.b("id", "=", itemId));
						dbList.remove(position);
						adapter.notifyDataSetInvalidated();
						T.showShort(SudiHistory.this, "删除成功");
					} catch (DbException e) {
						e.printStackTrace();
					} 
					break;
				case 3:
					try {
						db.dropTable(SudiBean.class); 
						T.showShort(SudiHistory.this, "删除成功");
						finish();
					} catch (DbException e) {
						e.printStackTrace();
					}
					break;
				case 0:
					break;
				default:
					break;
				}
				mdm.dismiss();
			}
		});
		
		
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
