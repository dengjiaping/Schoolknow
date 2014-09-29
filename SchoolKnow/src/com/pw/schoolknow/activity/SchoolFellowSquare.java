package com.pw.schoolknow.activity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.SchoolFellowAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.SchoolFellowBase;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.SchoolfellowDB;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.FileUtils;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.XListView;
import com.pw.schoolknow.widgets.XListView.IXListViewListener;

public class SchoolFellowSquare extends BaseActivity {
	
	private XListView lv;
	private List<SchoolFellowBase> list;
	public static SchoolFellowAdapter adapter;
	
	public MyProgressBar mpb;
	
	public String num="0";
	public Context mcontext;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_schoolfellow);
		setTitle("花椒社区");
		setTitleBar(R.drawable.btn_titlebar_back,R.drawable.navigationbar_compose);
		
		mcontext=this;
		
		list=new ArrayList<SchoolFellowBase>();
		lv=(XListView) super.findViewById(R.id.school_fellow_listview);
		lv.setPullLoadEnable(true);
		
		FileUtils.createPath(PathConfig.BASEPATH+"/data");
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载中...");
		adapter=new SchoolFellowAdapter(SchoolFellowSquare.this, list);
		lv.setAdapter(adapter);
		lv.setXListViewListener( new IXListViewListenerImp());
		
		if(!NetHelper.isNetConnected(this)){
			try {
				List<SchoolFellowBase> dbItemList=SchoolfellowDB.getInstance(mcontext).getList();
				list.addAll(dbItemList);
				adapter.notifyDataSetInvalidated();
				T.showLong(mcontext, R.string.net_error_tip);
				mpb.dismiss();
				return;
			} catch (Exception e) {
				
			}
		}else{
			new AsyncloadSF().execute("getNews",num);
		}
		
		
	}
	
	
	class IXListViewListenerImp implements IXListViewListener{
		public void onRefresh() {
			mpb.show();
			mpb.setMessage("正在加载中...");
			if(!NetHelper.isNetConnected(mcontext)){
				T.showShort(mcontext,R.string.net_error_tip);
				lv.stopRefresh();
				lv.stopLoadMore();
				mpb.dismiss();
				return;
			}
			list.clear();
			num="0";
			new AsyncloadSF().execute("getNews",num);
			lv.setRefreshTime(""+TimeUtil.getUpdateTime(TimeUtil.getCurrentTime()));
		}
		public void onLoadMore() {
			if(!NetHelper.isNetConnected(mcontext)){
				T.showShort(mcontext,R.string.net_error_tip);
				lv.stopRefresh();
				lv.stopLoadMore();
				return;
			}
			if(list.size()!=0){
				num=String.valueOf(list.get(list.size()-1).getId());
			}
			new AsyncloadSF().execute("readMore",num);
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			Intent it=new Intent(SchoolFellowSquare.this,NewAsk.class);
			it.putExtra("param","schoolfellow");
			startActivity(it);
			finish();
			break;
		default:
			break;
		}
	}
	
	
	public class AsyncloadSF extends AsyncTask<String, Void, String>{
		protected String doInBackground(String... params) {
			String result=GetUtil.getRes(ServerConfig.HOST+"/schoolknow/Square/getNew.php"
					+"?action="+params[0]+"&num="+params[1]);
			return result;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(!result.trim().equals("[]")){
				try {
					List<Map<String,Object>> dataList=new JsonHelper(result).parseJson(
							new String[]{"id","uid","nn","ct","tm","num"});
					for(Map<String,Object> map:dataList){
						SchoolFellowBase base=new SchoolFellowBase(Integer.parseInt(map.get("id").toString()),
								map.get("uid").toString(),map.get("nn").toString(),map.get("tm").toString(),
								map.get("ct").toString(),map.get("num").toString());
						list.add(base);
						
						//保存在数据库
						SchoolfellowDB.getInstance(mcontext).save(base);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				adapter.notifyDataSetChanged();
			}
			mpb.dismiss();
			lv.stopRefresh();
			lv.stopLoadMore();
		}
		
		
		
	}

}
