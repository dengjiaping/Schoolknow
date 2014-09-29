package com.pw.schoolknow.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;
import com.pw.schoolknow.helper.SudiHelper;

public class SudiList extends BaseActivity {
	
	@ViewInject(R.id.sudi_list_gv)
	private GridView gv;
	
	@ViewInject(R.id.sudi_list_lv)
	private ListView lv;
	
	private ArrayAdapter<String> sudiAdapter;
	private ArrayAdapter<String> letterAdapter;
	public final static String[] letterArray={
		"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
		"O","P","Q","R","S","T","U","V","W","X","Y","Z"
	};
	
	public int currentLetter=0;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sudi_list);
		setTitle("选择快递公司");
		setTitleBar(R.drawable.btn_titlebar_back,0);
		
		ViewUtils.inject(this);
		
		sudiAdapter=new ArrayAdapter<String>(this, R.layout.sudi_list_lv_item,
				SudiHelper.sudiName[0]);
		lv.setAdapter(sudiAdapter);
		
		letterAdapter=new ArrayAdapter<String>(this, 
				R.layout.sudi_list_gv_item,letterArray);
		gv.setAdapter(letterAdapter);
	}
	
	@OnItemClick({R.id.sudi_list_lv,R.id.sudi_list_gv})  
	public void itemClick(AdapterView<?> parent, View view, int position,long id) {  
         if(parent==gv){
        	 this.currentLetter=position;
        	 sudiAdapter=new ArrayAdapter<String>(this, R.layout.sudi_list_lv_item,
     				SudiHelper.sudiName[position]);
     		lv.setAdapter(sudiAdapter);
         }else if(parent==lv){
        	 Intent intent = new Intent();
 			 Bundle bundle = new Bundle();
 			 bundle.putIntArray("array",new int[]{
 					this.currentLetter,position});
 			 intent.putExtras(bundle);
 			 setResult(RESULT_OK, intent);
 			 finish();
        	 //T.showShort(this, SudiHelper.sudiName[this.currentLetter][position]);
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
		case 3:
			break;
		default:
			break;
		}
	}

}
