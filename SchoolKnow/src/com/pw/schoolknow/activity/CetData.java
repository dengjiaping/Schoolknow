package com.pw.schoolknow.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pw.schoolknow.R;
import com.pw.schoolknow.base.BaseActivity;

public class CetData extends BaseActivity {
	
	private ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_cet_data);
		setTitle("四六级查询");
		setTitleBar(0,0);
		
		String cetDa=getIntent().getStringExtra("cet");
		String[] cetInfo=cetDa.split("\\|");
		String[] param=new String[]{"姓名:","学校:","考试类型:","准考证号:",
				"考试时间:","总分:","听力:","阅读:","综合:","写作:"};
		
		//添加参数
		for(int i=0;i<cetInfo.length;i++){
			cetInfo[i]=param[i]+cetInfo[i];
		}
		
		lv=(ListView) super.findViewById(R.id.cet_data_lv);
		
		lv.setAdapter(new CetDataAdapter(this,cetInfo));
	}

	@Override
	protected void HandleTitleBarEvent(int buttonId, View v) {
		// TODO Auto-generated method stub

	}
	
	
	/**
	 * cet显示效果适配器
	 * @author wei8888go
	 *
	 */
	public class CetDataAdapter extends BaseAdapter{
		
		public String[] list;
		public Context context;
		private LayoutInflater mInflater;
		
		public CetDataAdapter(Context context,String[] list){
			this.context=context;
			this.list=list;
			mInflater = LayoutInflater.from(this.context);
		}

		@Override
		public int getCount() {
			return list.length;
		}

		@Override
		public Object getItem(int position) {
			return list[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null||convertView.getTag(R.drawable.ic_launcher+position) == null){
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.item_cet_data, null);
				holder.text=(TextView) convertView.findViewById(R.id.cet_data_item_text);
				convertView.setTag(R.drawable.ic_launcher + position);
			}else{
				holder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
						+ position);
			}
			holder.text.setText(list[position]);
			if(getCount()==1){				
				convertView.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.radius_listview_default));
			}else if(getCount()>1&&position==0){
				convertView.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.radius_listview_top));
			}else if(getCount()>1&&position==getCount()-1){
				convertView.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.radius_listview_bottom));
			}else{
				convertView.setBackgroundDrawable(
						getResources().getDrawable(R.drawable.radius_listview_center));
			}
			return convertView;
		}
		
	}
	
	public class ViewHolder {
		TextView text;
	}
	

}
