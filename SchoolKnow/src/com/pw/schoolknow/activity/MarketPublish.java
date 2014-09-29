package com.pw.schoolknow.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.adapter.MarketPublishAdapter;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.PathConfig;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.FileUtils;
import com.pw.schoolknow.utils.PictureUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.NoScrollGridView;

@SuppressLint("HandlerLeak")
public class MarketPublish extends BaseActivity {
	
	@ViewInject(R.id.market_publish_sort_spinner)  
	private Spinner sort;

	public Context mcontext;
	public MyProgressBar waitLoadPic;
	public String picName;
	public int MarketGoodsType;
	
	@ViewInject(R.id.market_publish_gv)  
	private NoScrollGridView gv;
	private MarketPublishAdapter mpadapter;
	private List<Map<String,Object>> picList;
	
	@ViewInject(R.id.market_publish_submit)
	private Button submit;
	
	@ViewInject(R.id.market_publish_title)
	private EditText title;
	@ViewInject(R.id.market_publish_price)
	private EditText price;
	@ViewInject(R.id.market_publish_phon)
	private EditText phone;
	@ViewInject(R.id.market_publish_content)
	private EditText content;
	
	public MyProgressBar mpb;
	public LoginHelper lh;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_publish_market);
		setTitle("发布商品");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"相机");
		lh=new LoginHelper(this);
		mcontext=this;
		
		ViewUtils.inject(this);
		
		//商品分类
		sort.setPrompt("请选择商品分类");
		ArrayAdapter<String> sortAdapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item,MarketSort.sortInfo);
		sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sort.setAdapter(sortAdapter);
		sort.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				MarketGoodsType=position+1;
			}
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		//略缩图
		picList=new ArrayList<Map<String,Object>>();
		mpadapter=new MarketPublishAdapter(this, picList);
		gv.setAdapter(mpadapter);
		gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				if(position==0){
					Intent it = new Intent(mcontext,
							MultiPicSelect.class);
					it.putExtra("num", 15-picList.size());
					startActivityForResult(it, 2);
				}else{
					picList.remove(position-1);
					mpadapter.notifyDataSetChanged();
				}
			}
		});
		
		submit.setOnClickListener(new OnSubmit());
	}
	
	/**
	 * 提交商品信息
	 * @author peng
	 *
	 */
	public class OnSubmit implements OnClickListener{
		public void onClick(View v) {
			if(v==submit){
				int titleLen=title.getText().toString().trim().length();
				int priceLen=price.getText().toString().trim().length();
				int phoneLen=phone.getText().toString().trim().length();
				int contentLen=content.getText().toString().trim().length();
				if(picList.size()==0){
					T.showShort(mcontext, "无图无真相哦!");
				}else if(titleLen<6||titleLen>30){
					T.showShort(mcontext, "标题长度限制6-30字");
				}else if(priceLen>4){
					T.showShort(mcontext, "商品价格不能大于1W");
				}else if(phoneLen!=0&&phoneLen!=11){
					T.showShort(mcontext, "电话号码为11位哟");
				}else if(contentLen<10){
					T.showShort(mcontext, "对商品进行更详细的描述吧~");
				}else{
					mpb=new MyProgressBar(mcontext);
					mpb.setMessage("正在提交...");
					RequestParams params = new RequestParams();
					
					for(int k=1;k<picList.size()+1;k++){
						File f=new File(picList.get(k-1).get("path").toString());
						if(f.exists()){
							params.addBodyParameter("pictures_"+k,f);
						}
					}
					//上传动态内容
					params.addBodyParameter("type",String.valueOf(MarketGoodsType));
					params.addBodyParameter("num",String.valueOf(picList.size()));
					params.addBodyParameter("title",title.getText().toString());
					params.addBodyParameter("content",content.getText().toString());
					params.addBodyParameter("phone",phone.getText().toString());
					params.addBodyParameter("price",price.getText().toString());
					params.addBodyParameter("uid",lh.getUid());
					params.addBodyParameter("token",lh.getToken());
					
					HttpUtils http = new HttpUtils();
					http.send(HttpMethod.POST, ServerConfig.HOST+"/schoolknow/plugin/market/upload_goods.php",
							params,new RequestCallBack<String>() {
								public void onSuccess(ResponseInfo<String> info) {
									mpb.dismiss();
									picList.clear();
									content.setText("");
									T.showShort(mcontext,"发布成功");
									Intent it=new Intent(mcontext,Market.class);
									startActivity(it);
									finish();
								}

								public void onFailure(HttpException arg0, String arg1) {
									mpb.dismiss();
									T.showShort(mcontext,"发布商品出现错误,请重新尝试+"+arg1);
								}
							});
				}
			}
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 0){
			 String path=PathConfig.SavePATH+picName;
			  File temp = new File(path);
			  if(temp.exists()){
				  Map<String,Object> map=new HashMap<String, Object>();
				  //压缩图片
				  String picAddr=PictureUtil.saveSmallPic(path);
				  map.put("path", picAddr);
			      picList.add(map);
			      mpadapter.notifyDataSetChanged();
			  }else{
				  return;
			  }
		}else if(requestCode == 2){
			 if (resultCode == RESULT_OK) {
					Bundle bundle = data.getExtras();
					@SuppressWarnings("unchecked")
					final ArrayList<String> tDataList = (ArrayList<String>) bundle
							.getSerializable("dataList");
					if (tDataList != null) {
						waitLoadPic=new MyProgressBar(mcontext);
						waitLoadPic.setMessage("正在加载中...");
						new Thread(new Runnable() {
							public void run() {
								for(String str:tDataList){
									 Map<String,Object> map=new HashMap<String, Object>();
									 
									  //压缩图片
									 String picAddr=PictureUtil.saveSmallPic(str);
									 map.put("path", picAddr);
									 picList.add(map);
								}
								Message msg=new Message();
								msg.what=104;
								handler.sendMessage(msg);
							}
						}).start();
						return;
					}
			 }
		}
	}
	
	//选择多张返回时刷新页面
	Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==104){
				mpadapter.notifyDataSetChanged();
				waitLoadPic.dismiss();
			}
			super.handleMessage(msg);
		}
		
	};

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			picName=String.valueOf(TimeUtil.getCurrentTime())+".jpg";
			Intent intent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);
			FileUtils.createPath(PathConfig.SavePATH);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(PathConfig.SavePATH,picName)));
			startActivityForResult(intent, 0);
			break;
		case 3:
			break;
		default:
			break;
		}
	}

}
