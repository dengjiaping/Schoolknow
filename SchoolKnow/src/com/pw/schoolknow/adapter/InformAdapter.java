package com.pw.schoolknow.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.base.InformBase;
import com.pw.schoolknow.config.InformConfig;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.utils.TimeUtil;
import com.pw.schoolknow.utils.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class InformAdapter extends BaseAdapter {
	
	public static String[] defaultList={"验证消息","和我相关","评论","赞"};
	
	private List<InformBase> list;
	private LayoutInflater inflater;
	
	public BitmapUtils bitmapUtils;

	public InformAdapter(Context context,List<InformBase> list){
		this.list=list;
		this.inflater = LayoutInflater.from(context);
		bitmapUtils=BitmapHelper.getHeadBitMap(context);
	}
	
	public int getCount() {
		return list.size()+defaultList.length;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position-defaultList.length);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position>=defaultList.length){
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_inform_lv, parent, false);
		    }
			ImageView img = ViewHolder.get(convertView, R.id.inform_item_userimg);
			TextView nick=ViewHolder.get(convertView, R.id.inform_item_username);
			TextView content=ViewHolder.get(convertView, R.id.inform_item_userinfo);
			TextView time=ViewHolder.get(convertView, R.id.inform_item_time);
			
			InformBase item=list.get(position-defaultList.length);
			nick.setText(item.getSendName());
			time.setText(TimeUtil.getCommentTime(Long.parseLong(item.getTime())));
			
			BitmapHelper.setHead(bitmapUtils, img, item.getSendUid());
			
			switch(item.getType()){
			case InformConfig.PUSH_SFComment:
				content.setText("回复了你的花椒社区动态");
				break;
			case InformConfig.PUSH_ADDREQUEST:
				content.setText("请求添加你为好友");
				break;
			default:
				content.setText("");
				break;
			}
		}else{
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_list_img_text_1, parent, false);
		    }
			TextView TextTitle=ViewHolder.get(convertView, R.id.list_img_text_1_txt);
			ImageView imageIcon=ViewHolder.get(convertView, R.id.list_img_text_1_icon);
			switch(position){
			case 0:
				TextTitle.setText(defaultList[0]);
				imageIcon.setImageResource(R.drawable.messagescenter_messagebox);
				break;
			case 1:
				TextTitle.setText(defaultList[1]);
				imageIcon.setImageResource(R.drawable.messagescenter_at);
				break;
			case 2:
				TextTitle.setText(defaultList[2]);
				imageIcon.setImageResource(R.drawable.messagescenter_comments);
				break;
			case 3:
				TextTitle.setText(defaultList[3]);
				imageIcon.setImageResource(R.drawable.messagescenter_good);
				break;
			default:
				break;
			}
		}
		return convertView;
	}

}
