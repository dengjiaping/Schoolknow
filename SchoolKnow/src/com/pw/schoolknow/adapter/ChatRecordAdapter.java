package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ChatRecordAdapter extends BaseAdapter {
	
	public Context context;
	private List<Map<String,Object>> list;
	public BitmapUtils bitmapUtils;
	
	public ChatRecordAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		bitmapUtils=BitmapHelper.getHeadBitMap(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.item_inform_lv, parent, false);
	    }
		
		Map<String,Object> map=list.get(position);
		
		ImageView img = ViewHolder.get(convertView, R.id.inform_item_userimg);
		TextView nick=ViewHolder.get(convertView, R.id.inform_item_username);
		TextView content=ViewHolder.get(convertView, R.id.inform_item_userinfo);
		TextView time=ViewHolder.get(convertView, R.id.inform_item_time);
		TextView num=ViewHolder.get(convertView, R.id.inform_item_num);
		
		BitmapHelper.setHead(bitmapUtils, img,map.get("uid").toString());
		nick.setText(map.get("nick").toString());
		content.setText("ÁÄÌì¼ÇÂ¼");
		time.setText("11:21");
		
		int numCount=(Integer) map.get("num");
		num.setVisibility(View.VISIBLE);
		if(numCount>99){
			num.setBackgroundResource(R.drawable.bg_tabbar_indicator3);
			num.setText("99+");
		}else if(numCount>0){
			if(numCount>=10){
				num.setBackgroundResource(R.drawable.bg_tabbar_indicator2);
			}
			num.setText(numCount+"");
		}else{
			num.setVisibility(View.GONE);
		}

		
		return convertView;
	}

	

}
