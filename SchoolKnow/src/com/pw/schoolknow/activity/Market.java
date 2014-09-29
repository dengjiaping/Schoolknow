package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.MarketAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.db.MarketTzDB;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.MyPopMenu;
import com.pw.schoolknow.widgets.MyPopMenu.MyPopMenuImp;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.MyTopPopMenu;
import com.pw.schoolknow.widgets.MyTopPopMenu.MyTopPopMenuImp;
import com.pw.schoolknow.widgets.XListView.IXListViewListener;
import com.pw.schoolknow.widgets.XListView;

public class Market extends BaseActivity implements OnItemClickListener {
	
	public Context mcontext;
	
	@ViewInject(R.id.market_tz_lv)
	private XListView lv;
	
	private MyPopMenu popmenu;
	private MyTopPopMenu mtpop;
	
	private MyProgressBar mpb;
	
	private List<Map<String,Object>> list=null;  
	
	public MarketAdapter adapter;
	public HttpUtils http;
	
	@ViewInject(R.id.market_tz_empty)
	public LinearLayout empty_Layout;
	
	
	//当前选择的商品类型,默认全部商品
	public static int type=0;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_market);
		setTitle("全部商品",R.drawable.titlebar_arrow_down);
		setTitleBar(R.drawable.btn_titlebar_back,R.drawable.btn_titlebar_select);
		mcontext=this;
		ViewUtils.inject(this);
		
		this.list=new ArrayList<Map<String,Object>>();
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载中...");
		
		lv.setOnItemClickListener(this);
		lv.setPullLoadEnable(true);
		lv.setXListViewListener(new IXListViewListenerImp());
		
		adapter=new MarketAdapter(this,list);
		lv.setAdapter(adapter);
		
		loadList(type,0);
	}
	
	
	/**
	 * XListView上拉下拉事件处理
	 * @author peng
	 *
	 */
	class IXListViewListenerImp implements IXListViewListener{
		public void onRefresh() {
			if(!NetHelper.isNetConnected(mcontext)){
				T.showShort(mcontext,R.string.net_error_tip);
				lv.stopRefresh();
				lv.stopLoadMore();
				return;
			}
			loadList(type,0);
		}

		@Override
		public void onLoadMore() {
			if(!NetHelper.isNetConnected(mcontext)){
				T.showShort(mcontext,R.string.net_error_tip);
				lv.stopRefresh();
				lv.stopLoadMore();
				return;
			}
			if(list.size()>0){
				int startNum=Integer.parseInt(list.get(list.size()-1).get("id").toString()) ;
				loadList(type,startNum);
			}
			
		}
		
	}
	

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			popmenu=new MyPopMenu(mcontext);
			popmenu.addItems(new String[]{"发布商品","我的商品","求购专区","分类信息"});
			popmenu.showAsDropDown(v);
			popmenu.setOnItemClickListener(new MyPopMenuImp() {
				public void onItemClick(int index) {
					switch(index){
					case 0:
						startActivity(new Intent(mcontext,MarketPublish.class));
						break;
					case 1:
						break;
					case 2:
						break;
					case 3:
						startActivity(new Intent(mcontext,MarketSort.class));
						break;
					default:
						break;
					}
				}
			});
			break;
		case 3:
			List<String> sortInfo= new ArrayList<String>(Arrays.asList(MarketSort.sortInfo));
			sortInfo.add(0, "今日上架");
			sortInfo.add(0, "全部商品");
			mtpop=new MyTopPopMenu(mcontext,sortInfo);
			mtpop.showAsDropDown(v);
			mtpop.setOnItemClick(new MyTopPopMenuImp() {
				public void onItemClick(int index, String value) {
					setTitle(value,R.drawable.titlebar_arrow_down);
					if(index==0){
						type=0;
					}else if(index==1){
						type=-1;
					}else{
						type=index-1;
					}
					loadList(type,0);
				}
			});
			break;
		default:
			break;
		}
	}
	
	//没有商品的时候
	public void WhenGoodsEmpty(){
		if(list.size()==0){
			empty_Layout.setVisibility(View.VISIBLE);
			lv.setVisibility(View.GONE);
		}else{
			lv.setVisibility(View.VISIBLE);
			empty_Layout.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * 加载商品列表
	 * @param type 商品类型
	 * @param start  商品id(下拉加载更多)
	 */
	public void loadList(int goodType,final int start){
		if(http==null){
			http=new HttpUtils();
		}
		http.send(HttpMethod.GET,ServerConfig.HOST+"/schoolknow/plugin/market/getList.php?type="+goodType
				+"&start="+start,new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {
				list.clear();
				list.addAll(MarketTzDB.getInstance(mcontext).getList());
				adapter.notifyDataSetInvalidated();
				T.showLong(mcontext, "加载出现异常,请确保网络连通后重新尝试");
				mpb.dismiss();
			}
			public void onSuccess(ResponseInfo<String> info) {
				String result=info.result;
				try {
					List<Map<String,Object>> tempList=new JsonHelper(result).
							parseJson(new String[]{"id","time","title","imgurl"});
					if(start==0){
						list.clear();
						list.addAll(tempList);
						 WhenGoodsEmpty();
						adapter.notifyDataSetChanged();
					}else{
						list.addAll(tempList);
						 WhenGoodsEmpty();
						adapter.notifyDataSetInvalidated();
					}
					MarketTzDB.getInstance(mcontext).save(tempList);
				} catch (Exception e) {
				}
				mpb.dismiss();
				lv.stopRefresh();
				lv.stopLoadMore();
			}
		});
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		@SuppressWarnings("unchecked")
		Map<String,Object> map=(Map<String, Object>) lv.getItemAtPosition(position);
		Intent it=new Intent(mcontext,MarketContent.class);
		it.putExtra("id",map.get("id").toString());
		startActivity(it);
	}

}
