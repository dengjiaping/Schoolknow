package com.pw.schoolknow.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.config.ServerConfig;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.helper.NetHelper;
import com.pw.schoolknow.utils.GetUtil;
import com.pw.schoolknow.utils.T;

/**
 * 绑定解除绑定学号
 * @author wei8888go
 *
 */
public class BindStuid extends BaseActivity {
	
	private EditText et;
	private LoginHelper lh;
	
	private boolean hasBind;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_update_username);
		setTitle("绑定学号");
		
		
		lh=new LoginHelper(this);
		
		et=(EditText) super.findViewById(R.id.update_username_edit);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		et.setHint("请输入学号");
		String uid=lh.getStuId().trim();
		if(uid.length()==14){
			hasBind=true;
			et.setText(uid);
			et.setFocusable(false);
			setTitleBar(R.drawable.btn_titlebar_back,"",0,"解除");
		}else{
			hasBind=false;
			et.setFocusable(true);
			setTitleBar(R.drawable.btn_titlebar_back,"",0,"绑定");
		}
		
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		switch(buttonId){
		case 1:
			finish();
			break;
		case 2:
			if(!hasBind){
				String val=et.getText().toString().trim();
				if(val.length()!=14){
					T.showShort(BindStuid.this, "学号输入错误");	
				}else{
					if(!NetHelper.isNetConnected(this)){
						T.showShort(this,R.string.net_error_tip);
					}else{
						hasBind=true;
						lh.setStuId(val);
						T.showShort(BindStuid.this, "成功绑定");	
						onCreate(null);
						//new AsyncUpdate().execute(v);
					}
				}
			}else{
				hasBind=false;
				lh.setStuId("");
				T.showShort(BindStuid.this, "成功解除绑定");	
				onCreate(null);
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
				T.showShort(BindStuid.this, "修改成功");
				finish();
			}else{
				v.setEnabled(true);
				T.showShort(BindStuid.this, "修改失败,请重新尝试");
			}
		}

		
		
	}

}
