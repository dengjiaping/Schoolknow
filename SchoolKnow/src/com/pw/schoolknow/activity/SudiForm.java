package com.pw.schoolknow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.SudiHelper;
import com.pw.schoolknow.utils.T;

public class SudiForm extends BaseActivity {
	
	@ViewInject(R.id.sudi_form_tv)
	private TextView tv;
	@ViewInject(R.id.sudi_form_et)
	private EditText et;
	@ViewInject(R.id.sudi_form_btn)
	private Button btn;
	
	private String sudiCode="";
	private String sudiName="";
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudi_form);
		setTitle("快递查询");
		setTitleBar(R.drawable.btn_titlebar_back,"",0,"记录");
		ViewUtils.inject(this);
	}
	
	@OnClick({R.id.sudi_form_tv,R.id.sudi_form_btn})
	public void onClick(View v){
		if(v==tv){
			Intent it=new Intent(this,SudiList.class);
			startActivityForResult(it, 2);
		}else if(v==btn){
			if(tv.getText().equals(getString(R.string.select_sudi_company))){
				T.showShort(this, "请先选择快递公司");
			}else if(et.getText().toString().trim().length()<5){
				T.showShort(this, "请输入快递订单号");
			}else{
				Intent it=new Intent(this,SudiContent.class);
				it.putExtra("sudiCode", sudiCode);
				it.putExtra("sudiName", sudiName);
				it.putExtra("bookCode",et.getText().toString().trim());
				startActivity(it);
			}
			
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 2){
			if (resultCode == RESULT_OK){
				if(data!=null){
					Bundle bundle = data.getExtras();
					int[] sudiInfo=bundle.getIntArray("array");
					if(sudiInfo.length==2){
						sudiCode=SudiHelper.sudiType[sudiInfo[0]][sudiInfo[1]];
						sudiName=SudiHelper.sudiName[sudiInfo[0]][sudiInfo[1]];
						tv.setText(sudiName);
					}
					
				}
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
			Intent it=new Intent(this,SudiHistory.class);
			startActivity(it);
			break;
		case 3:
			break;
		default:
			break;
		}
	}

}
