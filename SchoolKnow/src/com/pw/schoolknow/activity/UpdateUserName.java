package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;

public class UpdateUserName extends BaseActivity {
	
	private EditText et;
	private LoginHelper lh;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_update_username);
		setTitle("昵称");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"确定");
		
		lh=new LoginHelper(this);
		
		et=(EditText) super.findViewById(R.id.update_username_edit);
		et.setText(lh.getNickname());
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			String val=et.getText().toString().trim();
			if(val.length()<2||val.length()>10){
				T.showShort(UpdateUserName.this, "昵称限制为2-10字符");	
			}else{
				if(!NetHelper.isNetConnected(this)){
					T.showShort(this,R.string.net_error_tip);
				}else if(val.equals(lh.getNickname())){
					finish();
				}else{
					v.setEnabled(false);
					new AsyncUpdate().execute(v);
				}
			}
			break;
		}
	}
	
	public class AsyncUpdate extends AsyncTask<View,Void,String>{

		private View v;
		protected String doInBackground(View... v) {
			this.v=v[0];
			List<NameValuePair> params=new ArrayList<NameValuePair>(); 
			params.add(new BasicNameValuePair("uid",lh.getUid()));
			params.add(new BasicNameValuePair("token",lh.getToken()));
			params.add(new BasicNameValuePair("nick",et.getText().toString()));
			return GetUtil.sendPost(ServerConfig.HOST+"/schoolknow/manage/updateInfo.php", params);
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			
			if(result.equals("success")){
				lh.setNickname(et.getText().toString());
				T.showShort(UpdateUserName.this, "修改成功");
				finish();
			}else{
				v.setEnabled(true);
				T.showShort(UpdateUserName.this, "修改失败,请重新尝试");
			}
		}

		
		
	}

}
