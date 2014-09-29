package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.IndexAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.CountdownItem;
import com.pw.schoolknow.base.IndexItemBase;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.CountdownDB;
import com.pw.schoolknow.db.NewsDB;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.helper.VersionHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.XListView;
import com.pw.schoolknow.widgets.XListView.IXListViewListener;

public class Index extends BaseActivity {
	
	private XListView lv;
	private List<IndexItemBase> list;
	
	private MyProgressBar mpb;
	private IndexAdapter adapter;
	
	private String num="0";  //当前动态时间戳
	
	public  final static String SER_KEY = "com.pw.schoolknow.ser";
	
	public TextView title,time,day;
	public View head;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_index);
		setTitle("校园动态");
		setTitleBar(0,0);
		
		this.init();
		
	}
	
	public void init(){
		lv=(XListView) super.findViewById(R.id.index_lv);
		lv.setPullLoadEnable(true);
		
		//倒计时部分
		LayoutInflater inflater=LayoutInflater.from(Index.this);
		head = inflater.inflate(R.layout.part_index_countdown, null);
		title=(TextView) head.findViewById(R.id.index_countdown_title);
		time=(TextView) head.findViewById(R.id.index_countdown_time);
		day=(TextView) head.findViewById(R.id.index_countdown_day);
		head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent it=new Intent(Index.this,CountDown.class);
				startActivity(it);
			}
		});
	
		list=new ArrayList<IndexItemBase>();
		
		Collections.sort(list);
		adapter=new IndexAdapter(Index.this,list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				IndexItemBase item=(IndexItemBase) lv.getItemAtPosition(position);
				Intent it=new Intent(Index.this,NewsContent.class);
				Bundle mBundle = new Bundle();  
			    mBundle.putSerializable(SER_KEY,item);  
			    it.putExtras(mBundle);  
				startActivity(it);
			}
		});
		lv.setXListViewListener( new IXListViewListenerImp());
		
		if(!NetHelper.isNetConnected(this)){
			list.addAll(new NewsDB(this).getList());
			Collections.sort(list);
			adapter.notifyDataSetChanged();
			updateIndexCountDown();
			T.showShort(Index.this,R.string.net_error_tip);
			return;
		}
		
		mpb=new MyProgressBar(Index.this);
		mpb.setMessage("正在加载最新动态...");
		new IXListViewListenerImp().onRefresh();
		new VersionHelper(Index.this).updateTip();
		
	}
	
	//更新首页倒数日
	public void updateIndexCountDown(){
		CountdownItem item=new CountdownDB(this).getIndexTip();
		if(item==null){
			if(lv.getHeaderViewsCount()!=0){
				lv.removeHeaderView(head);
			}
		}else{
			if(lv.getHeaderViewsCount()<=1){
				lv.addHeaderView(head);
			}
			title.setText(item.getTitle());
			time.setText(TimeUtil.getDayTime(item.getTime_samp()));
			day.setText(TimeUtil.betweenDays(item.getTime_samp())+"");
		}
	}
	
	class IXListViewListenerImp implements IXListViewListener{
		@Override
		public void onRefresh() {
			updateIndexCountDown();
			if(!NetHelper.isNetConnected(Index.this)){
				T.showShort(Index.this,R.string.net_error_tip);
				lv.stopRefresh();
				lv.stopLoadMore();
				return;
			}
			if(list.size()!=0){
				Collections.sort(list);
				num=Long.toString(list.get(0).getTimesamp());
			}
			lv.setRefreshTime(""+TimeUtil.getUpdateTime(System.currentTimeMillis()));
			new AsyncLoadNews().execute("getNews",String.valueOf(num));
		}
		@Override
		public void onLoadMore() {
			if(!NetHelper.isNetConnected(Index.this)){
				T.showShort(Index.this,R.string.net_error_tip);
				lv.stopRefresh();
				lv.stopLoadMore();
				return;
			}
			if(list.size()!=0){
				Collections.sort(list);
				num=Long.toString(list.get(list.size()-1).getTimesamp());
				new AsyncLoadNews().execute("readMore",String.valueOf(num));
			}
			
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return false;
	}
	
	
	
	//异步载入新闻
	public class AsyncLoadNews extends AsyncTask<String, Void, String>{
		@Override
		protected String doInBackground(String... params) {
			String baseUrl=ServerConfig.HOST+"/schoolknow/newsManage.php?action=";
			String json=GetUtil.getRes(baseUrl+params[0]+"&num="+params[1]);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				List<Map<String,Object>> jsonList=new JsonHelper(result).parseJson(
						new String[]{"id","ti","tm","tp","ar","dp","pt"}, 
						new String[]{"","","","","","",""});
				for(Map<String,Object> map:jsonList){
					//活动
					IndexItemBase item=null;
					if(map.get("tp").equals("1")){
						item=new IndexItemBase(Integer.parseInt(map.get("id").toString()),1, map.get("ti").toString(),"",
								map.get("ar").toString(),Long.parseLong(map.get("tm").toString()), map.get("pt").toString());
					//新闻
					}else if(map.get("tp").equals("2")){
						item=new IndexItemBase(Integer.parseInt(map.get("id").toString()),2,map.get("ti").toString(),map.get("dp").toString(), 
								"", Long.parseLong(map.get("tm").toString()), "");
					//通知
					}else if(map.get("tp").equals("3")){
						item=new IndexItemBase(Integer.parseInt(map.get("id").toString()),3,map.get("ti").toString(),map.get("dp").toString(), 
								"", Long.parseLong(map.get("tm").toString()), "");
				    //阅读
					}else if(map.get("tp").equals("4")){
						item=new IndexItemBase(Integer.parseInt(map.get("id").toString()),4,map.get("ti").toString(),map.get("dp").toString(), 
								"", Long.parseLong(map.get("tm").toString()), "");
					}else{
						item=new IndexItemBase(Integer.parseInt(map.get("id").toString()),2,map.get("ti").toString(),map.get("dp").toString(), 
								"", Long.parseLong(map.get("tm").toString()), "");
					}
					list.add(item);
					
					//不存在本地数据库就更新
					NewsDB newsDB=new NewsDB(Index.this);
					if(!newsDB.hasExistNews(item.getId())){
						new NewsDB(Index.this).insert(item);
					}
				}
				Collections.sort(list);
				adapter.notifyDataSetChanged();
				mpb.dismiss();
				lv.stopRefresh();
				lv.stopLoadMore();


			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

}
