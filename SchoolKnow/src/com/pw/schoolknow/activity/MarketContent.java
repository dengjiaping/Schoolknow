package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.BufferType;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.Comment2Adapter;
import com.pw.schoolknow.adapter.SFImgShowAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.JsonHelper;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.RegUtils;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.EmotionBox;
import com.pw.schoolknow.widgets.EmotionBox.EmotionBoxClickListener;
import com.pw.schoolknow.widgets.MyDialogMenu;
import com.pw.schoolknow.widgets.MyDialogMenu.OnItemClickCallBack;
import com.pw.schoolknow.widgets.MyPopMenu;
import com.pw.schoolknow.widgets.XListView.IXListViewListener;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.NoScrollGridView;
import com.pw.schoolknow.widgets.XListView;

public class MarketContent extends BaseActivity {
	
	@ViewInject(R.id.market_content_lv)
	private XListView lv;
	
	public Context mcontext;
	public String id;
	public MyProgressBar mpb;
	private List<Map<String,Object>>  list;
	private Comment2Adapter adapter;
	
	public EmotionBox box;
	public String uid="";
	public LoginHelper lh;
	
	public int targetId=0;
	public MyDialogMenu mdm;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_market_content);
		setTitle("商品详情");
		setTitleBar(R.drawable.btn_titlebar_back,R.drawable.navigationbar_profile_edit);
		mcontext=this;
		ViewUtils.inject(this);
		lh=new LoginHelper(mcontext);
		
		mpb=new MyProgressBar(this);
		mpb.setMessage("正在加载信息...");
		list=new ArrayList<Map<String,Object>>();
		lv.setPullRefreshEnable(false);
		lv.setPullLoadEnable(true);
		lv.setXListViewListener(new IXListViewListenerImp());
		lv.setOnItemClickListener(new onItemClick());
		
		adapter=new Comment2Adapter(this, list);
		
		id=getIntent().getStringExtra("id");
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET,ServerConfig.HOST+"/schoolknow/plugin/market/getContent.php?id="+id,
				new RequestCallBack<String>() {
			public void onFailure(HttpException arg0, String arg1) {
				
			}
			public void onSuccess(ResponseInfo<String> info) {
				final String result=info.result;
				View layout=LayoutInflater.from(mcontext)
				          .inflate(R.layout.part_market_content_head, null);
				TextView headTitle=(TextView) layout.findViewById(R.id.market_content_head_title);
				TextView headPrice=(TextView) layout.findViewById(R.id.market_content_head_price);
				TextView headCont=(TextView) layout.findViewById(R.id.market_content_head_content);
				TextView headPhone=(TextView) layout.findViewById(R.id.market_content_head_phone);
				NoScrollGridView headGv=(NoScrollGridView) layout.findViewById(R.id.market_content_head_gv);
				try {
					JSONObject JsonData = new JSONObject(result);
					String headContent=JsonData.getString("content");
					headTitle.setText(JsonData.getString("title"));
					headCont.setText(EmotionBox.convertNormalStringToSpannableString(mcontext,
							 RegUtils.replaceImage(headContent)),BufferType.SPANNABLE);
					headPhone.setText("联系电话:"+JsonData.getString("phone")); 
					headPrice.setText("价格:￥"+JsonData.getString("price"));
					uid=JsonData.getString("uid");
					//获取发布内容中的图片地址
					final List<String> marketImgList=RegUtils.
						getMarketImgFromString(headContent,JsonData.getString("uid"));
					 
					//九宫格图片显示
					headGv.setVisibility(View.VISIBLE);
					SFImgShowAdapter SAadapter=null;
					headGv.setNumColumns(4);
					SAadapter=new SFImgShowAdapter(mcontext,marketImgList,headGv,R.layout.item_img_gv);
					headGv.setAdapter(SAadapter);
					 mpb.dismiss();
					 
					 //浏览图片
					 headGv.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> parent, View view, int position,
								long id) {
							Intent it=new Intent(mcontext,PhotoView.class);
							it.putStringArrayListExtra("data",(ArrayList<String>) marketImgList);
							it.putExtra("position", position);
							mcontext.startActivity(it);
						}
					});
					 lv.addHeaderView(layout);
					 lv.setAdapter(adapter);
				} catch (Exception e1) {
					T.showShort(mcontext,e1.toString());
				}
			}
		});	
		
		getMoreList(id,"0");
		
		lv.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				box.HideEmotionBox();
				return false;
			}
		});
		//发布评论
		box=new EmotionBox(this);
		box.setOnClick(new EmotionBoxClickListener() {
			public void onClick(String EditTextVal, View view) {
				String commentVal=EditTextVal.trim();
				if(!lh.hasLogin()){
					T.showShort(mcontext, "请登录后再发布评论");
				}else if(commentVal.length()<5){
					T.showShort(mcontext, "评论最少5个字哟");
				}else if(commentVal.length()>125){
					T.showShort(mcontext, "评论字数不能超过125字%>_<%");
				}else if(!NetHelper.isNetConnected(mcontext)){
					T.showShort(mcontext,R.string.net_error_tip);
					return;
				}else{
					view.setClickable(false);
					HttpUtils http=new HttpUtils();
					RequestParams params = new RequestParams();
					params.addBodyParameter("uid", lh.getUid());
					params.addBodyParameter("token",lh.getToken());
					params.addBodyParameter("content",EditTextVal.trim());
					params.addBodyParameter("goodid",id);
					params.addBodyParameter("target",String.valueOf(targetId));
					http.send(HttpMethod.POST,ServerConfig.HOST+"/schoolknow/plugin/market/insertComment.php",
							params,new RequestCallBack<String>() {
						public void onFailure(HttpException arg0, String arg1) {
							T.showLong(mcontext, "评论发布失败");
							box.setClickable(true);
						}
						public void onSuccess(ResponseInfo<String> info) {
							if(info.result.equals("success")){
								T.showLong(mcontext, "发布评论成功");
								box.setClickable(true);
								box.SetValue("");
							}else{
								T.showLong(mcontext, "评论发布失败");
								box.setClickable(true);
							}
							
						}
					} );
				}
			}
		});
		
		
	}
	
	/**
	 * 下拉事件接口
	 * @author peng
	 *
	 */
	class IXListViewListenerImp implements IXListViewListener{
		public void onRefresh() {
			if(!NetHelper.isNetConnected(mcontext)){
				T.showShort(mcontext,R.string.net_error_tip);
				lv.stopRefresh();
				return;
			}
		}

		@Override
		public void onLoadMore() {
			if(!NetHelper.isNetConnected(mcontext)){
				T.showShort(mcontext,R.string.net_error_tip);
				lv.stopLoadMore();
				return;
			}
			if(list.size()!=0){
				String start=(String) list.get(list.size()-1).get("id");
				getMoreList(id,start);
			}
			
		}
		
	}
	
	
	/**
	 * 下拉获取更多评论
	 */
	public void getMoreList(String goodid,String start){
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("goodid", goodid);
		params.addBodyParameter("start",start);
		http.send(HttpMethod.POST,ServerConfig.HOST+"/schoolknow/plugin/market/getCommentList.php",params,
				new RequestCallBack<String>() {
					public void onFailure(HttpException arg0, String arg1) {
						T.showShort(mcontext, "加载评论失败");
					}
					public void onSuccess(ResponseInfo<String> info) {
						try {
							List<Map<String,Object>> tempList=new JsonHelper(info.result).parseJson(
									new String[]{"id","uid","nick","content","time"});
							list.addAll(tempList);
							adapter.notifyDataSetInvalidated();
							lv.stopLoadMore();
							
						} catch (Exception e) {
						}
					}
		});
	}
	
	class onItemClick implements OnItemClickListener{
		@SuppressWarnings("unchecked")
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			Map<String,Object> map=(Map<String, Object>) lv.getItemAtPosition(position);
			final String contentVal_=(String) map.get("content");
			final String nickVal=map.get("nick").toString();
			mdm=new MyDialogMenu(mcontext,new String[]{"取消","回复","复制","举报"});
			mdm.setOnItemClick(new OnItemClickCallBack() {
				public void OnItemClick(View view, int position) {
					switch(position){
					case 0:
						mdm.dismiss();
						break;
					case 1:
						mdm.dismiss();
						box.setEditHint("回复:@"+nickVal);
						break;
					case 2:
						ClipboardManager cmb = (ClipboardManager) mcontext
						.getSystemService(Context.CLIPBOARD_SERVICE);
						String contentVal=contentVal_.trim();
						int start=contentVal.indexOf("</at>");
						if(start!=-1){
							start+=5;
							contentVal=contentVal.substring(start);
						}
						cmb.setText(contentVal);
						T.showShort(mcontext,"已经复制到粘贴板");
						mdm.dismiss();
						break;
					case 3:
						mdm.dismiss();
						break;
					default:
						mdm.dismiss();
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
			finish();
			break;
		case 2:
			if(uid.trim().length()!=0){
				Intent it=new Intent(mcontext,FriendZone.class);
				it.putExtra("uid", uid.trim());
				startActivity(it);
			}
			break;
		case 3:
			break;
		default:
			break;
		}
	}

}
