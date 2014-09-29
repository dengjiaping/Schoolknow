package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.base.IndexItemBase;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.DownloadManageUtil;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.widgets.EmotionBox;
import com.pw.schoolknow.widgets.MyAlertDialog;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.EmotionBox.EmotionBoxClickListener;
import com.pw.schoolknow.widgets.MyAlertDialog.MyDialogInt;

public class NewsContent extends BaseActivity {
	
	private WebSettings webSettings=null;
	private WebView web=null;
	private MyProgressBar mpb;
	
	private EmotionBox emotionBox;
	
	private IndexItemBase item;
	
	private MyAlertDialog mad;
	
	private LoginHelper loginHelper;
	
	@ViewInject(R.id.part_sorry_tip_layout)
	private RelativeLayout clickReLoad;
	
	public Context mcontext;
	
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.act_news_content);
		setTitle("最新动态");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"评论");
		
		ViewUtils.inject(this);
		mcontext=this;
		
		loginHelper=new LoginHelper(this);
		
		IndexItemBase temmItem=(IndexItemBase) getIntent().getSerializableExtra(Index.SER_KEY);
		
		if(temmItem!=null&&item==null){
			item=temmItem;
		}
		
		web=(WebView) super.findViewById(R.id.news_content_webview);
		
		//点击图片重新加载
		clickReLoad.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(!NetHelper.isNetConnected(mcontext)){
					T.showShort(mcontext,R.string.net_error_tip);
				}else{
					onCreate(null);
				}
				
			}
		});
		
		//网络故障不加载
		if(!NetHelper.isNetConnected(this)){
			web.setVisibility(View.GONE);
			clickReLoad.setVisibility(View.VISIBLE);
			return;
		}else{
			web.setVisibility(View.VISIBLE);
			clickReLoad.setVisibility(View.GONE);
		}
		
		
		webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setUseWideViewPort(false); 
        webSettings.setLoadWithOverviewMode(true); 
        //web.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //web.setHorizontalScrollBarEnabled(false);
        web.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		web.loadUrl(ServerConfig.HOST+"/schoolknow/news.php?id="+item.getId());
		
		web.addJavascriptInterface(new JavascriptInterface(this), "imagelistner");  
		web.setDownloadListener(new MyWebViewDownLoadListener()); 
		web.setWebViewClient(new MyWebViewClient()); 
		
		emotionBox=new EmotionBox(NewsContent.this);
		emotionBox.setEditHint("我来说两句");
		emotionBox.setOnClick(new EmotionBoxClickListener() {
			@Override
			public void onClick(String EditTextVal, View view) {
				if(loginHelper.hasLogin()){
					if(EditTextVal.length()>128){
						T.showShort(NewsContent.this,"评论长度不能大于128个字符");
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
					mad=new MyAlertDialog(NewsContent.this);
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
		web.setOnTouchListener(new OnTouchListener() {
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
					T.showShort(NewsContent.this,"评论成功");
					emotionBox.SetValue("");
					emotionBox.setClickable(true);
				}else{
					T.showShort(NewsContent.this,"评论失败,请确定网络正常后再次尝试");
				}
			}
		}
		
	};
	
	// 注入js函数监听  
    private void addImageClickListner() {  
        // 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去  
    	web.loadUrl("javascript:(function(){" +  
        "var objs = document.getElementsByTagName(\"img\"); " +   
                "for(var i=0;i<objs.length;i++)  " +   
        "{"  
                + "    objs[i].onclick=function()  " +   
        "    {  "   
                + "        window.imagelistner.openImage(this.src);  " +   
        "    }  " +   
        "}" +   
        "})()");  
    }  
  
    // js通信接口  
    public class JavascriptInterface {  
  
        private Context context;  
  
        public JavascriptInterface(Context context) {  
            this.context = context;  
        }   
        public void openImage(String img) {  
            Intent intent = new Intent();  
            intent.putExtra("imgsrc", img);  
            intent.setClass(context, ImgPreview.class);  
            context.startActivity(intent);  
        }  
    }  
  
    // 监听  
    @SuppressLint("SetJavaScriptEnabled")
	private class MyWebViewClient extends WebViewClient {  
        @Override  
        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
            return super.shouldOverrideUrlLoading(view, url);  
        }  
  
        @Override  
        public void onPageFinished(WebView view, String url) {  
        	web.setVisibility(View.VISIBLE);
        	mpb.dismiss();
            view.getSettings().setJavaScriptEnabled(true);   
            super.onPageFinished(view, url);  
            // html加载完成之后，添加监听图片的点击js函数  
            addImageClickListner();  
  
        }  
  
        @Override  
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
        	web.setVisibility(View.GONE);
        	mpb=new MyProgressBar(NewsContent.this);
        	mpb.setMessage("正在加载中...");
            view.getSettings().setJavaScriptEnabled(true);   
            super.onPageStarted(view, url, favicon);  
        }  
  
        @Override  
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {  
  
            super.onReceivedError(view, errorCode, description, failingUrl);  
  
        }  
    }
    
    /**
     * 调用系统浏览器进行下载
     * @author wei8888go
     *
     */
    private class MyWebViewDownLoadListener implements DownloadListener{
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
        	//调用其他浏览器下载
        	/*
	            Uri uri = Uri.parse(url);
	            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
	            startActivity(intent);      
             */
        	
        	//调用系统下载
        	DownloadManageUtil.DownloadFile(mcontext, url,"/schoolknow/DownLoad");
        }
    }
    
    @Override
	protected void onDestroy() {
		super.onDestroy();
		//DownloadUtil.unregisterReceiver(this);
	}


	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			Intent it=new Intent(NewsContent.this,NewsComment.class);
			Bundle mBundle = new Bundle();  
		    mBundle.putSerializable(Index.SER_KEY,item);  
		    it.putExtras(mBundle); 
			startActivity(it);
			break;
		default:
			break;
		}
	}

}
