package com.pw.schoolknow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.InputHelper;
import com.pw.schoolknow.utils.EncodeUtil;
import com.pw.schoolknow.utils.T;
import com.pw.schoolknow.utils.TextUtils;
import com.pw.schoolknow.widgets.MyProgressBar;
import com.pw.schoolknow.widgets.StuidSelect;
import com.pw.schoolknow.widgets.StuidSelect.StuidSelectInterface;

public class BukaoInput extends BaseActivity {
	
	@ViewInject(R.id.who_edit_id)
	private EditText et;
	
	@ViewInject(R.id.who_search_btn)
	private Button btn;
	
	public String val;
	
	public Context mcontext;
	public MyProgressBar mpb;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_who);
		setTitle("补考查询");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		ViewUtils.inject(this);
		
		mcontext=this;
		
	}
	
	 @OnClick({ R.id.who_search_btn }) 
	 public void clickMethod(View v) { 
		 InputHelper.Hide(BukaoInput.this);
		 val=et.getText().toString().trim();
		 if(val.length()==14&&TextUtils.isAllNum(val)){
			 	getBkInfo(val);
			}else if(TextUtils.isAllNum(val)&&val.length()!=14){
				T.showShort(mcontext, "输入的学号有误");
			}else if(val.length()<2||val.length()>4){
				T.showShort(mcontext, "姓名限制2-4个字");
			}else if(val.length()<2||val.length()>4){
				T.showShort(mcontext, "姓名限制2-4个字");
			}else if(TextUtils.contain(val,"%")||TextUtils.contain(val,"_")){
				T.showLong(mcontext, "同学使坏是不对的哟(*^__^*)");
			}else{
				mpb=new MyProgressBar(this);
				mpb.setMessage("正在加载中...");
				String stuName=EncodeUtil.ToUtf8(val);
				StuidSelect stuidSelect=new StuidSelect(mcontext);
				stuidSelect.execute(stuName);
				stuidSelect.getSelected(new StuidSelectInterface() {
					public void onSelect(String stuid) {
						getBkInfo(stuid);
					}
				});
			}
	 }
	 
	 /**
	  * 根据学号获取补考信息
	  * @param stuid
	  * @return
	  */
	 public void getBkInfo(String stuid){
		  HttpUtils http = new HttpUtils();  
		  http.configCurrentHttpCacheExpiry(1000 * 10);  
		  http.send(HttpMethod.GET,ServerConfig.HOST+"/schoolknow/api/bukao.php?stuid="+stuid,
				  null,new RequestCallBack<String>() {
						public void onFailure(HttpException arg0, String arg1) {
							mpb.dismiss();
							T.showLong(mcontext, "加载失败,请确保网络连接正常后重新尝试");
						}
						public void onSuccess(ResponseInfo<String> info) {
							mpb.dismiss();
							String result=info.result;
							Intent it=new Intent(mcontext,Bukao.class);
							it.putExtra("data", result);
							startActivity(it);
						}
					});
	 }

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		default:
			break;
		}
	}

}
