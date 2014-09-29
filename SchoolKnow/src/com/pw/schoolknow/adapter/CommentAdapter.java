package com.pw.schoolknow.adapter;

import java.util.List;
import java.util.Map;

import com.lidroid.xutils.BitmapUtils;
import com.pw.schoolknow.R;
import com.pw.schoolknow.activity.FriendZone;
import com.pw.schoolknow.helper.BitmapHelper;
import com.pw.schoolknow.widgets.EmotionBox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

public class CommentAdapter extends BaseAdapter {
	
	private List<Map<String,Object>> list;
	private LayoutInflater inflater;
	private Context context;
	public ListView lv;
	public BitmapUtils bitmapUtils;
	
	public CommentAdapter(Context context,List<Map<String,Object>> list){
		this.list=list;
		this.context=context;
		this.inflater = LayoutInflater.from(context);
		bitmapUtils=BitmapHelper.getHeadBitMap(context);
	}

	@Override
	public int getCount() {
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
		Map<String,Object> map=list.get(position);
		if (convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_comment_lv, null, false);
			holder.name=(TextView) convertView.findViewById(R.id.comment_lv_user);
			holder.time=(TextView) convertView.findViewById(R.id.comment_lv_time);
			holder.content=(TextView) convertView.findViewById(R.id.comment_lv_content);
			holder.head=(ImageView) convertView.findViewById(R.id.comment_lv_user_head);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(map.get("name").toString());
		holder.time.setText(map.get("time").toString());
		holder.content.setText(EmotionBox.convertNormalStringToSpannableString(context,
				map.get("content").toString()),BufferType.SPANNABLE);
		//holder.content.setText(map.get("content").toString());
		
		//跳转至个人介绍页面
		String uid=map.get("uid").toString();
		holder.name.setOnClickListener(new Onclick(context,uid));
		holder.head.setOnClickListener(new Onclick(context,uid));
		
		BitmapHelper.setHead(bitmapUtils,holder.head,uid);
		return convertView;
	}
	
	/**
	 * 跳转至个人介绍页面
	 * @author peng
	 *
	 */
	class Onclick implements OnClickListener{
		public String uid;
		public Context context;
		public Onclick(Context context,String uid){
			this.uid=uid;
			this.context=context;
		}
		public void onClick(View v) {
			Intent it=new Intent(context,FriendZone.class);
			it.putExtra("uid", uid);
			context.startActivity(it);
		}
		
	}
	
	static class ViewHolder {
		ImageView head;
		TextView name;
		TextView time;
		TextView content;
	}

}
