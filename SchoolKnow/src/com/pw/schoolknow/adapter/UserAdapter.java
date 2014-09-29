package com.pw.schoolknow.adapter;

import com.pw.schoolknow.R;
import com.pw.schoolknow.db.ChatMessageDB;
import com.pw.schoolknow.db.InformDB;
import com.pw.schoolknow.helper.LoginHelper;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class UserAdapter extends BaseAdapter {
	
	public Context context;
	private String[] list;
	public LoginHelper lh;
	public int imgRes[]={R.drawable.skin_icon_profile_color_friends,
			R.drawable.skin_icon_profile_color_readingcenter,
			R.drawable.skin_icon_profile_color_friends,
			R.drawable.skin_icon_profile_color_readingcenter,
			R.drawable.skin_icon_profile_color_visitors
	};
	
	public UserAdapter(Context context, String[] list) {
		this.context = context;
		this.list = list;
		lh=new LoginHelper(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.item_user_iv, parent, false);
	    }
		ImageView img = ViewHolder.get(convertView, R.id.user_item_img);
		TextView title=ViewHolder.get(convertView, R.id.user_item_title);
		TextView num=ViewHolder.get(convertView, R.id.user_item_num);
		
		
		if(position==1&&list[position].equals("我的消息")){
			int count=new InformDB(context, lh.getUid()).getNewCount();
			if(count!=0){
				num.setVisibility(View.VISIBLE);
				num.setText(String.valueOf(count));
			}else{
				num.setVisibility(View.GONE);
			}
		}else if(position==3&&list[position].equals("聊天记录")){
			int count=ChatMessageDB.getInstance(context, lh.getUid()).getNOReadNum();
			if(count!=0){
				num.setVisibility(View.VISIBLE);
				num.setText(String.valueOf(count));
			}else{
				num.setVisibility(View.GONE);
			}
		}else{
			num.setVisibility(View.GONE);
		}
		
		
		if(position<imgRes.length){
			img.setImageResource(imgRes[position]);
		}
		title.setText(list[position]);
		return convertView;
	}

	

}
