package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.bean.SudiBean;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.utils.TimeUtil;

public class SudiContent extends BaseActivity {
	
	@ViewInject(R.id.sudi_content_lv)
	private ListView lv;
	private List<Map<String,Object>> list;
	private SimpleAdapter adapter;
	
	private TextView tv_name,tv_book,tv_state;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudi_content_act);
		setTitle("查询结果");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		ViewUtils.inject(this);
		
		Intent it=getIntent();
		String sudiCode=it.getStringExtra("sudiCode");
		String sudiName=it.getStringExtra("sudiName");
		String bookCode=it.getStringExtra("bookCode");
		
		LinearLayout head=(LinearLayout) LayoutInflater.from(this)
		          .inflate(R.layout.sudi_content_head,null);
		tv_name=(TextView) head.findViewById(R.id.sudi_content_head_name);
		tv_book=(TextView) head.findViewById(R.id.sudi_content_head_bookid);
		tv_state=(TextView) head.findViewById(R.id.sudi_content_head_state);
		tv_name.setText("物流信息:"+sudiName);
		tv_book.setText("订单号:"+bookCode);
		tv_state.setText("物流状态:正在查询中...");
		lv.addHeaderView(head);
		final SudiBean bean=new SudiBean(TimeUtil.getCurrentTime(), 
				bookCode, sudiCode, sudiName, "");
		new Thread(new Runnable() {
			public void run() {
				DbUtils db = DbUtils.create(SudiContent.this);
				 try {
					db.save(bean);
				} catch (DbException e1) {
					
				}
			}
		}).start();
		
		
		list=new ArrayList<Map<String,Object>>();
		adapter=new SimpleAdapter(this, list, R.layout.sudi_content_lv_item,
				new String[]{"time","context"}, 
				new int[]{R.id.sudi_content_lv_item_time,R.id.sudi_content_lv_item_context});
		lv.setAdapter(adapter);
		
		String baseUrl="http://m.kuaidi100.com/query?type="+sudiCode+"&postid="+bookCode
		+"&id=1&valicode=&temp=1";
		HttpUtils http=new HttpUtils();
		http.send(HttpMethod.GET, baseUrl,null,new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {
				tv_state.setText("物流状态:查询出现异常");
			}
			public void onSuccess(ResponseInfo<String> info) {
				String jsonInfo=info.result;
				try{
					JSONObject JsonData = new JSONObject(jsonInfo);
					String sudiInfo=JsonData.getString("data");
					String sudiState=JsonData.getString("state");
//					String sudiStatus=JsonData.getString("state");
//					if(sudiStatus=="0"){
//						tv_state.setText("物流状态:物流单暂无结果");
//					}else if(sudiStatus=="1"){
//						
//					}else if(sudiStatus=="2"){
//						tv_state.setText("物流状态:接口出现异常..");
//					}
					switch(Integer.parseInt(sudiState)){
					case 0:
						sudiState="运输途中";
						break;
					case 1:
						sudiState="正在揽件中";
						break;
					case 2:
						sudiState="存在疑难问题";
						break;
					case 3:
						sudiState="已经签收";
						break;
					case 4:
						sudiState="已经退签";
						break;
					case 5:
						sudiState="正在派件中";
						break;
					case 6:
						sudiState="退回";
						break;
					default:
						break;
					}
					tv_state.setText("物流状态:"+sudiState);
					list.addAll(new JsonHelper(sudiInfo).parseJson(new String[]{"time","context"}));
					adapter.notifyDataSetChanged();
				}catch(Exception e){
					tv_state.setText("物流状态:查询出现异常");
				}
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
