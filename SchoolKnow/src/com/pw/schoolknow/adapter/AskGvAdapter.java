package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.pw.schoolknow.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AskGvAdapter extends BaseAdapter {
	
	private Context context;
	private List<Map<String,Object>> list;
	private LayoutInflater inflater;
	
	public AskGvAdapter(Context context, List<Map<String, Object>> list) {
		this.context = context;
		this.list = list;
		this.inflater = LayoutInflater.from(this.context);
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
		ViewHolder holder = null;
		if (convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_ask_img_gv, null, false);
			holder.img=(ImageView) convertView.findViewById(R.id.ask_img_gv_pic);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Map<String,Object> map=list.get(position);
		Bitmap bm=(Bitmap) map.get("bm");
		holder.img.setImageBitmap(bm);
		
		return convertView;
	}
	
	static class ViewHolder{
		ImageView img;
	}

}
