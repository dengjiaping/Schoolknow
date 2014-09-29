package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


/**
 * 跳蚤市场发布商品图片适配器 
 * @author peng
 *
 */
public class MarketPublishAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String,Object>> list;
	
	public MarketPublishAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
	        convertView = LayoutInflater.from(context)
	          .inflate(R.layout.item_ask_img_gv, parent, false);
	    }
		ImageView img=(ImageView) convertView.findViewById(R.id.ask_img_gv_pic);
		ImageView del=(ImageView) convertView.findViewById(R.id.ask_img_gv_del);
		if(position==0){
			img.setImageResource(R.drawable.compose_pic_add_more);
			del.setVisibility(View.GONE);
		}else{
			String path=list.get(position-1).get("path").toString();
			Bitmap bm=BitmapFactory.decodeFile(path);
			img.setImageBitmap(bm);
			del.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

}
