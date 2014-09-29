package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.CommentAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.IndexItemBase;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.EmotionBox;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.XListView;
import com.pw.schoolknow.widgets.EmotionBox.EmotionBoxClickListener;
import com.pw.schoolknow.widgets.XListView.IXListViewListener;

public class NewsComment extends BaseActivity {
	
	private XListView lv;
	private List<Map<String,Object>> list;
	
	private EmotionBox emotionBox;
	
	private IndexItemBase item;
	
	private MyProgressBar mpb;
	
	private TextView sepBar;
	
	private CommentAdapter adapter;
	
	private int num=0;
	
	private MyAlertDialog mad;
	
	private LoginHelper loginHelper;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_comment);
		setTitle("新闻评论");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		loginHelper=new LoginHelper(this);
		
		item=(IndexItemBase) getIntent().getSerializableExtra(Index.SER_KEY);
		
		this.Init();
		
		emotionBox=new EmotionBox(NewsComment.this);
		emotionBox.setEditHint("我来说两句");
		emotionBox.setOnClick(new EmotionBoxClickListener() {
			@Override
			public void onClick(String EditTextVal, View view) {
				if(loginHelper.hasLogin()){
					if(EditTextVal.length()>128){
						T.showShort(NewsComment.this,"评论长度不能大于128个字符");
						return;
					}
					view.setClickable(false);
					final List<NameValuePair> params=new ArrayList<NameValuePair>(); 
			        params.add(new BasicNameValuePair("newsid",item.getId()+"")); 
			        params.add(new BasicNameValuePair("uid",loginHelper.getUid()));
			        //params.add(new BasicNameValuePair("time",Long.toString(System.currentTimeMillis()))); 
			        params.add(new BasicNameValuePair("comment",EditTextVal.trim())); 
					new Thread(new Runnable() {
						@Override
						public void run() {
							Message msg=new Message();
							msg.what=102;
							msg.obj=GetUtil.sendPost(ServerConfig.HOST+"/schoolknow/newsCommentManage.php",params);
							handler.sendMessage(msg);
						}
					}).start();
				}else{
					mad=new MyAlertDialog(NewsComment.this);
					mad.setTitle("消息提示");
					mad.setMessage("您还没有登录不能发布评论，是否前往登录页面?");
					mad.setLeftButton("确定",new MyDialogInt() {
						@Override
						public void onClick(View view) {
							loginHelper.ToLogin();
						}
					});
					mad.setRightButton("取消",new MyDialogInt() {
						@Override
						public void onClick(View view) {
							mad.dismiss();
						}
					});
				}
			}
		});
		
		lv.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				emotionBox.HideEmotionBox();
				return false;
			}
		});
		
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==102){
				String val=msg.obj.toString();
				if(val.trim().equals("ok")){
					T.showShort(NewsComment.this,"评论成功");
					emotionBox.SetValue("");
					emotionBox.setClickable(true);
					new IXListViewListenerImp().onRefresh();
				}else{
					T.showShort(NewsComment.this,"评论失败,请确定网络正常后再次尝试");
				}
			}
		}
		
	};
	
	public void Init(){
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在载入数据..");
		
		lv= (XListView) super.findViewById(R.id.comment_lv);
		lv.setPullLoadEnable(true);
		list=new ArrayList<Map<String,Object>>();
		
		LayoutInflater inflater=LayoutInflater.from(NewsComment.this);
		View v=inflater.inflate(R.layout.part_news_comment_head, null);
		TextView title=(TextView) v.findViewById(R.id.news_comment_head_title);
		sepBar=(TextView) v.findViewById(R.id.news_comment_head_sep);
		sepBar.setText("所有评论(0条)");
		title.setText(item.getTitle());
		lv.addHeaderView(v);
		
		adapter=new CommentAdapter(this, list);
		lv.setAdapter(adapter);
		
		lv.setXListViewListener( new IXListViewListenerImp());
		new IXListViewListenerImp().onRefresh();
		
	}
	
	class IXListViewListenerImp implements IXListViewListener{
		@Override
		public void onRefresh() {
			if(list.size()!=0){
				num=Integer.parseInt(list.get(0).get("id").toString());
			}
			lv.setRefreshTime(""+TimeUtil.getUpdateTime(System.currentTimeMillis()));
			list.clear();
			new AsyncLoadComment().execute("getNew",String.valueOf(num));
		}
		@Override
		public void onLoadMore() {
			if(list.size()!=0){
				num=Integer.parseInt(list.get(list.size()-1).get("id").toString());
				new AsyncLoadComment().execute("readMore",String.valueOf(num));
			}
		}
		
	}
	
	public class AsyncLoadComment extends AsyncTask<String,Void,String>{
		
		@Override
		protected String doInBackground(String... params) {
			String baseUrl=ServerConfig.HOST+"/schoolknow/newsCommentManage.php?newsid="+item.getId()
					+"&action="+params[0]+"&num="+params[1];
			return  GetUtil.getRes(baseUrl);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			try {
				JSONObject jsonobj=new JSONObject(result);
				int sum=Integer.parseInt(jsonobj.getString("sum"));
				sepBar.setText("所有评论("+sum+"条)");
				String data=jsonobj.getString("data");
				List<Map<String,Object>> JsonList=new JsonHelper(data).parseJson(
						new String[]{"id","uid","cm","tm","nn","he"});
				for(Map<String,Object> JsonMap:JsonList){
					Map<String,Object> map=new HashMap<String, Object>();
					map.put("id",JsonMap.get("id").toString());
					map.put("name",JsonMap.get("nn").toString());
					map.put("great","");
					map.put("time",TimeUtil.getCommentTime(Long.parseLong(JsonMap.get("tm").toString())));
					map.put("content",JsonMap.get("cm").toString());
					map.put("uid",JsonMap.get("uid").toString());
					list.add(map);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			adapter.notifyDataSetChanged();
			mpb.dismiss();
			lv.stopLoadMore();
			lv.stopRefresh();
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			break;
		default:
			break;
		}
	}

}
