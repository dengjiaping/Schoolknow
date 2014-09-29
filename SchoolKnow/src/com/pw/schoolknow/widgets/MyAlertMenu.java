package com.pw.schoolknow.widgets;



import com.pw.schoolknow.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



public class MyAlertMenu implements OnItemClickListener{
	private Dialog mDialog;
	private ListView listview=null;
	private String params[]=null;
	private LinearLayout layout;
	
	public interface MyDialogMenuInt {
		public void onItemClick(int position);
	}
	private MyDialogMenuInt listener;
	
	/**
	 * 
	 * @param context
	 * @param params 菜单的值
	 */
	public MyAlertMenu(Context context,String params[]){
		mDialog = new AlertDialog.Builder(context).create();
		mDialog.show();
		this.listview=new ListView(context);
		this.listview.setBackgroundDrawable(context.getResources().
				getDrawable(R.drawable.qz_bg_container_cell_normal));
		//this.listview.setDividerHeight(1);
		//this.listview.setDivider(new ColorDrawable(0x93A1AC));
		this.params=params;
		layout=new LinearLayout(context);
		
		listview.setAdapter(new MyAlertMenuAdapter(context,this.params));
		listview.setOnItemClickListener(this);
		layout.addView(listview);
		layout.setPadding(30,10,30,10);
		listview.setPadding(10,10,10,10);
		
		mDialog.setContentView(layout);
	}
	
	
	
	
	
	public void dismiss(){
		mDialog.dismiss();
	}





	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
		if (listener != null) {
			listener.onItemClick(position);
		}
		mDialog.dismiss();		
	}
	
	public void setOnItemClickListener(MyDialogMenuInt listener) {
		 this.listener = listener;
	}

}


//适配器类
class MyAlertMenuAdapter extends BaseAdapter{
	
	private Context context;
	private String val[]=null;
	
	public MyAlertMenuAdapter(Context context,String val[]){
		this.context=context;
		this.val=val;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return val.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return val[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View arg1, ViewGroup arg2) {
		TextView tv=new TextView(context);
		tv.setText(val[position]);
		tv.setTextSize(18);
		tv.setTextColor(Color.rgb(0, 0, 0));
		tv.setPadding(0, 20,0,20);
		
		//长度设置为1000不是最好的解决办法
		tv.setWidth(1000);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		return tv;
	}
	
}

